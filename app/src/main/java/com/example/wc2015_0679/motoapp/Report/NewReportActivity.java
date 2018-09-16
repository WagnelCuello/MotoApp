package com.example.wc2015_0679.motoapp.Report;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Models.UserModel;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewReportActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private ImageView btnUploadCamera, btnUploadPhoto,ivImg;
    private TextView tvDateLost;
    private Spinner spYearMoto, spBrandMoto;
    private Button btnSave;
    private GoogleMap mMap;
    private static final float MAP_ZOOM = 11f;
    private Uri filePath;
    private StorageReference storage;
    private ImagePicker imagePicker;
    private String archivo;
    private FirebaseDatabase mDatabase;
    private UserModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        model = new UserModel(null,null, null,null,null,
                null,null,null, 0, 0);

        tvDateLost = findViewById(R.id.tvDateLost);
        spYearMoto = findViewById(R.id.spYearMoto);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadCamera = findViewById(R.id.btnUploadCamera);
        ivImg = findViewById(R.id.ivImg);
        btnSave = findViewById(R.id.btnSaveReport);
        spBrandMoto = findViewById(R.id.spBrandMoto);

        mDatabase = FirebaseDatabase.getInstance();

        btnUploadPhoto.setOnClickListener(this);
        btnUploadCamera.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        configYears();
        configDate();
        configMap();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("save")){
            configStorage();
        }
        else if (v.getTag().equals("camera")){
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 234);
        }
        else if (v.getTag().equals("gallery")){
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 234);
            }
            catch(Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 234 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }



        /*
        if(imagePicker == null) {
            imagePicker = new ImagePicker(NewReportActivity.this, null, new OnImagePickedListener() {
                @Override
                public void onImagePicked(Uri imageUri) {
                    setectedUri = imageUri;
                    ivImg.setImageURI(imageUri);
                }
            });
        }
        if (ivImg.getDrawable() == null)
            imagePicker.choosePicture(true);

        imagePicker.handleActivityResult(resultCode,requestCode, data);
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        configMyLocation();
    }
    private void configMap(){
        // ---------------------------- MAPS ---------------------------//
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
    }
    // this method get current location and show it
    private void configMyLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    LatLng sd = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(sd).title("you are here").icon(BitmapDescriptorFactory.defaultMarker()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sd));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sd, MAP_ZOOM));

                    model.setLongitude(location.getLatitude());
                    model.setLatitude(location.getLongitude());
                }
            });
        }catch (Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(0+"-"+0);
            alert.setMessage(ex.getMessage());
            alert.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            alert.show();
        }
    }
    // this method put date into tvDateLost
    private void configDate(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        tvDateLost.setText(dateFormat.format(date));
    }
    // this method put years into spYearMoto
    private void configYears(){
        List<Integer> years = new ArrayList<>();
        for (int i = 1980; i <= 2019 ; i++)
            years.add(i);

        ArrayAdapter<Integer> adapter;
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);
        spYearMoto.setAdapter(adapter);
    }
    // this method save lost registry
    private void saveRegistry(String downloadUri){
        try {
            model.setBrand(spBrandMoto.getSelectedItem().toString());
            model.setYear(spYearMoto.getSelectedItem().toString());
            model.setDateLost(tvDateLost.getText().toString());
            model.setMotoUri(downloadUri);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> user = new HashMap<>();
            user.put("Brand", model.getBrand());
            user.put("Year", model.getYear());
            user.put("date", model.getDateLost());
            user.put("latitude", model.getLatitude());
            user.put("longitude", model.getLongitude());
            user.put("motoUri", model.getMotoUri());

            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(NewReportActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            NewReportActivity.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewReportActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception ex){
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    // this method configure upload img
    private void configStorage(){
        try {
            final StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference("motolost/"+ String.valueOf(Math.random()) + getFileExtension(filePath));
            storageReference.putFile(filePath)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        saveRegistry(downloadUri.toString());
                    }
                }
            });
        } catch (Exception ex) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

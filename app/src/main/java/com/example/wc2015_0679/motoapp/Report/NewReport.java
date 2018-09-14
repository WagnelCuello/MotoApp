package com.example.wc2015_0679.motoapp.Report;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Models.UserModel;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class NewReport extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private ImageView btnUploadPhoto,ivImg;
    private TextView tvDateLost;
    private Spinner spYearMoto, spBrandMoto;
    private Button btnSave;
    //private static final int ACTIVITY_SELECT_IMAGE = 128;
    private GoogleMap mMap;
    private static final float MAP_ZOOM = 11f;
    private Uri setectedUri;
    private StorageReference mStorageRef;
    private ImagePicker imagePicker;
    //private static int TAKE_PICTURE = 1;
    //private static int SELECT_PICTURE = 2;
    private String archivo;
    private UserModel model;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        model = new UserModel();

        tvDateLost = findViewById(R.id.tvDateLost);
        spYearMoto = findViewById(R.id.spYearMoto);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        ivImg = findViewById(R.id.ivImg);
        btnSave = findViewById(R.id.btnSaveReport);
        spBrandMoto = findViewById(R.id.spBrandMoto);

        btnUploadPhoto.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        configYears();
        configDate();
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

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("save")){
            try {
                model.setBrand(spBrandMoto.getSelectedItem().toString());
                model.setYear(spYearMoto.getSelectedItem().toString());
                model.setDateLost(tvDateLost.getText().toString());

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users").child("tubRuoZUNfLIKxlrEJy3").child("brand").setValue(model.getBrand());
                mDatabase.child("Users").child("tubRuoZUNfLIKxlrEJy3").child("year").setValue(model.getYear());
                mDatabase.child("Users").child("tubRuoZUNfLIKxlrEJy3").child("date").setValue(model.getDateLost());
                mDatabase.child("Users").child("tubRuoZUNfLIKxlrEJy3").child("latitude").setValue(model.getLatitude());
                mDatabase.child("Users").child("tubRuoZUNfLIKxlrEJy3").child("longitude").setValue(model.getLongitude());

                mDatabase.push();
                Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
            }catch (Exception ex){
                Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else if (v.getTag().equals("camera")){
            //Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            //galleryIntent.setType("image/*");
            //startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
        }
        else if (v.getTag().equals("gallery")){
            try {
                Intent icamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                archivo = Environment.getExternalStorageDirectory() + "/external_sd/Fotos/FOTO.jpg";
                File file = new File(archivo);

                Uri outputFileUri = Uri.fromFile(file);
                icamara.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(icamara,0);
                //-----------------------------------------//
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference("/motos");
                storageReference.putFile(setectedUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String imageUrl = downloadUri.toString();
                            //TODO upload to database post data + imageUrl
                        }
                    }
                });

            }
            catch(Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(imagePicker == null) {
            imagePicker = new ImagePicker(NewReport.this, null, new OnImagePickedListener() {
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
}

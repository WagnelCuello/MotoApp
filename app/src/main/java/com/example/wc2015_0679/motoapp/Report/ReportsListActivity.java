package com.example.wc2015_0679.motoapp.Report;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Models.UserModel;
import com.example.wc2015_0679.motoapp.MyRecyclerAdapter;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReportsListActivity extends AppCompatActivity {
    private ArrayList<UserModel> list;
    private FirebaseFirestore db;
    //private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView rc;
    private UserModel model;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);
        rc = findViewById(R.id.myRecyclerView);
        rc.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        getUsersList();
    }

    private void getUsersList(){
        try {
            db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                list = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    model = new UserModel(null,null, null,null,null,
                                            null,null,null, 0, 0);

                                    String id = document.getId();
                                    String motoUri = (String) document.get("motoUri");
                                    String brand = (String) document.get("Brand");
                                    String year = (String) document.get("Year");
                                    String date = (String) document.get("date");
                                    Double lon = document.getDouble("longitude");
                                    Double lat = document.getDouble("latitude");

                                    model.setCod(id);
                                    model.setMotoUri(motoUri);
                                    model.setBrand(brand);
                                    model.setYear(year);
                                    model.setDateLost(date);
                                    model.setLongitude(lon);
                                    model.setLatitude(lat);
                                    model.setUsername(mAuth.getCurrentUser().getEmail());

                                    list.add(model);
                                }

                                rc.setLayoutManager(new LinearLayoutManager(ReportsListActivity.this,
                                        LinearLayoutManager.VERTICAL, false));
                                adapter = new MyRecyclerAdapter(ReportsListActivity.this, list);
                                rc.setAdapter(adapter);
                            } else {
                                Toast.makeText(ReportsListActivity.this, "Error gettin documents", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(e.getMessage());
            alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.create().show();
        }
    }
}

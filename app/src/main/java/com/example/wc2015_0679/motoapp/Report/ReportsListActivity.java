package com.example.wc2015_0679.motoapp.Report;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Models.UserModel;
import com.example.wc2015_0679.motoapp.MyRecyclerAdapter;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReportsListActivity extends AppCompatActivity {
    private ArrayList<UserModel> list;
    private FirebaseFirestore db;
    private UserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);

        db = FirebaseFirestore.getInstance();
        getUsersList();
    }

    private void getUsersList(){
        try {
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list = new ArrayList<UserModel>();
                                    model = new UserModel(null,null, null,null,null,
                                            null,null,null, 0, 0);

                                    model.setCod(document.getId());
                                    model.setMotoUri((String) document.get("motoUri"));
                                    model.setBrand((String) document.get("Brand"));
                                    model.setYear((String) document.get("Year"));
                                    model.setDateLost((String) document.get("date"));
                                    model.setLongitude(document.getDouble("longitude"));
                                    model.setLatitude(document.getDouble("latitude"));

                                    list.add(model);

                                    RecyclerView rv = findViewById(R.id.myRecyclerView);
                                    rv.setLayoutManager(new LinearLayoutManager(ReportsListActivity.this,
                                            LinearLayoutManager.VERTICAL, false));

                                    rv.setAdapter(new MyRecyclerAdapter(ReportsListActivity.this, list));
                                }
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

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReportsListActivity extends AppCompatActivity {
    private ArrayList<UserModel> list;
    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView rc;
    private UserModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);
        rc = findViewById(R.id.myRecyclerView);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));
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
                                    fillRecycler();
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

    private void fillRecycler(){
        mDatabase.orderByChild("correousuario").equalTo(mAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.removeAll(list);

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UserModel publicacion = postSnapshot.getValue(UserModel.class);
                    //list.add(list);
                }
                //RecyclerView.Adapter adapter = new Adapter(ReportsListActivity.this, list);
                //adapter.notifyDataSetChanged();
                //rc.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportsListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

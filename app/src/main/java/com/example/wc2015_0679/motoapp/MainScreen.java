package com.example.wc2015_0679.motoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wc2015_0679.motoapp.Report.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnNewReport,btnUsers,btnReports,btnOptions;
    private TextView tvCurrentUser;
    private ImageView ivLogOut;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mAuth = FirebaseAuth.getInstance();

        tvCurrentUser = findViewById(R.id.tvCurrentUser);
        ivLogOut = findViewById(R.id.ivLogOut);
        btnNewReport = findViewById(R.id.btnNewReport);
        btnUsers = findViewById(R.id.btnUsers);
        btnReports = findViewById(R.id.btnReports);
        btnOptions = findViewById(R.id.btnOptions);

        tvCurrentUser.setOnClickListener(this);
        ivLogOut.setOnClickListener(this);
        btnNewReport.setOnClickListener(this);
        btnUsers.setOnClickListener(this);
        btnReports.setOnClickListener(this);
        btnOptions.setOnClickListener(this);

        getCurrentUser();
    }

    // this method get the current user sent from main activity
    private void getCurrentUser(){
        try {
            currentUser = getIntent().getExtras().getParcelable("user");
            tvCurrentUser.setText(currentUser.getEmail());
        }catch (Exception ex){
            showMessage("Error", ex.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("newReport")){
            startActivity(new Intent(this, MapsActivity.class));
        } else if (v.getTag().equals("users")){

        } else if (v.getTag().equals("report")){

        }else if (v.getTag().equals("options")){

        }else if(v.getTag().equals("cUser")){

        }else if(v.getTag().equals("logOut")){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Question");
            alert.setMessage("Are you sure you want log out?");
            alert.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(MainScreen.this, MainActivity.class).putExtra("return",true));
                }
            });
            alert.show();
        }
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
    }
}

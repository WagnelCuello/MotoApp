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

import com.example.wc2015_0679.motoapp.Constants.Constant;
import com.example.wc2015_0679.motoapp.Report.NewReportActivity;
import com.example.wc2015_0679.motoapp.Report.ReportsListActivity;
import com.example.wc2015_0679.motoapp.Users.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnNewReport,btnUsers,btnReports,btnOptions;
    private TextView tvCurrentUser;
    private ImageView ivLogOut;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        btnUsers.setEnabled(false);
        btnOptions.setEnabled(false);

        getCurrentUser();
    }

    // this method get the current user sent from main activity
    private void getCurrentUser(){
        try {
            currentUser = getIntent().getExtras().getParcelable("user");
            tvCurrentUser.setText(currentUser.getEmail());
        }catch (Exception ex){
            Constant.showMessage("Error",ex.getMessage(),this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("newReport")){
            startActivity(new Intent(this, NewReportActivity.class));
        } else if (v.getTag().equals("users")){
            // show Activity_User_List registered
        } else if (v.getTag().equals("report")){
            startActivity(new Intent(this, ReportsListActivity.class));
        }else if (v.getTag().equals("options")){
            // show a fragment with options for change language, change background, and more...
        }else if(v.getTag().equals("cUser")){
            // show information about current user
        }else if(v.getTag().equals("logOut")){
            if (Constant.showConfirmMessage("Are you sure you want log out?", this))
                startActivity(new Intent(MainActivity.this, LogInActivity.class)
                        .putExtra("return",true)
                );
        }
    }
}

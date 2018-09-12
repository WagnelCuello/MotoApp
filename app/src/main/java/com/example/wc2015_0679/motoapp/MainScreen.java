package com.example.wc2015_0679.motoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.wc2015_0679.motoapp.Report.NewReport;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnNewReport,btnUsers,btnReports,btnOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        btnNewReport = findViewById(R.id.btnNewReport);
        btnUsers = findViewById(R.id.btnUsers);
        btnReports = findViewById(R.id.btnReports);
        btnOptions = findViewById(R.id.btnOptions);

        btnNewReport.setOnClickListener(this);
        btnUsers.setOnClickListener(this);
        btnReports.setOnClickListener(this);
        btnOptions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("newReport")){
            startActivity(new Intent(this,MapsActivity.class));
            //NewReport nr = new NewReport();
            //nr.show(getSupportFragmentManager(), "NewReport");
        } else if (v.getTag().equals("users")){

        } else if (v.getTag().equals("report")){

        }else if (v.getTag().equals("options")){

        }
    }
}

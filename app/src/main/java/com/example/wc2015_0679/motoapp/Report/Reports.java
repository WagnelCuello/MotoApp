package com.example.wc2015_0679.motoapp.Report;

import android.content.ContextWrapper;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wc2015_0679.motoapp.R;

public class Reports extends AppCompatActivity {
    private TableLayout tableLayout;
    private TableRow tableRow;
    private TextView textView;
    private RelativeLayout relativeLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        try {
            tableLayout = findViewById(R.id.list);

            for (int n = 0; n < 3; n++){
                tableRow = new TableRow(getBaseContext());

                imageView = new ImageView(this);
                imageView.setImageResource(R.mipmap.users);
                tableRow.addView(imageView);

                relativeLayout = new RelativeLayout(this);
                textView = new TextView(this);
                textView.setText("  " + "wcuello@gmail.com");
                relativeLayout.addView(textView);

                textView = new TextView(this);
                textView.setText("  " + "Motor: " + "BMW");
                textView.setTop(25);
                relativeLayout.addView(textView);

                tableRow.addView(relativeLayout);
                tableLayout.addView(tableRow);
            }
        }catch (Exception ex){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(ex.getMessage());
            alert.show();
        }
    }
}

package com.example.wc2015_0679.motoapp.Report;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ReportsListActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private TableRow tableRow;
    private TextView textView;
    private RelativeLayout relativeLayout;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(ReportsListActivity.this);
                                alert.setMessage(document.getId()+"-"+document.getData());
                                alert.show();
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Toast.makeText(ReportsListActivity.this, "Error gettin documents", Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


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

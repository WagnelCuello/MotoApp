package com.example.wc2015_0679.motoapp.Users;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnExit, btnSave;
    private FirebaseAuth mAuth;
    private EditText etUserName, etPassword1, etPassword2;
    private String username,password1,password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        mAuth = FirebaseAuth.getInstance();

        btnExit = findViewById(R.id.btnExit);
        btnSave = findViewById(R.id.btnSave);
        etUserName = findViewById(R.id.etUsername);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);
        btnExit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("exit")){
            this.finish();
        } else if(v.getTag().equals("save")) {
            username = etUserName.getText().toString();
            password1 = etPassword1.getText().toString();
            password2 = etPassword2.getText().toString();

            if (validate()) {
                mAuth.createUserWithEmailAndPassword(username, password1)
                        .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    RegistryActivity.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate(){
        boolean ret = true;

        if (username.trim().isEmpty() || password1.trim().isEmpty() || password2.trim().isEmpty())
            ret = false;
        else if (!password1.equals(password2))
            ret = false;

        return ret;
    }
}

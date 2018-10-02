package com.example.wc2015_0679.motoapp.Users;

import android.app.ActivityOptions;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Constants.Constant;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistryActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnExit,btnSave;
    private FirebaseAuth mAuth;
    private EditText etUserName,etPassword1,etPassword2;
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
            if (validate()) {
                try {
                    mAuth.createUserWithEmailAndPassword(username, password1)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        RegistryActivity.this.finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }catch (Exception e){
                    Constant.showMessage("Exception", e.getMessage(),this);
                }
            } else {
                Constant.showMessage("Error","Username or Password incorrect",this);
            }
        } else if (v.getTag().equals("google")){

        }
    }

    private boolean validate(){
        boolean option = true;

        username = etUserName.getText().toString();
        password1 = etPassword1.getText().toString();
        password2 = etPassword2.getText().toString();

        if (username.trim().isEmpty() || password1.trim().isEmpty() || password2.trim().isEmpty())
            option = false;
        else if (!password1.equals(password2))
            option = false;

        return option;
    }
}

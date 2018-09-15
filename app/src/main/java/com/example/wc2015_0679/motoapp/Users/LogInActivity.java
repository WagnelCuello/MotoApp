package com.example.wc2015_0679.motoapp.Users;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.MainActivity;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etUsername, etPassword;
    private ImageButton btnLogin, btnRegister;
    private Pattern pattern;
    private String username,password;
    private android.widget.ProgressBar bar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bar = findViewById(R.id.progressHorizontal);
        showProgressBar(true);

        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        boolean ret = getIntent().getBooleanExtra("return", false);

        if (ret){
            FirebaseAuth.getInstance().signOut();
            showProgressBar(false);
        }else{
            verifyLogin();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("log")) getLogin();
        if (v.getTag().equals("reg")) getRegister();
    }

    // this method verify if there is a current user
    private void verifyLogin(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            getMainScreen(mAuth.getCurrentUser());
        else
            btnRegister.setVisibility(View.VISIBLE);

        showProgressBar(false);
    }

    private void getMainScreen(FirebaseUser user){
        showProgressBar(true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        showProgressBar(false);
    }

    // this method show a progressbar
    private void showProgressBar(boolean option){
        if (option == true) {
            bar.setIndeterminate(true);
        }else{
            bar.setIndeterminate(false);
        }
    }

    // this method configure login
    private void getLogin() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.trim().isEmpty())
            etUsername.setError("Username can not be empty!");
        else if (password.trim().isEmpty())
            etPassword.setError("Password can not be empty!");
        else {
            try {
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getMainScreen(mAuth.getCurrentUser());
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(LogInActivity.this);
                                    dialog.setTitle("Error");
                                    dialog.setMessage("Authentication failed");
                                    dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                }
                            }
                        });
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        showProgressBar(false);
    }

    // this method is to registry a new user
    private void getRegister() {
        startActivity(new Intent(this, RegistryActivity.class));
    }
}

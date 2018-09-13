package com.example.wc2015_0679.motoapp;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.IconCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Users.RegistryUsers;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etUsername, etPassword;
    private ImageButton btnLogin, btnRegister;
    private Pattern pattern;
    private String username,password;
    private android.widget.ProgressBar bar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.progressHorizontal);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        boolean ret = getIntent().getBooleanExtra("return", false);

        if (ret){
            showProgressBar(true);
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
        showProgressBar(true);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            getMainScreen(mAuth.getCurrentUser());
        else
            btnRegister.setVisibility(View.VISIBLE);

        showProgressBar(false);
    }

    private void getMainScreen(FirebaseUser user){
        showProgressBar(true);
        Intent intent = new Intent(this, MainScreen.class);
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
        showProgressBar(true);

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.trim().length() == 0)
            Toast.makeText(this, "Username can not be empty!", Toast.LENGTH_LONG).show();
        else if (password.trim().length() == 0)
            Toast.makeText(this, "Password can not be empty!", Toast.LENGTH_LONG).show();
        else {
            try {
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getMainScreen(mAuth.getCurrentUser());
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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

    private boolean validate(){
        boolean option = false;

        if (TextUtils.isEmpty(etUsername.getText().toString())) etUsername.setError("Username can not be empty");
        else if (TextUtils.isEmpty(etPassword.getText().toString())) etUsername.setError("Password can not be empty");
        else if (!isValidUserName(etUsername.getText().toString())) etUsername.setError("username is not valid");
        else if (TextUtils.isEmpty(etUsername.getText().toString()) && TextUtils.isEmpty(etPassword.getText().toString())) option = true;

        return option;
    }

    private boolean isValidUserName(String email) {
        pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // this method is to registry a new user
    private void getRegister() {
        RegistryUsers dialog = new RegistryUsers();
        dialog.show(getFragmentManager(), "RegistryUsers");
    }
}

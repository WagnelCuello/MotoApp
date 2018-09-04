package com.example.wc2015_0679.motoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etUsername, etPassword;
    private ImageButton btnLogin, btnRegister;
    private Pattern pattern;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String username, password;
    private GoogleSignInOptions gso;

    //private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("log")) getLogin();
        if (v.getTag().equals("reg")) getRegister();
    }

    // this method configure login
    private void getLogin(){
        startActivity(new Intent(this, MainScreen.class));
        if (validate()) {
            //Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();
            progressBar.setVisibility(View.VISIBLE);

            startActivity(new Intent(this, MainScreen.class));

            //create user

/*
            //create user
            auth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(getContext(), "createUserWithUsername:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
/*
                    if (!task.isSuccessful())
                        Toast.makeText(getContext(), "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    else startActivity(new Intent(this, MainFragment.class));
                }
            }); */
        }
    }

    private boolean validate(){
        boolean option = false;

        if (TextUtils.isEmpty(username)) etUsername.setError("Username can not be empty");
        else if (TextUtils.isEmpty(password)) etUsername.setError("Password can not be empty");
        else if (!isValidUserName(username)) etUsername.setError("username is not valid");
        else if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) option = true;

        return option;
    }

    private boolean isValidUserName(String email) {
        pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // this method is to registry a new user
    private void getRegister() {
        //Log.d(TAG, "onClick: opening dialog.");
        RegistryUsers dialog = new RegistryUsers();
        dialog.show(getFragmentManager(), "RegistryUsers");
    }
}

package com.example.wc2015_0679.motoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LogIn extends Fragment implements View.OnClickListener{
    private EditText etUsername, etPassword;
    private TextView tvRegister;
    private ImageButton btnLogin;
    private Pattern pattern;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);

        etUsername = rootView.findViewById(R.id.etUser);
        etPassword = rootView.findViewById(R.id.etPass);
        btnLogin = rootView.findViewById(R.id.btnLogin);
        tvRegister = rootView.findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == "log") getLogin();
        if (v.getTag() == "reg") getRegister();
    }

    // this method configure login
    private void getLogin(){
        if ( !isValidUserName(etUsername.getText().toString()) ){
            etUsername.setError("username is not valid");
            Toast.makeText(getContext(), "Username is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidUserName(String email) {
        pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // this method is to register a new user
    private void getRegister(){

    }
}

package com.example.wc2015_0679.motoapp.Users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.MainActivity;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class RegistryUsers extends DialogFragment implements View.OnClickListener{
    //private static final String TAG = "RegistryUsers";
    private ImageButton btnExit, btnSave;
    private FirebaseAuth mAuth;
    private EditText etUserName, etPassword1, etPassword2;
    private String username,password1,password2;
    private Context thisContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registry_users, container, false);

        mAuth = FirebaseAuth.getInstance();

        btnExit = rootView.findViewById(R.id.btnExit);
        btnSave = rootView.findViewById(R.id.btnSave);
        etUserName = rootView.findViewById(R.id.etUsername);
        etPassword1 = rootView.findViewById(R.id.etPassword1);
        etPassword2 = rootView.findViewById(R.id.etPassword2);
        btnExit.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        thisContext = container.getContext();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("exit")){
            getDialog().dismiss();
        }else if(v.getTag().equals("save")){
            username = etUserName.getText().toString();
            password1 = etPassword1.getText().toString();
            password2 = etPassword2.getText().toString();

            if (validate()) {
                mAuth.createUserWithEmailAndPassword(username, password1)
                        .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "Authentication successful");
                                } else {
                                    //Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private boolean validate(){
        if (username.trim().isEmpty()){
            //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}

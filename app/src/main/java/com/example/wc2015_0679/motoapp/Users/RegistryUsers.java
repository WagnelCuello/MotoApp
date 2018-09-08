package com.example.wc2015_0679.motoapp.Users;

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

import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistryUsers extends DialogFragment implements View.OnClickListener{
    //private static final String TAG = "RegistryUsers";
    private ImageButton btnExit, btnSave;
    private FirebaseAuth mAuth;
    private EditText etUserName, etPassword1, etPassword2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registry_users, container, false);

        btnExit = rootView.findViewById(R.id.btnExit);
        btnSave = rootView.findViewById(R.id.btnSave);
        etUserName = rootView.findViewById(R.id.etUsername);
        etPassword1 = rootView.findViewById(R.id.etPassword1);
        etPassword2 = rootView.findViewById(R.id.etPassword2);
        btnExit.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("exit")){
            getDialog().dismiss();
        }else if(v.getTag().equals("save")){
            mAuth.signInWithEmailAndPassword(etUserName.getText().toString(), etPassword1.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                    }else{

                    }
                }
            });
            getDialog().dismiss();
        }
    }
}

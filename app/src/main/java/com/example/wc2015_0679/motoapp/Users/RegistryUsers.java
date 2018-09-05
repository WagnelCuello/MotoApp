package com.example.wc2015_0679.motoapp.Users;

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

public class RegistryUsers extends DialogFragment implements View.OnClickListener{
    //private static final String TAG = "RegistryUsers";
    private ImageButton btnExit, btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registry_users, container, false);

        btnExit = rootView.findViewById(R.id.btnExit);
        btnSave = rootView.findViewById(R.id.btnSave);
        btnExit.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("exit")){
            //Log.d(TAG, "onClick: capturing input");
            getDialog().dismiss();
        }else if(v.getTag().equals("save")){
            //Log.d(TAG, "onClick: capturing input");
            getDialog().dismiss();
        }
    }
}

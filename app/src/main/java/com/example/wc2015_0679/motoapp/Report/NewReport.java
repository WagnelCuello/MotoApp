package com.example.wc2015_0679.motoapp.Report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.DateInterval;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;

public class NewReport extends DialogFragment {
    private Button btnUploadImg;
    private Spinner spYearMoto;
    private Integer[] years;
    private static final int ACTIVITY_SELECT_IMAGE = 128;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_report, container, false);
        btnUploadImg = rootView.findViewById(R.id.btnUploadImg);
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
            }
        });
        spYearMoto = rootView.findViewById(R.id.spYearMoto);
        for (int i = 2000; i <= 2018; i++){
            years[i] = i;
        }

        spYearMoto.setAdapter(new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, years));
        return rootView;
    }
}


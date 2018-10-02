package com.example.wc2015_0679.motoapp.Constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class Constant {
    private static final String CLOSE_BUTTON = "CLOSE";
    private static final String NEUTRAL_BUTTON = "OK";
    private static final String NEGATIVE_BUTTON = "NO";
    private static final String POSITIVE_BUTTON = "YES";
    private static boolean option = false;

    // this method show a dialog message
    public static void showMessage(String title, String message, Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(CLOSE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create().show();
    }

    // this method show a confirm message dialog
    public static boolean showConfirmMessage(String message, Context context){
        AlertDialog.Builder confirm = new AlertDialog.Builder(context);
        confirm.setTitle("Question");
        confirm.setMessage(message);
        confirm.setNegativeButton(NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        confirm.setPositiveButton(POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                option = true;
            }
        });
        confirm.create().show();
        return option;
    }

    // this method show a progressbar
    public static void showProgressBar(RelativeLayout relativeLayout, boolean isVisible){
        if (isVisible)
            relativeLayout.setVisibility(View.VISIBLE);
        else
            relativeLayout.setVisibility(View.INVISIBLE);
    }
}

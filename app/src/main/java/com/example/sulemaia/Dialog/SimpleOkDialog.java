package com.example.sulemaia.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class SimpleOkDialog {

    private Context context;
    private String title;
    private String message;


    public SimpleOkDialog(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
    }

    public AlertDialog.Builder build(){
        return new AlertDialog.Builder(context)
                .setPositiveButton("OK", null)
                .setMessage(message)
                .setTitle(title);
    }
}

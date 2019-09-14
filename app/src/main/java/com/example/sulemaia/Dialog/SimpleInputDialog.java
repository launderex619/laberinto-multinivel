package com.example.sulemaia.Dialog;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

public class SimpleInputDialog {

    private Context context;
    private String title;
    private String message;
    private EditText text;

    public SimpleInputDialog(Context context, String title, String message){
        text = new EditText(context);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
    }


    public AlertDialog.Builder build(){
        return new AlertDialog.Builder(context)
                .setView(text)
                .setPositiveButton("OK", null)
                .setMessage(message)
                .setTitle(title);
    }
}

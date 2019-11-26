package com.example.sulemaia.Dialog;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

/**
 * Creation of a dialog for user input.
 */
public class SimpleInputDialog {

    private Context context;
    private String title;
    private String message;
    private EditText text;

    /**
     * Instantiation of the input dialog, with each variable related to a paremeter.
     * @param context of the activity for the dialog.
     * @param title of the dialog.
     * @param message of the dialog.
     */
    public SimpleInputDialog(Context context, String title, String message){
        text = new EditText(context);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    /**
     * Builder of the actual input dialog.
     * @return the whole dialog already formed.
     */
    public AlertDialog.Builder build(){
        return new AlertDialog.Builder(context)
                .setView(text)
                .setPositiveButton("OK", null)
                .setMessage(message)
                .setTitle(title);
    }
}

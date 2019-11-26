package com.example.sulemaia.Dialog;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Creation of a class to form popup dialogs in any context of activity.
 */
public class SimpleOkDialog {

    private Context context;
    private String title;
    private String message;

    /**
     * We initialize each parameter needed for the dialog.
     * @param context activity context to insert the dialog.
     * @param title title of the dialog.
     * @param message message of the dialog.
     */
    public SimpleOkDialog(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
    }

    /**
     * Builder of the actual dialog.
     * @return the whole dialog, configured.
     */
    public AlertDialog.Builder build(){
        return new AlertDialog.Builder(context)
                .setPositiveButton("OK", null)
                .setMessage(message)
                .setTitle(title);
    }
}

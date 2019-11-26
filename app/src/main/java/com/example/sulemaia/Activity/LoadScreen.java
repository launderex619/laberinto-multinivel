package com.example.sulemaia.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Helper.GraphViz;
import com.example.sulemaia.R;

import java.io.File;

/**
 * <h1>
 *  Initial class
 * </h1>
 * <br>
 * Class for the load screen of the app.
 */
public class LoadScreen extends AppCompatActivity {

    @Override
    /**
     * We show the splash theme, create a new intent, and start the app.
     */
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

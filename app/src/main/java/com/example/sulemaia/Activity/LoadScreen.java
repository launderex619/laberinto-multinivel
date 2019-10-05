package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

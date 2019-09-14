package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.sulemaia.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

}

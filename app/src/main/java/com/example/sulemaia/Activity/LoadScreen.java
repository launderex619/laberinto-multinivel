package com.example.sulemaia.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Helper.GraphViz;
import com.example.sulemaia.R;

import java.io.File;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        gv.addln("A -> B;");
        gv.addln("A -> C;");
        gv.addln(gv.end_graph());
        System.out.println(gv.getDotSource());

        String type = "png";
        File out = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "temp.png");
        String temp = gv.getDotSource();
        Bitmap bmp = gv.generateImage();
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

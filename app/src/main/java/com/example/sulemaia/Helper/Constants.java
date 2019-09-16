package com.example.sulemaia.Helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    public static final int REQUEST_FILE_READ_EXTERNAL = 2;
    public static final int RESULT_FOR_FIELD_INFORMATION = 3;
    public static final int mapValues[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,},
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 3},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}};
    public static final String biomes[] = {
            "Tundra",
            "Desierto",
            "Taiga",
            "Selva",
            "Chaparral",
            "Arrecife de coral",
            "Rio",
            "Pastizales",
            "Océano",
            "Sabana",
            "Bosque templado",
            "Humedal",
            "Montaña",
            "Camino",
            "Bosque",
            "Lava",
            "Tonala"
    };
}

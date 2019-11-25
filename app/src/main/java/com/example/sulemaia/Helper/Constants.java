package com.example.sulemaia.Helper;

import android.graphics.drawable.Drawable;

import com.example.sulemaia.Model.CharacterItem;

import java.util.Hashtable;

public class Constants {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    public static final int REQUEST_FILE_READ_EXTERNAL = 2;
    public static final int RESULT_FOR_FIELD_INFORMATION = 3;
    public static final int RESULT_FOR_CHARACTER_EDITOR = 4;
    public static final int REQUEST_TUTORIAL = 5;
    public static final int[][] mapValues = {
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
    public static final String[] biomes = {
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
            "Tonala",
            "Escaleras",
            "Pantano",
            "Cielo Nublado",
            "Edificaciones",
            "Arena Movediza"
    };
    public static final String PREF_KEY_FIRST_START = "com.example.sulemaia.PREF_KEY_FIRST_START";
    public static final String PREF_KEY_FIRST_START_MAIN_ACTIVITY = "com.example.sulemaia.PREF_KEY_FIRST_START_MAIN_ACTIVITY";
    public static final String PREF_KEY_FIRST_START_CHARACTER_EDITOR = "com.example.sulemaia.PREF_KEY_FIRST_START_CHARACTER_EDITOR";
    public static final String PREF_KEY_FIRST_START_CHARACTER_SELECTOR = "com.example.sulemaia.PREF_KEY_FIRST_START_CHARACTER_SELECTOR";
    public static final String PREF_KEY_FIRST_START_GAME_SCREEN = "com.example.sulemaia.PREF_KEY_FIRST_START_GAME_SCREEN";
    public static final String PREF_KEY_FIRST_START_FIELD_INFORMATION = "com.example.sulemaia.PREF_KEY_FIRST_START_FIELD_INFORMATION";
    public static final String PREF_KEY_FIRST_START_EXPANSION_DETAILS = "com.example.sulemaia.PREF_KEY_FIRST_START_EXPANSION_DETAILS";
    public static final int BLACK_COLOR = 0xFF000000;
    public static final String PREF_KEY_FIRST_START_UNIFORM_COST_SCREEN = "com.example.sulemaia.PREF_KEY_FIRST_START_UNIFORM_COST_SCREEN";
    public static Drawable[] characterIcons;
    public static CharacterItem[] charactersConstant = {
            new CharacterItem(0, "Calamar", Constants.biomes[8]),
            new CharacterItem(1, "Cabra", Constants.biomes[12]),
            new CharacterItem(2, "Mariposa", Constants.biomes[14]),
    };
}

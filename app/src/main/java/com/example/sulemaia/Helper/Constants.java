package com.example.sulemaia.Helper;

import android.graphics.drawable.Drawable;

import com.example.sulemaia.Model.CharacterItem;

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
    public static Drawable[] characterIcons;
    public static CharacterItem[] charactersConstant = {
            new CharacterItem(0, "Calamar", Constants.biomes[8]),
            new CharacterItem(1, "Cabra", Constants.biomes[12]),
            new CharacterItem(2, "Mariposa", Constants.biomes[14]),
    };

}

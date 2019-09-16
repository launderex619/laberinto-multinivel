package com.example.sulemaia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CharacterSelector extends AppCompatActivity {

    private FloatingActionButton fabAddCharacter;
    private RecyclerView rvCharacters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selector);

        fabAddCharacter = findViewById(R.id.fab_add_character);
        rvCharacters = findViewById(R.id.rv_character_items);

    }
}

package com.example.sulemaia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.sulemaia.Adapter.CharacterSelectorAdapter;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

import static com.example.sulemaia.Helper.Constants.biomes;

public class CharacterSelector extends AppCompatActivity {

    private FloatingActionButton fabAddCharacter;
    private RecyclerView rvCharacters;
    private ArrayList<CharacterItem> characters;
    private CharacterSelectorAdapter characterAdapter;
    private ArrayList<String> biomes = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private int initialX, initialY, finalX, finalY;
    private String contentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selector);
        CharacterItem[] charactersConstant = {
                new CharacterItem(getDrawable(R.drawable.calamar), "Calamar", Constants.biomes[8]),
                new CharacterItem(getDrawable(R.drawable.cabra), "Cabra", Constants.biomes[12]),
                new CharacterItem(getDrawable(R.drawable.mariposa), "Mariposa", Constants.biomes[14]),
        };

        Intent intent = getIntent();
        biomes.addAll(intent.getStringArrayListExtra("biomes"));
        contentFile = intent.getStringExtra("contentFile");
        colors.addAll(intent.getIntegerArrayListExtra("colors"));
        codes.addAll(intent.getIntegerArrayListExtra("codes"));
        initialX = intent.getIntExtra("initialX", 0);
        finalX = intent.getIntExtra("finalX", 0);
        initialY = intent.getIntExtra("initialY", 0);
        finalY = intent.getIntExtra("finalY", 0);

        characters = new ArrayList<>();
        characters.addAll(Arrays.asList(charactersConstant));

        for (CharacterItem item : characters) {
            ArrayList<Float> costs = new ArrayList<>();
            for (int i = 0; i < biomes.size(); i++){
                costs.add((float) ((Math.round( (float) Math.random()*100f) * 100f) / 100d));
            }
            item.setLands(biomes);
            item.setLandsCosts(costs);
            item.setLandsColors(colors);
        }

        fabAddCharacter = findViewById(R.id.fab_add_character);
        rvCharacters = findViewById(R.id.rv_character_items);
        characterAdapter = new CharacterSelectorAdapter(characters,
                R.layout.item_character_selector_activity,
                this);
        LinearLayoutManager mainLayoutManager = new LinearLayoutManager(getApplicationContext());
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCharacters.setLayoutManager(mainLayoutManager);
        rvCharacters.setAdapter(characterAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }
}

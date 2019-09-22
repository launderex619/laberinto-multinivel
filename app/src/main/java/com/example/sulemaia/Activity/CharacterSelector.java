package com.example.sulemaia.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Adapter.CharacterSelectorAdapter;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.sulemaia.Helper.Constants.charactersConstant;

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

        // TODO: necesitamos mas iconos para los personajes, pienso que unos 10 estarian bien
        Constants.characterIcons = new Drawable[]{
                getDrawable(R.drawable.calamar),
                getDrawable(R.drawable.cabra),
                getDrawable(R.drawable.mariposa)
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
            ArrayList<Boolean> canPass = new ArrayList<>();
            for (int i = 0; i < biomes.size(); i++) {
                costs.add(0.00f);
                canPass.add(true);
            }
            item.setLands(biomes);
            item.setLandsCosts(costs);
            item.setLandsColors(colors);
            item.setCanPass(canPass);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.RESULT_FOR_CHARACTER_EDITOR) {

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }
}

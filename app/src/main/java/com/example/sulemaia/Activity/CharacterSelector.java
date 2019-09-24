package com.example.sulemaia.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Adapter.CharacterSelectorAdapter;
import com.example.sulemaia.Dialog.SimpleOkDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Interface.iCharacterSelected;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.sulemaia.Helper.Constants.charactersConstant;

public class CharacterSelector extends AppCompatActivity implements iCharacterSelected {

    private FloatingActionButton fabAddCharacter, fabNext;
    private LinearLayoutManager mainLayoutManager;
    private RecyclerView rvCharacters;
    private ArrayList<CharacterItem> characters;
    private CharacterSelectorAdapter characterAdapter;
    private ArrayList<String> biomes = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private int initialX, initialY, finalX, finalY;
    private String contentFile, initialNameField, finalNameField;
    private ButtonActions buttonActions;
    private int characterSelected;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selector);

        /* TODO: necesitamos mas iconos para los personajes, pienso que unos 10 estarian bien, te recomiendo cambirles el color de fondo antes de agregarlas para que no tengas que cambiar la misma imagen 6 veces xdxd
         TODO: sobre los mismos iconos, cambiales el color de fondo, al color background_color definido en colors.xml
         TODO: las imagenes las consigues en la carpeta drawable, te recomiendo photoshop xd, abres la imagen y sobre ella lo cambias, si vez que son muchas imagenes del mismo tipo en carpetas diferentes, estan asi por que son para cada tamaño de pantalla, es mucha lata, srry
         TODO: instala las extenciones que te mande, con una agregas iconos oficiales de google mas facil, y con la otra importas las imagenes al proyecto (las adapta a los distintos tipos de pantalla automaticamente) si no sabes instalar plugings busca como xD
         TODO: borra todos los todo conforme los vayas realizando xdxdxdxdxdxdxdxd
        */
        Constants.characterIcons = new Drawable[]{
                getDrawable(R.drawable.calamar),
                getDrawable(R.drawable.cabra),
                getDrawable(R.drawable.mariposa)
        };

        Intent intent = getIntent();
        biomes.addAll(intent.getStringArrayListExtra("biomes"));
        contentFile = intent.getStringExtra("contentFile");
        initialNameField = intent.getStringExtra("initialName");
        finalNameField = intent.getStringExtra("finalName");
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
            item.setMainLand(item.getLands().get(0));
        }

        fabAddCharacter = findViewById(R.id.fab_add_character);
        fabNext = findViewById(R.id.fab_character_selector_next);
        rvCharacters = findViewById(R.id.rv_character_items);
        characterAdapter = new CharacterSelectorAdapter(this, characters,
                R.layout.item_character_selector_activity,
                this);
        fabNext.setVisibility(View.GONE);
        buttonActions = new ButtonActions();

        fabAddCharacter.setOnClickListener(buttonActions);
        fabNext.setOnClickListener(buttonActions);

        mainLayoutManager = new LinearLayoutManager(getApplicationContext());
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCharacters.setLayoutManager(mainLayoutManager);
        rvCharacters.setAdapter(characterAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RESULT_FOR_CHARACTER_EDITOR) {
                int pos = data.getIntExtra("position", 0);
                int i = 0;
                float lowestCost = Float.MAX_VALUE;
                CharacterItem item = (CharacterItem) data.getSerializableExtra("item");
                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                if (isDeleted) {
                    if (characters.size() > 1) {
                        characters.remove(pos);
                        characterAdapter.notifyItemRemoved(pos);
                    } else {
                        (new SimpleOkDialog(CharacterSelector.this, getString(R.string.error),
                                getString(R.string.cant_less_1_item)))
                                .build()
                                .setIcon(R.drawable.ic_warning_lime_24dp)
                                .show();
                    }
                } else {
                    characters.set(pos, item);
                }
                pos = 0;
                for (float f : item.getLandsCosts()) {
                    if (f < lowestCost) {
                        pos = i;
                        if (item.getCanPass().get(pos)) {
                            lowestCost = f;
                        }
                    }
                    i++;
                }
                item.setMainLand(item.getLands().get(pos));
                characterAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setCharacter(int pos) {
        for (int i = 0; i < mainLayoutManager.getChildCount(); i++) {
            AppCompatCheckBox cb = mainLayoutManager.getChildAt(i).findViewById(R.id.cb_item_select_character);
            if (pos != i) {
                cb.setChecked(false);
            } else {
                if (cb.isChecked()) {
                    fabNext.setVisibility(View.VISIBLE);
                    characterSelected = pos;
                } else {
                    fabNext.setVisibility(View.GONE);
                }
            }
        }
    }

    private class ButtonActions implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabAddCharacter) {
                if (characters.size() < 5) {
                    CharacterItem item = new CharacterItem(0,
                            charactersConstant[0].getName(),
                            characters.get(0).getLands().get(0));
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
                    characters.add(item);
                    characterAdapter.notifyDataSetChanged();
                } else {
                    (new SimpleOkDialog(CharacterSelector.this, getString(R.string.error),
                            getString(R.string.cant_more_5_items)))
                            .build()
                            .setIcon(R.drawable.ic_warning_lime_24dp)
                            .show();
                }
            } else if (v == fabNext) {
                CharacterItem item = characters.get(characterSelected);
                String landCantPass;
                int i = 0;
                boolean canPass = true;
                for (String land : item.getLands()) {
                    if (land.equals(initialNameField)) {
                        if (!item.getCanPass().get(i)) {
                            canPass = false;
                            (new SimpleOkDialog(CharacterSelector.this, getString(R.string.error),
                                    getString(R.string.cant_start_in_field)))
                                    .build()
                                    .setIcon(R.drawable.ic_warning_lime_24dp)
                                    .show();
                        }
                    }
                    else if (land.equals(finalNameField)){
                        if (!item.getCanPass().get(i)) {
                            canPass = false;
                            (new SimpleOkDialog(CharacterSelector.this, getString(R.string.error),
                                    getString(R.string.cant_finish_in_field)))
                                    .build()
                                    .setIcon(R.drawable.ic_warning_lime_24dp)
                                    .show();
                        }
                    }
                    i++;
                }
                if (canPass){
                    Intent intent = new Intent(getApplicationContext(), GameScreen.class);
                    intent.putExtra("contentFile", contentFile);
                    intent.putExtra("character", characters.get(characterSelected));
                    intent.putExtra("initialX", initialX);
                    intent.putExtra("finalX", finalX);
                    intent.putExtra("initialY", initialY);
                    intent.putExtra("finalY", finalY);
                    intent.putExtra("biomes", biomes);
                    intent.putExtra("colors", colors);
                    intent.putExtra("codes", codes);
                    startActivity(intent);
                }
            }
        }
    }
}

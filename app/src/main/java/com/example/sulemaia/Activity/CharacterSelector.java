package com.example.sulemaia.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.Interface.iCharacterSelected;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
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

        Constants.characterIcons = new Drawable[]{
                getDrawable(R.drawable.calamar),
                getDrawable(R.drawable.cabra),
                getDrawable(R.drawable.mariposa),
                getDrawable(R.drawable.hada),
                getDrawable(R.drawable.claptrap),
                getDrawable(R.drawable.dragon),
                getDrawable(R.drawable.link),
                getDrawable(R.drawable.ogro),
                getDrawable(R.drawable.slime),
                getDrawable(R.drawable.tiburon),
        };

        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START_CHARACTER_SELECTOR, true);
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

        if (firstStart) {
            TapTargetView.showFor(this,
                    new TapTargetHelper(this,
                            fabAddCharacter,
                            getString(R.string.add_character_title),
                            getString(R.string.add_character_description)).Create(),
                    null
            );
        }
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
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START_CHARACTER_SELECTOR, true);
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
        if (firstStart) {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean(Constants.PREF_KEY_FIRST_START_CHARACTER_SELECTOR, false)
                    .apply();
            TapTargetView.showFor(this,
                    new TapTargetHelper(this,
                            fabNext,
                            getString(R.string.game_activity),
                            getString(R.string.game_description)).Create(),
                    null
            );
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
                    } else if (land.equals(finalNameField)) {
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
                if (canPass) {
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

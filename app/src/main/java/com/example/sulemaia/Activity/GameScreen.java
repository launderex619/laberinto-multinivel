package com.example.sulemaia.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Dialog.SimpleOkDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Helper.Parser;
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.sulemaia.Helper.Constants.characterIcons;

public class GameScreen extends AppCompatActivity {

    float textSize = 8;
    private ArrayList<String> biomes = new ArrayList<>();
    private String contentFile;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private ArrayList<EditText> etLands = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY, actualStep = 0, mapValues[][];
    private CharacterItem character;
    private TableLayout tlTableMap;
    private FloatingActionButton fabUp, fabLeft, fabDown, fabRight;
    private EditText board[][];
    private ButtonActions buttonActions;
    private Drawable characterIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent intent = getIntent();
        biomes.addAll(intent.getStringArrayListExtra("biomes"));
        contentFile = intent.getStringExtra("contentFile");
        colors.addAll(intent.getIntegerArrayListExtra("colors"));
        codes.addAll(intent.getIntegerArrayListExtra("codes"));
        initialX = intent.getIntExtra("initialX", 0);
        finalX = intent.getIntExtra("finalX", 0);
        initialY = intent.getIntExtra("initialY", 0);
        finalY = intent.getIntExtra("finalY", 0);
        character = (CharacterItem) intent.getSerializableExtra("character");

        tlTableMap = findViewById(R.id.tl_game_table_map);

        fabUp = findViewById(R.id.fab_game_up);
        fabDown = findViewById(R.id.fab_game_down);
        fabLeft = findViewById(R.id.fab_game_left);
        fabRight = findViewById(R.id.fab_game_right);

        buttonActions = new ButtonActions();
        fabUp.setOnClickListener(buttonActions);
        fabRight.setOnClickListener(buttonActions);
        fabLeft.setOnClickListener(buttonActions);
        fabDown.setOnClickListener(buttonActions);
        tlTableMap.post(new Runnable() {
            @Override
            public void run() {
                createTable(Parser.getFileArray(contentFile));
                loadBoard();
            }
        });
    }

    private void loadBoard() {
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START_GAME_SCREEN, true);
        actualX = initialX;
        actualY = initialY;
        Bitmap bitmap = ((BitmapDrawable) characterIcons[character.getIcon()]).getBitmap();
        characterIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) textSize * 3, (int) textSize * 3, true));
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
        board[actualY][actualX].setText(String.valueOf(actualStep));
        board[actualY][actualX].setText("I");
        board[actualY][actualX].setTextColor(Color.WHITE);
        board[finalY][finalX].setText("F");
        board[finalY][finalX].setTextColor(Color.WHITE);
        if (firstStart) {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean(Constants.PREF_KEY_FIRST_START_GAME_SCREEN, false)
                    .apply();

            TapTargetView.showFor(this,
                    new TapTargetHelper(this,
                            board[actualY][actualX],
                            getString(R.string.game_field),
                            getString(R.string.game_field_description)).Create(),
                    null
            );
        }

    }

    private void createTable(int[][] mapValues) {
        this.mapValues = mapValues;
        board = new EditText[mapValues.length][mapValues[0].length];
        for (int i = 0; i < mapValues.length; i++) {
            TableRow tableRow = new TableRow(GameScreen.this);
            tableRow.setGravity(Gravity.CENTER);
            if (i == 0) {
                TableRow tableRowT = new TableRow(GameScreen.this);
                tableRowT.setGravity(Gravity.CENTER);
                for (int j = 0; j <= mapValues[i].length; j++) {
                    TextView tv = new TextView(GameScreen.this);
                    tv.setText(Parser.getLetterForInt(j));
                    tv.setTextSize(textSize);
                    tv.setGravity(Gravity.CENTER);
                    tableRowT.addView(tv, new TableRow.LayoutParams(tlTableMap.getWidth() / mapValues[0].length,
                            tlTableMap.getHeight() / (mapValues.length + 1)));
                }
                tlTableMap.addView(tableRowT);
            }
            //creacion de los Buttons de cada columna
            for (int j = 0; j < mapValues[i].length; j++) {
                if (j == 0) {
                    TextView tv = new TextView(GameScreen.this);
                    tv.setText(String.valueOf(i));
                    tv.setTextSize(textSize);
                    tv.setGravity(Gravity.CENTER);
                    tableRow.addView(tv, new TableRow.LayoutParams(tlTableMap.getWidth() / mapValues[0].length,
                            tlTableMap.getHeight() / (mapValues.length + 1)));
                }
                EditText et = new EditText(GameScreen.this);
                board[i][j] = et;
                et.setFocusable(false);
                et.setBackground(getDrawable(android.R.color.transparent));
                et.setTextSize(textSize);
                et.setBackgroundColor(colors.get(codes.indexOf(mapValues[i][j])));
                et.setGravity(Gravity.CENTER);
                et.setOnClickListener(buttonActions);
                tableRow.addView(et, new TableRow.LayoutParams(tlTableMap.getWidth() / mapValues[0].length,
                        tlTableMap.getHeight() / (mapValues.length + 1)));
            }
            tlTableMap.addView(tableRow);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    private boolean isGameFinish() {
        return actualX == finalX && actualY == finalY;
    }

    private class ButtonActions implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabDown) {
                if ((actualY + 1) < mapValues.length) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY + 1][actualX]))) {
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[++actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        if (isGameFinish()) {
                            new SimpleOkDialog(GameScreen.this,
                                    getString(R.string.game_over),
                                    getString(R.string.you_finish_game)).build().show();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (v == fabLeft) {
                if ((actualX - 1) >= 0) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY][actualX - 1]))) {
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[actualY][--actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        if (isGameFinish()) {
                            new SimpleOkDialog(GameScreen.this,
                                    getString(R.string.game_over),
                                    getString(R.string.you_finish_game)).build().show();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (v == fabRight) {
                if ((actualX + 1) < mapValues[0].length) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY][actualX + 1]))) {
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[actualY][++actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        if (isGameFinish()) {
                            new SimpleOkDialog(GameScreen.this,
                                    getString(R.string.game_over),
                                    getString(R.string.you_finish_game)).build().show();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }

            } else if (v == fabUp) {
                if ((actualY - 1) >= 0) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY - 1][actualX]))) {
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[--actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        if (isGameFinish()) {
                            new SimpleOkDialog(GameScreen.this,
                                    getString(R.string.game_over),
                                    getString(R.string.you_finish_game)).build().show();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                new SimpleOkDialog(GameScreen.this,
                        getString(R.string.field_information_game_screen),
                        ((EditText) v).getText().toString()
                ).build().show();
            }
        }
    }
}

package com.example.sulemaia.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Helper.Parser;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.sulemaia.Helper.Constants.characterIcons;
import static com.example.sulemaia.Helper.Constants.mapValues;

public class GameScreen extends AppCompatActivity {

    private ArrayList<String> biomes = new ArrayList<>();
    private String contentFile;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private ArrayList<EditText> etLands = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY;
    private CharacterItem character;
    private TableLayout tlTableMap;
    private FloatingActionButton fabUp, fabLeft, fabDown, fabRight;
    private EditText board[][], lastVisited;
    private ButtonActions buttonActions;
    private Drawable characterIcon;
    float textSize = 8;

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
        lastVisited = null;
        actualX = initialX;
        actualY = initialY;
        Bitmap bitmap = ((BitmapDrawable) characterIcons[character.getIcon()]).getBitmap();
        characterIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int)textSize*3, (int)textSize*3, true));
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);

    }

    private void createTable(int[][] mapValues) {
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
                et.setText(String.valueOf(mapValues[i][j]));
                et.setTextSize(textSize);
                et.setBackgroundColor(colors.get(codes.indexOf(mapValues[i][j])));
                et.setGravity(Gravity.CENTER);
                //et.setOnClickListener(buttonActions);
                tableRow.addView(et, new TableRow.LayoutParams(tlTableMap.getWidth() / mapValues[0].length,
                        tlTableMap.getHeight() / (mapValues.length + 1)));
            }
            tlTableMap.addView(tableRow);
        }

    }

    private class ButtonActions implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v == fabDown){
                if ((actualY+1) < mapValues.length) {
                    board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    board[++actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                }
            }else if (v == fabLeft){
                if ((actualX-1) >= 0) {
                    board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    board[actualY][--actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                }
            }else if (v == fabRight){
                if ((actualX+1) < mapValues[0].length) {
                    board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    board[actualY][++actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                }

            }else if (v == fabUp){
                if ((actualY-1) >= 0) {
                    board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    board[--actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                }
            }
        }
    }
}

package com.example.sulemaia.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Helper.Parser;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {

    private ArrayList<String> biomes = new ArrayList<>();
    private String contentFile;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private ArrayList<EditText> etLands = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, rows, columns;
    private CharacterItem character;
    private TableLayout tlTableMap;
    private FloatingActionButton fabUp, fabLeft, fabDown, fabRight;
    private ButtonActions buttonActions;

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
        /*fabUp = findViewById(R.id.fab_game_up);
        fabDown = findViewById(R.id.fab_game_down);
        fabLeft = findViewById(R.id.fab_game_left);
        fabRight = findViewById(R.id.fab_game_right);
*/
        buttonActions = new ButtonActions();

        createTable(Parser.getFileArray(contentFile));


    }

    private void createTable(int[][] mapValues) {
        //TODO: ver por que carajos esto no jala, llevo muchas horas :c
        //creacion de las filas
        rows = mapValues.length;
        columns = mapValues[0].length;
        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(GameScreen.this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER);
            //creacion de los Buttons de cada columna
            for (int j = 0; j < columns; j++) {
                Button btn = new Button(GameScreen.this);
                btn.setText(String.valueOf(mapValues[i][j]));
                btn.setTextSize(10f);
                btn.setGravity(Gravity.CENTER);
                btn.setBackgroundColor(Color.BLUE);
                /* EditText et = new EditText(context);
                et.setHint("test");
                et.setText("");
                et.setText(String.valueOf(mapValues[i][j]));
                int color = colors.get(codes.indexOf(mapValues[i][j]));
                et.setBackgroundColor(Color.BLUE);
                et.setText("test");
                et.setTextSize(10f);
                et.setGravity(Gravity.CENTER);
                et.setOnClickListener(buttonActions);
                etLands.add(et);
                */
                tableRow.addView(btn, new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, tlTableMap.getHeight() / mapValues.length
                ));
            }
            tlTableMap.addView(tableRow);
        }
    }

    private class ButtonActions implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(GameScreen.this, "clicked", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.sulemaia.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.sulemaia.Model.PathTree;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.sulemaia.Helper.Constants.characterIcons;

public class GameScreen extends AppCompatActivity {

    float textSize;
    private ArrayList<String> biomes = new ArrayList<>();
    private String contentFile;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private ArrayList<EditText> etLands = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY, actualStep = 1, mapValues[][];
    private CharacterItem character;
    private TableLayout tlTableMap;
    private FloatingActionButton fabUp, fabLeft, fabDown, fabRight;
    private EditText board[][];
    private ButtonActions buttonActions;
    private Drawable characterIcon;
    private PathTree tree;

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
                initTreeAndColors();
            }
        });
    }

    private void initTreeAndColors() {
        tree = new PathTree(new PathTree.Node(
                board[actualY][actualX].getTag().toString(),
                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                String.valueOf(actualStep))
        );
        setFieldsColors(actualY, actualX);
        if((actualX - 1) >= 0) {
            expandLeft(false);
        }
        if((actualY - 1) >= 0) {
            expandUp(false);
        }
        if((actualX + 1) < mapValues[0].length) {
            expandRight(false);
        }
        if((actualY + 1) < mapValues.length) {
            expandDown(false);
        }
    }

    private void setFieldsColors(int y, int x) {
        //middle
        setFieldColor(y, x, colors.get(codes.indexOf(mapValues[y][x])));
        //up
        if (y - 1 >= 0) {
            setFieldColor(y - 1, x, colors.get(codes.indexOf(mapValues[y - 1][x])));
        }
        //down
        if (y + 1 < board.length) {
            setFieldColor(y + 1, x, colors.get(codes.indexOf(mapValues[y + 1][x])));
        }
        //left
        if (x - 1 >= 0) {
            setFieldColor(y, x - 1, colors.get(codes.indexOf(mapValues[y][x - 1])));
        }
        //right
        if (x + 1 < board[0].length) {
            setFieldColor(y, x + 1, colors.get(codes.indexOf(mapValues[y][x + 1])));
        }
    }

    private void setFieldColor(int y, int x, int color) {
        board[y][x].setBackgroundColor(color);
    }

    private void loadBoard() {
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START_GAME_SCREEN, true);
        actualX = initialX;
        actualY = initialY;
        Bitmap bitmap = ((BitmapDrawable) characterIcons[character.getIcon()]).getBitmap();
        characterIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) textSize * 3, (int) textSize * 3, true));
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
        board[actualY][actualX].setText("I. " + ", " + actualStep);
        board[actualY][actualX].setTextColor(Color.WHITE);
        board[finalY][finalX].setText("F. ");
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
        textSize = (mapValues[0].length > mapValues.length) ?
                Parser.getTextSizeForMap(mapValues[0].length) : Parser.getTextSizeForMap(mapValues.length);
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
                    tv.setText(String.valueOf(i + 1));
                    tv.setTextSize(textSize);
                    tv.setGravity(Gravity.CENTER);
                    tableRow.addView(tv, new TableRow.LayoutParams(tlTableMap.getWidth() / mapValues[0].length,
                            tlTableMap.getHeight() / (mapValues.length + 1)));
                }
                EditText et = new EditText(GameScreen.this);
                et.setTag("" + (i + 1) + ", " + Parser.getLetterForInt(j + 1));
                board[i][j] = et;
                et.setFocusable(false);
                et.setBackground(getDrawable(android.R.color.transparent));
                et.setTextSize(textSize);
                //et.setBackgroundColor(colors.get(codes.indexOf(mapValues[i][j])));
                et.setBackgroundColor(Constants.BLACK_COLOR);
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

    private void finishGame() {
        tree.endTree();
        new SimpleOkDialog(GameScreen.this,
                getString(R.string.game_over),
                getString(R.string.you_finish_game)).build().show();
        String urlTree ="https://dreampuf.github.io/GraphvizOnline/#" +
                tree.getDotTree().replace("\n", "%0A");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTree));
        startActivity(browserIntent);
    }

    public void expandDown(boolean isCharacterMoving){
        tree.addDown(new PathTree.Node(
                board[actualY+1][actualX].getTag().toString(),
                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                String.valueOf(actualStep)), isCharacterMoving);
    }

    public void expandUp(boolean isCharacterMoving){
        tree.addUp(new PathTree.Node(
                board[actualY-1][actualX].getTag().toString(),
                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                String.valueOf(actualStep)), isCharacterMoving);
    }

    public void expandLeft(boolean isCharacterMoving){
        tree.addLeft(new PathTree.Node(
                board[actualY][actualX-1].getTag().toString(),
                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                String.valueOf(actualStep)), isCharacterMoving);
    }

    public void expandRight(boolean isCharacterMoving){
        tree.addRight(new PathTree.Node(
                board[actualY][actualX+1].getTag().toString(),
                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                String.valueOf(actualStep)), isCharacterMoving);
    }

    private class ButtonActions implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == fabDown) {
                //down
                if ((actualY + 1) < mapValues.length) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY + 1][actualX]))) {
                        if((actualX - 1) >= 0) {
                            expandLeft(false);
                        }
                        if((actualY - 1) >= 0) {
                            expandUp(false);
                        }
                        if((actualX + 1) < mapValues[0].length) {
                            expandRight(false);
                        }
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[++actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        setFieldsColors(actualY, actualX);
                        //addDown(true);
                        tree.addDown(new PathTree.Node(
                                board[actualY][actualX].getTag().toString(),
                                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                                String.valueOf(actualStep)), true);
                        /////////////////////////////////////////////////////
                        if((actualX - 1) >= 0) {
                            expandLeft(false);
                        }
                        if((actualX + 1) < mapValues[0].length) {
                            expandRight(false);
                        }
                        if((actualY + 1) < mapValues.length){
                            expandDown(false);
                        }
                        /////////////////////////////////////////////////////
                        if (isGameFinish()) {
                            finishGame();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (v == fabLeft) {
                //left
                if ((actualX - 1) >= 0) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY][actualX - 1]))) {
                        if((actualY - 1) >= 0) {
                            expandUp(false);
                        }
                        if((actualX + 1) < mapValues[0].length) {
                            expandRight(false);
                        }
                        if((actualY + 1) < mapValues.length) {
                            expandDown(false);
                        }
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[actualY][--actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        setFieldsColors(actualY, actualX);
                        //addLeft(true);
                        tree.addLeft(new PathTree.Node(
                                board[actualY][actualX].getTag().toString(),
                                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                                String.valueOf(actualStep)), true);
                        ///////////////////////////////////////////////////////////
                        if((actualY - 1) >= 0) {
                            expandUp(false);
                        }
                        if((actualX - 1) >= 0){
                            expandLeft(false);
                        }
                        if((actualY + 1) < mapValues.length) {
                            expandDown(false);
                        }
                        ///////////////////////////////////////////////////////////
                        if (isGameFinish()) {
                            finishGame();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (v == fabRight) {
                //right
                if ((actualX + 1) < mapValues[0].length) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY][actualX + 1]))) {
                        if((actualX - 1) >= 0) {
                            expandLeft(false);
                        }
                        if((actualY - 1) >= 0) {
                            expandUp(false);
                        }
                        if((actualY + 1) < mapValues.length) {
                            expandDown(false);
                        }
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[actualY][++actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        setFieldsColors(actualY, actualX);
                        //addRight(true);
                        tree.addRight(new PathTree.Node(
                                board[actualY][actualX].getTag().toString(),
                                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                                String.valueOf(actualStep)), true);
                        //////////////////////////////////////////////////////////
                        if((actualX + 1) < mapValues[0].length){
                            expandRight(false);
                        }
                        if((actualY - 1) >= 0) {
                            expandUp(false);
                        }
                        if((actualY + 1) < mapValues.length) {
                            expandDown(false);
                        }
                        //////////////////////////////////////////////////////////
                        if (isGameFinish()) {
                            finishGame();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }

            } else if (v == fabUp) {
                //up
                if ((actualY - 1) >= 0) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY - 1][actualX]))) {
                        if((actualX - 1) >= 0) {
                            expandLeft(false);
                        }
                        if((actualX + 1) < mapValues[0].length) {
                            expandRight(false);
                        }
                        if((actualY + 1) < mapValues.length) {
                            expandDown(false);
                        }
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[--actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        setFieldsColors(actualY, actualX);
                        //addUp(true);
                        tree.addUp(new PathTree.Node(
                                board[actualY][actualX].getTag().toString(),
                                character.getLandsCosts().get(codes.indexOf(mapValues[actualY][actualX])),
                                String.valueOf(actualStep)), true);
                        ///////////////////////////////////////////////////////////
                        if((actualX - 1) >= 0) {
                            expandLeft(false);
                        }
                        if((actualY - 1) >= 0){
                            expandUp(false);
                        }
                        if((actualX + 1) < mapValues[0].length) {
                            expandRight(false);
                        }
                        ///////////////////////////////////////////////////////////
                        if (isGameFinish()) {
                            finishGame();
                        }
                    } else {
                        Toast.makeText(GameScreen.this, getString(R.string.cant_go_to_field), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                new SimpleOkDialog(GameScreen.this,
                        getString(R.string.field_information_game_screen) + "\n" +
                                v.getTag().toString(),
                        ((EditText) v).getText().toString()
                ).build().show();
            }
        }
    }
}

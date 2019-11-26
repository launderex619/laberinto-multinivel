package com.example.sulemaia.Activity;

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
import com.example.sulemaia.Model.PathManualTree;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.sulemaia.Helper.Constants.characterIcons;

/**
 * Class with all the graphic implementations of the screen
 * in which the user will actually play.
 */
public class GameScreen extends AppCompatActivity {

    float textSize;
    private ArrayList<String> biomes = new ArrayList<>();
    private String contentFile;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY, actualStep = 1, mapValues[][];
    private CharacterItem character;
    private TableLayout tlTableMap;
    private FloatingActionButton fabUp, fabLeft, fabDown, fabRight;
    private EditText board[][];
    private PathManualTree.Node nodes[][];
    private ButtonActions buttonActions;
    private Drawable characterIcon;
    private PathManualTree tree;
    private enum MOVEMENT_CODES {DOWN, UP, LEFT, RIGHT, NONE};

    /**
     * Initialization of all the basic data that the game and board will need to be playable,
     * like biomes, colors, codes, coordinates, and buttons.
     * @param savedInstanceState Bundle with the Strings from the previous intent.
     */
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
            /**
             * First initialization of the board.
             * We load the txt file with the parser, load the board,
             * and initiate the fist step of the manual tree.
             */
            @Override
            public void run() {
                createTable(Parser.getFileArray(contentFile));
                loadBoard();
                initTreeAndColors();
            }
        });
    }

    /**
     * We create the graphic tree with the first node (which is obviously the
     * initial coordinate of the character), process the invalid nodes, and paint
     * the adjacent tiles with the corresponding color.
     */
    private void initTreeAndColors() {
        nodes[actualY][actualX].setStep(actualStep);
        tree = new PathManualTree(nodes[actualY][actualX]);
        tree.setInitial(nodes[actualY][actualX]);
        for (int i = 0; i < mapValues.length; i++) {
            for (int j = 0; j < mapValues[0].length; j++) {
                if (!character.getCanPass().get(codes.indexOf(mapValues[i][j]))) {
                    tree.addNodeToInvalidNodes(nodes[i][j]);
                }
            }
        }
        setAdyacentFieldsAndColor(actualY, actualX);
    }

    /**
     * We receive a coordinate and paint the adjacent fields with their corresponding color.
     * @param y y coordinate.
     * @param x x coordinate.
     */
    private void setAdyacentFieldsAndColor(int y, int x) {
        //middle
        setFieldColor(y, x, colors.get(codes.indexOf(mapValues[y][x])));
        //up
        if (y - 1 >= 0) {
            setFieldColor(y - 1, x, colors.get(codes.indexOf(mapValues[y - 1][x])));
            tree.addNode(nodes[y][x], nodes[y-1][x]);
        }
        //down
        if (y + 1 < board.length) {
            setFieldColor(y + 1, x, colors.get(codes.indexOf(mapValues[y + 1][x])));
            tree.addNode(nodes[y][x], nodes[y+1][x]);
        }
        //left
        if (x - 1 >= 0) {
            setFieldColor(y, x - 1, colors.get(codes.indexOf(mapValues[y][x - 1])));
            tree.addNode(nodes[y][x], nodes[y][x-1]);
        }
        //right
        if (x + 1 < board[0].length) {
            setFieldColor(y, x + 1, colors.get(codes.indexOf(mapValues[y][x + 1])));
            tree.addNode(nodes[y][x], nodes[y][x+1]);
        }
    }

    /**
     * We set the corresponding color of a specific tile (actually the one in which the
     * character is right now).
     * @param y y coordinate.
     * @param x x coordinate
     * @param color color code of the tile.
     */
    private void setFieldColor(int y, int x, int color) {
        board[y][x].setBackgroundColor(color);
    }

    /**
     * We load the board with the essential, initial and most important
     * information, like the initial and final tile, information about
     * any tile the user taps, and show the tutoria if its the fist
     * time executing the app.
     */
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

    /**
     * We create the whole map with buttons so later on you can tap on each tile
     * and get important information about it. Also, this creates the letters and
     * numbers on the first column and first row.
     * @param mapValues the whole compilation of tiles for the map.
     */
    private void createTable(int[][] mapValues) {
        textSize = (mapValues[0].length > mapValues.length) ?
                Parser.getTextSizeForMap(mapValues[0].length) : Parser.getTextSizeForMap(mapValues.length);
        this.mapValues = mapValues;
        board = new EditText[mapValues.length][mapValues[0].length];
        nodes = new PathManualTree.Node[mapValues.length][mapValues[0].length];

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
                et.setTag("" + Parser.getLetterForInt(j + 1) + ", " + (i + 1));
                board[i][j] = et;
                nodes[i][j] = new PathManualTree.Node(et.getTag().toString(),
                        character.getLandsCosts().get(codes.indexOf(mapValues[i][j])),
                        character.getCanPass().get(codes.indexOf(mapValues[i][j])));
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

    /**
     * Method called by system on Back button pressed.
     * @param item menu item corresponding to the activity.
     * @return a true value.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    /**
     * We check if the actual coordinate in which the character is right now,
     * is the same as the final tile, so the game would be finished.
     * @return a true or false value.
     */
    private boolean isGameFinish() {
        return actualX == finalX && actualY == finalY;
    }

    /**
     * When the game is finished, we deploy a message, and generate the url
     * that creates the graphic tree on the git repository.
     */
    private void finishGame() {
        tree.setFinal(nodes[actualY][actualX]);
        tree.endTree();
        new SimpleOkDialog(GameScreen.this,
                getString(R.string.game_over),
                getString(R.string.you_finish_game)).build().show();
        String urlTree = "https://dreampuf.github.io/GraphvizOnline/#" +
                tree.getDotTree().replace("\n", "%0A");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTree));
        startActivity(browserIntent);
    }

    /**
     * Class that implements the functionality of the buttons in this activity.
     */
    private class ButtonActions implements View.OnClickListener {
        /**
         * Depending on the button pressed (up, down, left, right), we have to check
         * if there is any map terrain in that coordinate (if the character is not
         * already on the bounds of the map), and also if the character can go through
         * that specific terrain. If this is true, then we move the icon, update the
         * coordinates and the actual step, add movement to the graphic tree, and check
         * if the game has finished.
         * @param v the button view pressed.
         */
        @Override
        public void onClick(View v) {
            if (v == fabDown) {
                //down
                if ((actualY + 1) < mapValues.length) {
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY + 1][actualX]))) {
                        setAdyacentFieldsAndColor(actualY+1, actualX);
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[++actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        nodes[actualY][actualX].setStep(actualStep);
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        tree.addMovement(nodes[actualY][actualX]);
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
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY][actualX-1]))) {
                        setAdyacentFieldsAndColor(actualY, actualX-1);
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[actualY][--actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        nodes[actualY][actualX].setStep(actualStep);
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        tree.addMovement(nodes[actualY][actualX]);
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
                    if (character.getCanPass().get(codes.indexOf(mapValues[actualY][actualX+1]))) {
                        setAdyacentFieldsAndColor(actualY, actualX+1);
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[actualY][++actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        nodes[actualY][actualX].setStep(actualStep);
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        tree.addMovement(nodes[actualY][actualX]);
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
                        setAdyacentFieldsAndColor(actualY-1, actualX);
                        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        board[--actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
                        actualStep++;
                        nodes[actualY][actualX].setStep(actualStep);
                        board[actualY][actualX].setText(board[actualY][actualX].getText() + "," + actualStep);
                        tree.addMovement(nodes[actualY][actualX]);
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

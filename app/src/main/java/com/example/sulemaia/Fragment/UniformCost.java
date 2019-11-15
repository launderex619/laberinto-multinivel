package com.example.sulemaia.Fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemaia.Activity.GameScreen;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class UniformCost extends Fragment {

    float textSize;
    private ArrayList<String> biomes = new ArrayList<>();
    private String contentFile;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY, actualStep = 1, mapValues[][];
    private CharacterItem character;
    private TableLayout tlTableMap;
    private EditText board[][];
    private PathTree.Node nodes[][];
    private ButtonActions buttonActions;
    private Drawable characterIcon;
    private PathTree tree;

    public UniformCost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_uniform_cost, container, false);
        Bundle bundle = getArguments();
        biomes.addAll(bundle.getStringArrayList("biomes"));
        contentFile = bundle.getString("contentFile");
        colors.addAll(bundle.getIntegerArrayList("colors"));
        codes.addAll(bundle.getIntegerArrayList("codes"));
        initialX = bundle.getInt("initialX", 0);
        finalX = bundle.getInt("finalX", 0);
        initialY = bundle.getInt("initialY", 0);
        finalY = bundle.getInt("finalY", 0);
        character = (CharacterItem) bundle.getSerializable("character");

        tlTableMap = view.findViewById(R.id.tl_game_table_map);

        buttonActions = new ButtonActions();
        tlTableMap.post(new Runnable() {
            @Override
            public void run() {
                createTable(Parser.getFileArray(contentFile));
                loadBoard();
                initTreeAndColors();
            }
        });
        return view;
    }


    private void initTreeAndColors() {
        nodes[actualY][actualX].setStep(actualStep);
        tree = new PathTree(nodes[actualY][actualX]);
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

    private void setFieldColor(int y, int x, int color) {
        board[y][x].setBackgroundColor(color);
    }

    private void loadBoard() {
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(getContext())
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
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                    .putBoolean(Constants.PREF_KEY_FIRST_START_GAME_SCREEN, false)
                    .apply();

            TapTargetView.showFor(getActivity(),
                    new TapTargetHelper(getActivity(),
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
        nodes = new PathTree.Node[mapValues.length][mapValues[0].length];

        for (int i = 0; i < mapValues.length; i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setGravity(Gravity.CENTER);
            if (i == 0) {
                TableRow tableRowT = new TableRow(getContext());
                tableRowT.setGravity(Gravity.CENTER);
                for (int j = 0; j <= mapValues[i].length; j++) {
                    TextView tv = new TextView(getContext());
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
                    TextView tv = new TextView(getContext());
                    tv.setText(String.valueOf(i + 1));
                    tv.setTextSize(textSize);
                    tv.setGravity(Gravity.CENTER);
                    tableRow.addView(tv, new TableRow.LayoutParams(tlTableMap.getWidth() / mapValues[0].length,
                            tlTableMap.getHeight() / (mapValues.length + 1)));
                }
                EditText et = new EditText(getContext());
                et.setTag("" + (i + 1) + ", " + Parser.getLetterForInt(j + 1));
                board[i][j] = et;
                nodes[i][j] = new PathTree.Node(et.getTag().toString(),
                        character.getLandsCosts().get(codes.indexOf(mapValues[i][j])),
                        character.getCanPass().get(codes.indexOf(mapValues[i][j])));
                et.setFocusable(false);
                et.setBackground(getActivity().getDrawable(android.R.color.transparent));
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
    private class ButtonActions implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new SimpleOkDialog(getContext(),
                    getString(R.string.field_information_game_screen) + "\n" +
                            v.getTag().toString(),
                    ((EditText) v).getText().toString()
            ).build().show();
        }
    }
}

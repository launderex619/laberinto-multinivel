package com.example.sulemaia.Fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import top.defaults.colorpicker.ColorPickerPopup;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemaia.Dialog.SimpleOkDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Helper.Parser;
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.Model.HeuristicPathTree;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import static com.example.sulemaia.Helper.Constants.characterIcons;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstBest extends Fragment {
    private static final String DOWN = "d", UP = "u", LEFT = "l", RIGHT = "r";
    private static final int MANHATTAN = 0;
    private static final int EUCLIDIANA = 1;

    float textSize;
    private ArrayList<String> biomes = new ArrayList<>();
    private ArrayList<String> expansionOrder = new ArrayList<>();
    private String contentFile, updateTimeText;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY, actualStep = 1, mapValues[][],
            measureMode = 0;
    private long updateTime = 0;
    private CharacterItem character;
    private TableLayout tlTableMap;
    private EditText board[][];
    private HeuristicPathTree.Node nodes[][];
    private FirstBest.ButtonActions buttonActions;
    private Drawable characterIcon;
    private HeuristicPathTree tree;
    private Button btnStartAlgorithm;
    private TextView tvRefreshRate;
    private SeekBar sbRefreshBar;
    private FirstBest.ResolverFirstBestThread resolverFirstBestThread;
    private Spinner spMeasureType;
    private int pathColor = 0xFFFF0000;

    public FirstBest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_first_best, container, false);
        Bundle bundle = getArguments();
        biomes.addAll(bundle.getStringArrayList("biomes"));
        expansionOrder.addAll(bundle.getStringArrayList("expansionOrder"));
        contentFile = bundle.getString("contentFile");
        colors.addAll(bundle.getIntegerArrayList("colors"));
        codes.addAll(bundle.getIntegerArrayList("codes"));
        initialX = bundle.getInt("initialX", 0);
        finalX = bundle.getInt("finalX", 0);
        initialY = bundle.getInt("initialY", 0);
        finalY = bundle.getInt("finalY", 0);
        character = (CharacterItem) bundle.getSerializable("character");
        btnStartAlgorithm = view.findViewById(R.id.btn_start_algorithm);
        tvRefreshRate = view.findViewById(R.id.tv_refresh_rate);
        sbRefreshBar = view.findViewById(R.id.sb_refresh_bar);
        tlTableMap = view.findViewById(R.id.tl_game_table_map);
        spMeasureType = view.findViewById(R.id.sp_measure_type);
        updateTimeText = tvRefreshRate.getText().toString();
        tvRefreshRate.setText(updateTimeText + ": " + (2 + updateTime) + "ms");
        buttonActions = new FirstBest.ButtonActions();
        btnStartAlgorithm.setOnClickListener(buttonActions);
        sbRefreshBar.setOnSeekBarChangeListener(buttonActions);


        String[] curveOrder = {"Manhattan", "Euclidiana"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                curveOrder);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMeasureType.setAdapter(spnAdapter);
        spMeasureType.setOnItemSelectedListener(new FirstBest.Measures());

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
        tree = new HeuristicPathTree(nodes[actualY][actualX]);
        tree.setInitial(nodes[actualY][actualX]);
    }

    private void setColorToField(int y, int x) {
        setFieldColor(y, x, colors.get(codes.indexOf(mapValues[y][x])));
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
        nodes = new HeuristicPathTree.Node[mapValues.length][mapValues[0].length];

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
                et.setTag("" + Parser.getLetterForInt(j + 1) + ", " + (i + 1));
                board[i][j] = et;
                nodes[i][j] = new HeuristicPathTree.Node(et.getTag().toString(),
                        character.getLandsCosts().get(codes.indexOf(mapValues[i][j])),
                        character.getCanPass().get(codes.indexOf(mapValues[i][j])),
                        i,
                        j);
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

    private class ButtonActions implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        @Override
        public void onClick(View v) {
            if (v == btnStartAlgorithm) {
                resolverFirstBestThread = new FirstBest.ResolverFirstBestThread();
                resolverFirstBestThread.execute();
                resolverFirstBestThread.onPostExecute(null);
            } else {
                new SimpleOkDialog(getContext(),
                        getString(R.string.field_information_game_screen) + "\n" +
                                v.getTag().toString(),
                        ((EditText) v).getText().toString()
                ).build().show();
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateTime = (2 + progress) * (progress);
            tvRefreshRate.setText(updateTimeText + ": " + updateTime + "ms");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private void showFailureMessage() {
        (new SimpleOkDialog(getContext(), getString(R.string.error),
                getString(R.string.path_not_found))).build();
    }
    private void drawPath() {
        HeuristicPathTree.Node path = nodes[finalY][finalX];

        new ColorPickerPopup.Builder(getContext())
                .initialColor(Color.RED) // Set initial color
                .enableBrightness(true) // Enable brightness slider or not
                .enableAlpha(false) // Enable alpha slider or not
                .okTitle("Aceptar")
                .cancelTitle("Cancelar")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        pathColor = color;
                    }
                });
        while (path != nodes[initialY][initialY]){
            board[path.getPosY()][path.getPosX()].setBackgroundColor(pathColor);
        }
    }

    private void getNodesNextStep(
            int y, int x, ArrayList<HeuristicPathTree.Node> expandedNodes, HashSet<HeuristicPathTree.Node> visitedNodes) {
        //middle
        //PriorityQueue<HeuristicPathTree.Node> nodes = new PriorityQueue<>(4);
        for (String direction : expansionOrder) {
            HeuristicPathTree.Node response = expandInDirection(direction, y, x, visitedNodes);
            if (response != null) {
                if (!expandedNodes.contains(response)) {
                    expandedNodes.add(response);
                }
            }
        }
        Collections.sort(expandedNodes, new Comparator<HeuristicPathTree.Node>() {
            @Override
            public int compare(HeuristicPathTree.Node o1, HeuristicPathTree.Node o2) {
                return Float.compare(o1.getCost(), o2.getCost());
            }
        });
        //return nodes;
    }

    private HeuristicPathTree.Node expandInDirection(String direction, int y, int x,
                                                     HashSet<HeuristicPathTree.Node> visitedNodes) {
        switch (direction) {
            case UP:
                //up
                if (y - 1 >= 0) {
                    setColorToField(y - 1, x);
                    if (visitedNodes.contains(nodes[y - 1][x])) {
                        return null;
                    }
                    if (!nodes[y - 1][x].isAccessible()) {
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y - 1][x]);
                    return nodes[y - 1][x];
                }
                break;
            case DOWN:
                //down
                if (y + 1 < board.length) {
                    setColorToField(y + 1, x);
                    if (visitedNodes.contains(nodes[y + 1][x])) {
                        return null;
                    }
                    if (!nodes[y + 1][x].isAccessible()) {
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y + 1][x]);
                    return nodes[y + 1][x];
                }
                break;
            case LEFT:
                //left
                if (x - 1 >= 0) {
                    setColorToField(y, x - 1);
                    if (visitedNodes.contains(nodes[y][x - 1])) {
                        return null;
                    }
                    if (!nodes[y][x - 1].isAccessible()) {
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y][x - 1]);
                    return nodes[y][x - 1];
                }
                break;
            case RIGHT:
                //right
                if (x + 1 < board[0].length) {
                    setColorToField(y, x + 1);
                    if (visitedNodes.contains(nodes[y][x + 1])) {
                        return null;
                    }
                    if (!nodes[y][x + 1].isAccessible()) {
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y][x + 1]);
                    return nodes[y][x + 1];
                }
                break;
        }
        return null;
    }

    private class ResolverFirstBestThread extends AsyncTask<Void, HeuristicPathTree.Node, Void> {
        private ArrayList<HeuristicPathTree.Node> expandedNodes;
        private HashSet<HeuristicPathTree.Node> visitedNodes;
        private boolean isPathFounded;

        ResolverFirstBestThread() {
//            expandedNodes = new PriorityQueue<>(
//                    225, //225 because max size is 15 rows times 15 columns so 15*15=225
//                    new Comparator<HeuristicPathTree.Node>() {
//                        @Override
//                        public int compare(HeuristicPathTree.Node o1, HeuristicPathTree.Node o2) {
//                            return Float.compare(o2.getCost(), o1.getCost());
//                        }
//                    });
            expandedNodes = new ArrayList<>(225);
            visitedNodes = new HashSet<>(225);
            isPathFounded = false;
        }

        @Override
        protected void onPreExecute() {
            //code for pre execute the thread
            setColorToField(initialY, initialX);
            //expandedNodes.addAll(getNodesNextStep(initialY, initialX, expandedNodes, visitedNodes));.
            getNodesNextStep(initialY, initialX, expandedNodes, visitedNodes);
            //expandedNodes.
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // mientras la cola no este vacia
            while (expandedNodes.size() > 0) {
                HeuristicPathTree.Node actualNode = expandedNodes.remove(0);
                //evaluamos el nodo
                if (actualNode == nodes[finalY][finalX]) {
                    // do stuff to end the thread, we reach the final node
                    isPathFounded = true;
                    publishProgress(actualNode);
                    break;
                }
                //lo agregamos al set de visitados
                visitedNodes.add(actualNode);
                //agregamos a los hijos
                //expandedNodes.addAll(getNodesNextStep(actualNode.getPosY(), actualNode.getPosX(), expandedNodes, visitedNodes));.
                publishProgress(actualNode);
                getNodesNextStep(actualNode.getPosY(), actualNode.getPosX(), expandedNodes, visitedNodes);
                try {
                    Thread.sleep(updateTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(HeuristicPathTree.Node... values) {
            moveCharacter(values[0].getPosY(), values[0].getPosX());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            drawPath();
        }
    }

    private void moveCharacter(int posY, int posX) {
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        actualY = posY;
        actualX = posX;
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
    }


    private class Measures implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            measureMode = position;
            Toast.makeText(getContext(), "measure mode: " + measureMode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
package com.example.sulemaia.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.sulemaia.Interface.iUniformCost;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.Thread.ResolverUniformCostThread;
import com.example.sulemaia.Model.HeuristicPathTree;
import com.example.sulemaia.Model.PathManualTree;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import top.defaults.colorpicker.ColorPickerPopup;

import static com.example.sulemaia.Helper.Constants.characterIcons;

/**
 * A simple {@link Fragment} subclass.
 */
public class UniformCost extends Fragment implements iUniformCost {
    private static final String DOWN = "d", UP = "u", LEFT = "l", RIGHT = "r";
    float textSize;
    private ArrayList<String> biomes = new ArrayList<>();
    private ArrayList<String> expansionOrder = new ArrayList<>();
    private String contentFile, updateTimeText;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private int initialX, finalX, initialY, finalY, actualX, actualY, actualStep = 1, mapValues[][];
    private long updateTime = 0;
    private CharacterItem character;
    private TableLayout tlTableMap;
    private EditText board[][];
    private HeuristicPathTree.Node nodes[][];
    private UniformCost.ButtonActions buttonActions;
    private Drawable characterIcon;
    private HeuristicPathTree tree;
    private Button btnStartAlgorithm;
    private TextView tvRefreshRate;
    private SeekBar sbRefreshBar;
    private int pathColor = 0xFFFF0000;
    private ResolverUniformCostThread thread;

    /**
     * Empty constructor.
     */
    public UniformCost() {
        // Required empty public constructor
    }

    /**
     * On create view we load all the importan information from the bundle, like the biomes,
     * codes, colors, relevant coordinates, etc...
     * @param inflater Layout.
     * @param container view group.
     * @param savedInstanceState Strings bundle for the information load.
     * @return the view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_uniform_cost, container, false);
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
        updateTimeText = tvRefreshRate.getText().toString();
        tvRefreshRate.setText(updateTimeText + ": " + (2 + updateTime) + "ms");
        buttonActions = new UniformCost.ButtonActions();
        btnStartAlgorithm.setOnClickListener(buttonActions);
        sbRefreshBar.setOnSeekBarChangeListener(buttonActions);

        tlTableMap.post(new Runnable() {
            /**
             * To run the creation, creating the table and loading the board
             * with all the necessary information.
             */
            @Override
            public void run() {
                createTable(Parser.getFileArray(contentFile));
                loadBoard();
                //initTreeAndColors();
            }
        });
        return view;
    }

    /**
     * Set the color to a specific tile.
     * @param y Y coordinate.
     * @param x X coordinate.
     * @param color specific color.
     */
    private void setFieldColor(int y, int x, int color){
        board[y][x].setBackgroundColor(color);
    }

    /**
     * Load the board with the initial information, like initial and final coordinates, and
     * check if its the first time running the app so we can show the tutorial.
     */
    private void loadBoard() {
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(Constants.PREF_KEY_FIRST_START_UNIFORM_COST_SCREEN, true);
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
                    .putBoolean(Constants.PREF_KEY_FIRST_START_UNIFORM_COST_SCREEN, false)
                    .apply();
            new TapTargetSequence(getActivity()).targets(
                    new TapTargetHelper(getContext(),
                            board[actualY][actualX],
                            getString(R.string.game_field),
                            getString(R.string.game_field_description)).Create(),
                    new TapTargetHelper(getContext(),
                            sbRefreshBar,
                            getString(R.string.actualization_time),
                            getString(R.string.actualization_time_description)).Create(),
                    new TapTargetHelper(getContext(),
                            btnStartAlgorithm,
                            getString(R.string.start_algorithm),
                            getString(R.string.start_algorithm_game)).Create()
            ).start();
        }

    }

    /**
     * The creation of the table with each tile as a text view and letters and numbers
     * for the first column and row.
     * @param mapValues the map information to process in the creation of the table.
     */
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


    /**
     * Implementation of the actions for each button.
     */
    private class ButtonActions implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        /**
         * When we start the algorithm, we let the user select a color for the final path drawing.
         * @param v button view.
         */
        @Override
        public void onClick(View v) {
            if (v == btnStartAlgorithm) {
                ColorPickerPopup color = new ColorPickerPopup.Builder(getContext())
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(false) // Enable alpha slider or not
                        .okTitle("Aceptar")
                        .cancelTitle("Cancelar")
                        .showIndicator(true)
                        .showValue(true)
                        .build();
                color.show(new ColorPickerPopup.ColorPickerObserver() {
                    /**
                     * Once the color is picked, we proceed with the actual algorithm with
                     * the parameters that it needs.
                     * @param color of the final path.
                     */
                    @Override
                    public void onColorPicked(int color) {
                        pathColor = color;
                        thread = new ResolverUniformCostThread(nodes,
                                expansionOrder,
                                updateTime,
                                UniformCost.this);
                        thread.execute(initialY, initialX, finalY, finalX);
                    }
                });
                btnStartAlgorithm.setVisibility(View.GONE);
            } else {
                new SimpleOkDialog(getContext(),
                        getString(R.string.field_information_game_screen) + "\n" +
                                v.getTag().toString(),
                        ((EditText) v).getText().toString()
                ).build().show();
            }
        }

        /**
         * Change to the refresh of the process of the thread.
         * @param seekBar for the speed of execution.
         * @param progress for the completion of the thread.
         * @param fromUser for the actions selected.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateTime = (5 * progress);
            tvRefreshRate.setText(updateTimeText + ": " + updateTime + "ms");
        }

        /**
         * Code not used but still needed to be instantiated for the thread to work properly.
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * Code not used but still needed to be instantiated for the thread to work properly.
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * Method to show a failure message in case the algorithm is not able to reach the final tile.
     */
    @Override
    public synchronized void showFailureMessage(){
        (new SimpleOkDialog(getContext(), getString(R.string.error),
                getString(R.string.path_not_found))).build().show();
    }

    /**
     * Method to control the whole drawing of the path once the algorithm finishes.
     * Also, the user gets the constructor URL for the tree, and gets redirected to the git
     * page in which the actual tree gets drawn using DOT code.
     * @param heuristicPathTree the tree from which all the information is taken.
     */
    @Override
    public synchronized void drawPath(HeuristicPathTree heuristicPathTree){
        if(heuristicPathTree == null){
            return;
        }

        HeuristicPathTree.Node node = heuristicPathTree.getFinalNode();
        putNodesInfo(heuristicPathTree.getAnchor());
        board[node.getPosY()][node.getPosX()].setBackgroundColor(pathColor);
        while (node != heuristicPathTree.getAnchor()) {
            node = node.getFather();
            board[node.getPosY()][node.getPosX()].setBackgroundColor(pathColor);
        }
        new SimpleOkDialog(getContext(),
                getString(R.string.game_over),
                getString(R.string.you_finish_game)).build().show();
        String urlTree = "https://dreampuf.github.io/GraphvizOnline/#" +
                heuristicPathTree.getDotTree().replace("\n", "%0A");

        if(urlTree.length()/1024 < 1024){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTree));
            startActivity(browserIntent);
        }
        else {
            Toast.makeText(getContext(), "Hubo un error al abrir la pagina web, DEMASIADA INFORMACION. URL copiada al portapapeles", Toast.LENGTH_SHORT).show();
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", urlTree);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * Method to be able to show the weight of each node in the graphic tree.
     * @param node The node which we are modifying to show the weight.
     */
    private void putNodesInfo(HeuristicPathTree.Node node) {
        //using info for Uniform Cost, this will be different for each algorithm.
        String text = board[node.getPosY()][node.getPosX()].getText().toString() +
                ",\nStep: " + node.getStep() + "\n" +
                "Accumulated: (" + node.getAccumulative() + ")";
        board[node.getPosY()][node.getPosX()].setText(text);
        for (HeuristicPathTree.Node n : node.getChildren()){
            putNodesInfo(n);
        }
    }

    /**
     * For the update on the movement of the character, for logically and in the UI.
     * @param posY Y actual coordinate.
     * @param posX X actual coordinate.
     */
    @Override
    public synchronized void moveCharacter(int posY, int posX){
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        actualY = posY;
        actualX = posX;
        board[actualY][actualX].setCompoundDrawablesWithIntrinsicBounds(characterIcon, null, null, null);
    }

    /**
     * Help method to color tiles, according to a specific color code.
     * @param y Y coordinate.
     * @param x X coordinate.
     */
    @Override
    public synchronized void setColorToField(int y, int x){
        setFieldColor(y, x, colors.get(codes.indexOf(mapValues[y][x])));
    }

    /**
     * Mehtod to stop the thread once the algorithm finishes.
     */
    @Override
    public synchronized void onStop() {
        stopThread();
        super.onStop();
    }

    /**
     * Method called by onStop to stop the thread.
     */
    private void stopThread() {
        if (thread != null && (thread.getStatus() == AsyncTask.Status.RUNNING || thread.getStatus() == AsyncTask.Status.PENDING)) {
            thread.cancel(true);
        }
    }
}

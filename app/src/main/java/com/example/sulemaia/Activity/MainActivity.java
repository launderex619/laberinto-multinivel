package com.example.sulemaia.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulemaia.Adapter.MainActivityAdapter;
import com.example.sulemaia.Dialog.SimpleOkDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Helper.Parser;
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.Model.LandItem;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnCancel;
    private ImageButton btnOk;
    private Button btnAddFile;
    private TextView tvTextSelectedMap;
    private TableLayout tlTableMap;
    private RecyclerView rvWorldElements;
    private LinearLayout llItemsHolder;
    private String fileDataUnFiltered = "";
    private Hashtable<Integer, ArrayList<LandItem>> hashCodes;
    private View.OnClickListener buttonActions;
    private LandItem lastItemInTablePressed;
    private LandItem initialItem;
    private LandItem finalItem;
    private ArrayList<String> biomesInUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instancia de elementos del xml
        hashCodes = new Hashtable<>();
        biomesInUse = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvActivityTitle = findViewById(R.id.toolbar_title);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        btnAddFile = findViewById(R.id.btn_add_file);
        tvTextSelectedMap = findViewById(R.id.text_not_selected_file);
        tlTableMap = findViewById(R.id.table_map);
        llItemsHolder = findViewById(R.id.ll_items_holder);
        rvWorldElements = findViewById(R.id.rv_map_items);

        buttonActions = new ButtonActions(this);
        tvActivityTitle.setText(getString(R.string.main_activity_title));

        //asigno los eventos escuchadores para asignar las acciones de los botones
        btnCancel.setOnClickListener(buttonActions);
        btnAddFile.setOnClickListener(buttonActions);
        btnOk.setOnClickListener(buttonActions);

        btnCancel.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.INVISIBLE);
        llItemsHolder.setVisibility(View.INVISIBLE);

        //checamos si es la primera vez que la aplicacion inicia:
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START, true);
        if (firstStart) {
            //iniciamos el tutorial
            Intent intent = new Intent(this, Tutorial.class);
            startActivityForResult(intent, Constants.REQUEST_TUTORIAL);
            //iniciamos la descripcion de los elementos individuales
            new TapTargetSequence(this).targets(
                    new TapTargetHelper(this, btnAddFile, getString(R.string.add_map),
                            getString(R.string.description_add_map)).Create(),
                    new TapTargetHelper(this, tlTableMap, getString(R.string.map),
                            getString(R.string.description_map)).Create()
            ).start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_FILE_READ_EXTERNAL) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(fileUri);
                        int content = inputStream.read();
                        while (content != -1) {
                            fileDataUnFiltered += (char) content;
                            content = inputStream.read();
                        }
                        (new SimpleOkDialog(this,
                                getString(R.string.content_file), fileDataUnFiltered))
                                .build().create().show();
                        if (Parser.isValidStringInFile(fileDataUnFiltered) == Parser.FULL_FILE_IS_CORRECT) {
                            int key;
                            boolean firstStart;
                            showMapAndElements(Parser.getFileArray(fileDataUnFiltered));

                            //checamos si es la primera vez que la aplicacion inicia:
                            firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                                    .getBoolean(Constants.PREF_KEY_FIRST_START_MAIN_ACTIVITY, true);
                            if (firstStart) {
                                //iniciamos el tutorial
                                PreferenceManager.getDefaultSharedPreferences(this).edit()
                                        .putBoolean(Constants.PREF_KEY_FIRST_START_MAIN_ACTIVITY, false)
                                        .apply();
                                key = Integer.parseInt(hashCodes.keySet().toArray()[0].toString());
                                new TapTargetSequence(this).targets(
                                        new TapTargetHelper(this,
                                                hashCodes.get(key).get(0).getBtnItemMap(),
                                                getString(R.string.item_field_title),
                                                getString(R.string.item_field_description)).Create(),
                                        new TapTargetHelper(this, btnOk, getString(R.string.next_page_main_activity),
                                                getString(R.string.next_page_main_activity_description)).Create(),
                                        new TapTargetHelper(this, btnCancel, getString(R.string.delete_map),
                                                getString(R.string.delete_map_description)).Create()
                                ).start();
                            }

                        } else {
                            if (Parser.isValidStringInFile(fileDataUnFiltered) == Parser.INCONSISTENT_ID_QUANTITY) {
                                (new SimpleOkDialog(this, "Error", "Cantidad inconsistente de datos"))
                                        .build().create().show();
                            } else if (Parser.isValidStringInFile(fileDataUnFiltered) == Parser.INVALID_FILE_CONTENT) {
                                (new SimpleOkDialog(this, "Error", "Error en los datos del archivo"))
                                        .build().create().show();
                            } else if (Parser.isValidStringInFile(fileDataUnFiltered) == Parser.TOO_MANY_COLUMNS_OR_ROWS) {
                                (new SimpleOkDialog(this, "Error", "El límite de filas o columnas fue rebasado. (Max = 15)"))
                                        .build().create().show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                } else {
                    (new SimpleOkDialog(this,
                            getString(R.string.content_file), getString(R.string.unselected_file)))
                            .build().create().show();
                }
            }
        } else if (requestCode == Constants.RESULT_FOR_FIELD_INFORMATION) {
            if (resultCode == Activity.RESULT_OK) {
                String name;
                int color;
                boolean isFinal, isInitial;
                name = data.getStringExtra("name");
                color = data.getIntExtra("color", lastItemInTablePressed.getColor());
                isFinal = data.getBooleanExtra("isFinal", lastItemInTablePressed.isFinal());
                isInitial = data.getBooleanExtra("isInitial", lastItemInTablePressed.isInitial());
                for (LandItem item : hashCodes.get(lastItemInTablePressed.getCode())) {
                    item.setName(name);
                    item.setColor(color);
                }
                if (isFinal) {
                    if (initialItem == lastItemInTablePressed) {
                        initialItem = null;
                    }
                    if (finalItem == null) {
                        finalItem = lastItemInTablePressed;
                        finalItem.setFinal(isFinal);
                    } else {
                        finalItem.setFinal(!isFinal);
                        finalItem = lastItemInTablePressed;
                        finalItem.setFinal(isFinal);
                    }
                }
                if (isInitial) {
                    if (finalItem == lastItemInTablePressed) {
                        finalItem = null;
                    }
                    if (initialItem == null) {
                        initialItem = lastItemInTablePressed;
                        initialItem.setInitial(isInitial);
                    } else {
                        initialItem.setInitial(!isInitial);
                        initialItem = lastItemInTablePressed;
                        initialItem.setInitial(isInitial);
                    }
                }
                rvWorldElements.getAdapter().notifyDataSetChanged();
            }
        } else if (requestCode == Constants.REQUEST_TUTORIAL) {
            if (resultCode == RESULT_OK) {
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        //.putBoolean(Constants.PREF_KEY_FIRST_START, true)
                        .putBoolean(Constants.PREF_KEY_FIRST_START, false)
                        .apply();
            } else {
                //el tutorial fue cancelado, entonces se mostrara la siguiente ocacion
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean(Constants.PREF_KEY_FIRST_START, true)
                        .apply();
            }
        }
    }

    private void showMapAndElements(int[][] mapValues) {
        btnCancel.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        btnAddFile.setVisibility(View.GONE);
        tvTextSelectedMap.setVisibility(View.GONE);
        llItemsHolder.setVisibility(View.VISIBLE);

        createTable(mapValues);
        setColorsToTable();
        setReciclerViewForCodes();
    }

    private void setReciclerViewForCodes() {
        LinearLayoutManager mainLayoutManager = new LinearLayoutManager(getApplicationContext());
        mainLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvWorldElements.setLayoutManager(mainLayoutManager);
        rvWorldElements.setAdapter(new MainActivityAdapter(hashCodes,
                R.layout.item_land_main_activity, this));

    }

    private void setColorsToTable() {
        Enumeration e = hashCodes.keys();
        while (e.hasMoreElements()) {
            Integer i = (Integer) e.nextElement();
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
                    rnd.nextInt(256));
            for (LandItem item : hashCodes.get(i)) {
                item.setColor(color);
            }
        }
    }

    private void createTable(int[][] mapValues) {
        Hashtable<Integer, String> names = new Hashtable<>();
        int counter = 0;
        //creacion de las filas
        for (int i = 0; i < mapValues.length; i++) {
            TableRow tableRow = new TableRow(MainActivity.this);
            tableRow.setGravity(Gravity.CENTER);
            if (i == 0) {
                TableRow tableRowT = new TableRow(MainActivity.this);
                tableRowT.setGravity(Gravity.CENTER);
                for (int j = 0; j <= mapValues[i].length; j++) {
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(Parser.getLetterForInt(j));
                    tv.setTextSize(8f);
                    tv.setGravity(Gravity.CENTER);
                    tableRowT.addView(tv, new TableRow.LayoutParams(
                            tlTableMap.getWidth() / mapValues[0].length,
                            tlTableMap.getHeight() / (mapValues.length + 1)
                    ));
                }
                tlTableMap.addView(tableRowT);
            }
            //creacion de los Buttons de cada columna
            for (int j = 0; j < mapValues[i].length; j++) {
                if (j == 0) {
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(String.valueOf(i+1));
                    tv.setTextSize(8f);
                    tv.setGravity(Gravity.CENTER);
                    tableRow.addView(tv, new TableRow.LayoutParams(
                            tlTableMap.getWidth() / mapValues[0].length,
                            tlTableMap.getHeight() / (mapValues.length + 1)
                    ));
                }
                Button btn = new Button(MainActivity.this);
                LandItem item;
                btn.setText(String.valueOf(mapValues[i][j]));
                btn.setTextSize(8f);
                btn.setGravity(Gravity.CENTER);
                btn.setOnClickListener(buttonActions);
                item = new LandItem(btn, mapValues[i][j], new Point(j, i));
                if (!names.containsKey(mapValues[i][j])) {
                    names.put(mapValues[i][j], Constants.biomes[counter]);
                    counter++;
                }
                item.setName(names.get(mapValues[i][j]));
                btn.setTag(item);
                tableRow.addView(btn, new TableRow.LayoutParams(
                        tlTableMap.getWidth() / mapValues[0].length,
                        tlTableMap.getHeight() / (mapValues.length + 1)
                ));
                //si el elemento ya existe en la tabla agrego el btn a la lista
                if (hashCodes.containsKey(mapValues[i][j])) {
                    hashCodes.get(mapValues[i][j]).add(item);
                } else {
                    ArrayList<LandItem> arrayListItems = new ArrayList<LandItem>();
                    arrayListItems.add(item);
                    hashCodes.put(mapValues[i][j], arrayListItems);
                }
            }
            tlTableMap.addView(tableRow);
        }
    }

    private void startIntentToReadFile() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, Constants.REQUEST_FILE_READ_EXTERNAL);
    }

    private class ButtonActions implements View.OnClickListener,
            ActivityCompat.OnRequestPermissionsResultCallback {

        private Activity activity;

        ButtonActions(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            if (v == btnCancel) {
                hashCodes.clear();
                tlTableMap.removeAllViews();
                tlTableMap.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.INVISIBLE);
                btnAddFile.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.INVISIBLE);
                llItemsHolder.setVisibility(View.GONE);
                rvWorldElements.removeAllViews();
                tvTextSelectedMap.setVisibility(View.VISIBLE);

            } else if (v == btnAddFile) {
                tlTableMap.setVisibility(View.VISIBLE);
                fileDataUnFiltered = "";
                if (isPermissionGranted()) {
                    startIntentToReadFile();
                }
            } else if (v == btnOk) {
                if (finalItem != null) {
                    if (initialItem != null) {
                        Intent intent = new Intent(getApplicationContext(), CharacterSelector.class);
                        ArrayList<String> biomes = new ArrayList<>();
                        ArrayList<Integer> codes = new ArrayList<>();
                        ArrayList<Integer> colors = new ArrayList<>();
                        for (int i = 0; i < hashCodes.size(); i++) {
                            int key = Integer.parseInt(hashCodes.keySet().toArray()[i].toString());
                            biomes.add(hashCodes.get(key).get(0).getName());
                            codes.add(hashCodes.get(key).get(0).getCode());
                            colors.add(hashCodes.get(key).get(0).getColor());
                        }
                        intent.putExtra("contentFile", fileDataUnFiltered);
                        intent.putExtra("initialX", initialItem.getPosition().x);
                        intent.putExtra("initialY", initialItem.getPosition().y);
                        intent.putExtra("finalX", finalItem.getPosition().x);
                        intent.putExtra("finalY", finalItem.getPosition().y);
                        intent.putExtra("initialName", initialItem.getName());
                        intent.putExtra("finalName", finalItem.getName());
                        intent.putIntegerArrayListExtra("colors", colors);
                        intent.putStringArrayListExtra("biomes", biomes);
                        intent.putIntegerArrayListExtra("codes", codes);
                        intent.putExtra("x_Coordinate", lastItemInTablePressed.getX_Coordinate());
                        intent.putExtra("y_Coordinate", lastItemInTablePressed.getY_Coordinate());
                        startActivity(intent);
                    } else {
                        new SimpleOkDialog(MainActivity.this, getString(R.string.error),
                                getString(R.string.please_select_initial))
                                .build()
                                .setIcon(getDrawable(R.drawable.ic_warning_lime_24dp)).show();
                    }
                } else {
                    new SimpleOkDialog(MainActivity.this, getString(R.string.error),
                            getString(R.string.please_select_final))
                            .build()
                            .setIcon(getDrawable(R.drawable.ic_warning_lime_24dp)).show();
                }
            } else {
                LandItem lndI = (LandItem) v.getTag();
                biomesInUse.clear();
                for (int i = 0; i < hashCodes.size(); i++) {
                    final int key = Integer.parseInt(hashCodes.keySet().toArray()[i].toString());
                    biomesInUse.add(hashCodes.get(key).get(0).getName());
                }
                lastItemInTablePressed = lndI;
                Intent intent = new Intent(getApplicationContext(), FieldInformation.class);
                intent.putStringArrayListExtra("biomesInUse", biomesInUse);
                intent.putExtra("name", lastItemInTablePressed.getName());
                intent.putExtra("color", lastItemInTablePressed.getColor());
                intent.putExtra("isFinal", lastItemInTablePressed.isFinal());
                intent.putExtra("isInitial", lastItemInTablePressed.isInitial());
                intent.putExtra("code", lastItemInTablePressed.getCode());
                intent.putExtra("x_Coordinate", lastItemInTablePressed.getX_Coordinate());
                intent.putExtra("y_Coordinate", lastItemInTablePressed.getY_Coordinate());
                startActivityForResult(intent, Constants.RESULT_FOR_FIELD_INFORMATION);
            }
        }

        private boolean isPermissionGranted() {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.read_permission),
                            Toast.LENGTH_SHORT)
                            .show();
                }
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
                return false;
            }

            // Permission has already been granted
            return true;

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL:
                    if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.read_permission_granted,
                                Snackbar.LENGTH_SHORT)
                                .show();
                        startIntentToReadFile();
                    } else {
                        // Permission request was denied.
                        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.read_permission_denied,
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }
        }
    }
}

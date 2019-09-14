package com.example.sulemaia;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.example.sulemaia.Adapter.MainActivityAdapter;
import com.example.sulemaia.Dialog.SimpleOkDialog;
import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Model.LandItem;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instancia de elementos del xml
        hashCodes = new Hashtable<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvActivityTitle = findViewById(R.id.toolbar_title);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        btnAddFile = findViewById(R.id.btn_add_file);
        tvTextSelectedMap = findViewById(R.id.text_not_selected_file);
        tlTableMap = findViewById(R.id.table_map);
        llItemsHolder = findViewById(R.id.ll_items_holder);
        rvWorldElements = findViewById(R.id.rv_map_items);

        //clase creada por mi para recibir todos los eventos de los botones (clase = ButtonActions)
        buttonActions = new ButtonActions(this);

        tvActivityTitle.setText(getString(R.string.main_activity_title));

        //asigno los eventos escuchadores para asignar las acciones de los botones
        btnCancel.setOnClickListener(buttonActions);
        btnAddFile.setOnClickListener(buttonActions);
        btnOk.setOnClickListener(buttonActions);

        btnCancel.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.INVISIBLE);
        llItemsHolder.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_FILE_READ_EXTERNAL && resultCode == Activity.RESULT_OK) {
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
                    //funcion que implementara silva:
                    //if(isStringValid(fileDataUnFiltered){
                    showMapAndElements();
                    //} else {
                    // crearAlertDialog diciendo que el archivo no es valido xd
                    //}
                } catch (IOException e) {
                    e.printStackTrace();

                }
            } else {
                (new SimpleOkDialog(this,
                        getString(R.string.content_file), getString(R.string.unselected_file)))
                        .build().create().show();
            }
        }
    }

    private void showMapAndElements() {
        btnCancel.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        btnAddFile.setVisibility(View.GONE);
        tvTextSelectedMap.setVisibility(View.GONE);
        llItemsHolder.setVisibility(View.VISIBLE);

        createTable();
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
            for(LandItem item: hashCodes.get(i)){
                item.setColor(color);
            }
        }
    }

    private void createTable() {
        int tempValuesForFile[][] = Constants.mapValues;
        //creacion de las filas
        for (int i = 0; i < tempValuesForFile.length; i++) {
            TableRow tableRow = new TableRow(MainActivity.this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER);
            //creacion de los textViews de cada columna
            for (int j = 0; j < tempValuesForFile[i].length; j++) {
                Button btn = new Button(MainActivity.this);
                LandItem item;
                btn.setText(String.valueOf(tempValuesForFile[i][j]));
                btn.setTextSize(10f);
                btn.setGravity(Gravity.CENTER);
                btn.setOnClickListener(buttonActions);
                item = new LandItem(btn, tempValuesForFile[i][j]);
                tableRow.addView(btn, new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, tlTableMap.getHeight() / tempValuesForFile.length
                ));
                //si el elemento ya existe en la tabla agrego el btn a la lista
                if(hashCodes.containsKey(tempValuesForFile[i][j])){
                    hashCodes.get(tempValuesForFile[i][j]).add(item);
                }
                else{
                    ArrayList<LandItem> arrayListItems = new ArrayList<LandItem>();
                    arrayListItems.add(item);
                    hashCodes.put(tempValuesForFile[i][j], arrayListItems);
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

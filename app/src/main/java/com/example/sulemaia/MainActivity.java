package com.example.sulemaia;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.sulemaia.Helper.Constants;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnCancel;
    private ImageButton btnOk;
    private Button btnAddFile;
    private TextView tvTextSelectedMap;
    private TableLayout tlTableMap;
    private String fileDataUnFiltered = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instancia de elementos del xml
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvActivityTitle = findViewById(R.id.toolbar_title);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        btnAddFile = findViewById(R.id.btn_add_file);
        tvTextSelectedMap = findViewById(R.id.text_not_selected_file);
        tlTableMap = findViewById(R.id.table_map);
        //clase creada por mi para recibir todos los eventos de los botones (clase = ButtonActions)
        View.OnClickListener buttonActions = new ButtonActions(this);

        tvActivityTitle.setText(getString(R.string.main_activity_title));

        //asigno los eventos escuchadores para asignar las acciones de los botones
        btnCancel.setOnClickListener(buttonActions);
        btnAddFile.setOnClickListener(buttonActions);
        btnOk.setOnClickListener(buttonActions);

        btnCancel.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    (new AlertDialog.Builder(this)
                            .setTitle("Contenido del Archivo")
                            .setMessage(fileDataUnFiltered)
                            .setPositiveButton("OK", null)
                            .create()).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    (new AlertDialog.Builder(this)
                            .setTitle("Contenido del Archivo")
                            .setMessage("El archivo no es valido")
                            .setPositiveButton("OK", null)
                            .create()).show();
                }
            } else {
                (new AlertDialog.Builder(this)
                        .setTitle("Error de archivo")
                        .setMessage("No se selecciono ningun archivo")
                        .setPositiveButton("OK", null)
                        .create()).show();
            }
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

            } else if (v == btnAddFile) {
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

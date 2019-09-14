package com.example.sulemaia.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemaia.Model.LandItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import top.defaults.colorpicker.ColorPickerPopup;

public class FieldInformation extends AppCompatActivity {

    private LandItem item;
    private ImageView ivFieldColor;
    private EditText etFieldName;
    private TextView tvId;
    private CheckBox cbIsInitial;
    private CheckBox cbIsFinal;
    private FloatingActionButton fabOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_information);
        Intent fieldObject = getIntent();

        ivFieldColor = findViewById(R.id.iv_field_color);
        etFieldName = findViewById(R.id.et_field_name);
        tvId = findViewById(R.id.tv_field_id);
        cbIsInitial = findViewById(R.id.cb_field_initial);
        cbIsFinal = findViewById(R.id.cb_field_final);
        fabOk = findViewById(R.id.fab_ok);

        item = fieldObject.getParcelableExtra("field");

        ivFieldColor.setBackgroundColor(item.getColor());
        tvId.setText(String.valueOf(item.getCode()));
        etFieldName.setText(item.getName());

        ivFieldColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(getBaseContext())
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(false) // Enable alpha slider or not
                        .okTitle("Aceptar")
                        .cancelTitle("Cancelar")
                        .showIndicator(true)
                        .showValue(false)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                ivFieldColor.setBackgroundColor(color);
                            }
                        });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "" + item, Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}

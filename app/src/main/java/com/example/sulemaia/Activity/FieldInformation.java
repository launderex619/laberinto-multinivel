package com.example.sulemaia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Model.LandItem;
import com.example.sulemaia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;
import top.defaults.colorpicker.ColorPickerPopup;

public class FieldInformation extends AppCompatActivity {

    private CircleImageView ivFieldColor;
    private EditText etFieldName;
    private TextView tvId;
    private CheckBox cbIsInitial;
    private CheckBox cbIsFinal;
    private FloatingActionButton fabOk;
    private ListPopupWindow lpw;
    private String[] validBiomes;
    private ArrayList<String> biomesInUse;
    private int color;
    private String name;
    private int code;
    private boolean isFinal;
    private boolean isInitial;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_information);

        ButtonActions buttonActions = new ButtonActions(this);
        Intent fieldObject = getIntent();


        ivFieldColor = findViewById(R.id.iv_field_color);
        etFieldName = findViewById(R.id.et_field_name);
        tvId = findViewById(R.id.tv_field_id);
        cbIsInitial = findViewById(R.id.cb_field_initial);
        cbIsFinal = findViewById(R.id.cb_field_final);
        fabOk = findViewById(R.id.fab_ok);

        biomesInUse = fieldObject.getStringArrayListExtra("biomesInUse");
        color = fieldObject.getIntExtra("color", 0);
        code = fieldObject.getIntExtra("code", 0);
        name = fieldObject.getStringExtra("name");
        isInitial = fieldObject.getBooleanExtra("isInitial", false);
        isFinal = fieldObject.getBooleanExtra("isFinal", false);
        ivFieldColor.setImageDrawable(new ColorDrawable(color));
        tvId.setText(String.valueOf(code));
        etFieldName.setText(name);

        validBiomes = getValidBiomes();

        cbIsInitial.setChecked(isInitial);
        cbIsFinal.setChecked(isFinal);
        cbIsInitial.setOnClickListener(buttonActions);
        cbIsFinal.setOnClickListener(buttonActions);
        ivFieldColor.setOnClickListener(buttonActions);
        fabOk.setOnClickListener(buttonActions);
        etFieldName.setOnTouchListener(buttonActions);
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, validBiomes));
        lpw.setAnchorView(etFieldName);
        lpw.setModal(true);
        lpw.setOnItemClickListener(buttonActions);

    }

    private String[] getValidBiomes() {
        HashSet<String> set = new HashSet<>(Arrays.asList(Constants.biomes));
        set.removeAll(biomesInUse);
        set.add(name);
        return set.toArray(new String[set.size()]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    private class ButtonActions implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {
        Context context;
        public ButtonActions(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (v == ivFieldColor) {
                new ColorPickerPopup.Builder(context)
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
                            public void onColorPicked(int col) {
                                ivFieldColor.setImageDrawable(new ColorDrawable(col));
                                color = col;
                            }
                        });
            }
            else if( v == cbIsFinal){
                if(cbIsInitial.isChecked())
                    cbIsInitial.setChecked(!cbIsFinal.isChecked());
            }
            else if(v == cbIsInitial){
                if(cbIsFinal.isChecked())
                    cbIsFinal.setChecked(!cbIsInitial.isChecked());
            }
            else if (v == fabOk){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", etFieldName.getText());
                returnIntent.putExtra("color", color);
                returnIntent.putExtra("isInitial", cbIsInitial.isChecked());
                returnIntent.putExtra("isFinal", cbIsFinal.isChecked());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() >= (v.getWidth() - ((EditText) v)
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    lpw.show();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = validBiomes[position];
            etFieldName.setText(item);
            lpw.dismiss();
        }
    }
}
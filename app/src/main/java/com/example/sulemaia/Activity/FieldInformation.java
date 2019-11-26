package com.example.sulemaia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.ListPopupWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.sulemaia.Helper.Parser;
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.Model.LandItem;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;
import top.defaults.colorpicker.ColorPickerPopup;

/**
 * Class to obtain the information of each field/terrain.
 */
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
    private TextView cell;
    private String cellPos = "";
    private int cell_x_Pos, cell_y_Pos;
    private int code;
    private boolean isFinal;
    private boolean isInitial;

    /**
     * Class to configure all the initial instances for the class to work.
     * Also, we can activate the tutorial for the fist time the app runs.
     * @param savedInstanceState String bundle with the instances.
     */
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
        cell = findViewById(R.id.cell_name);

        biomesInUse = fieldObject.getStringArrayListExtra("biomesInUse");
        color = fieldObject.getIntExtra("color", 0);
        code = fieldObject.getIntExtra("code", 0);
        name = fieldObject.getStringExtra("name");
        isInitial = fieldObject.getBooleanExtra("isInitial", false);
        isFinal = fieldObject.getBooleanExtra("isFinal", false);
        cell_x_Pos = 1 + fieldObject.getIntExtra("x_Coordinate",3);
        cell_y_Pos = 1 + fieldObject.getIntExtra("y_Coordinate",3);
        ivFieldColor.setImageDrawable(new ColorDrawable(color));
        tvId.setText(String.valueOf(code));
        etFieldName.setText(name);
        cellPos = "Celda: " + Parser.getLetterForInt(cell_x_Pos) + ", " + cell_y_Pos;
        cell.setText(cellPos);

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

        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START_FIELD_INFORMATION, true);
        //checamos si es la primera vez que la aplicacion inicia:
        if (firstStart) {

            //iniciamos el tutorial
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean(Constants.PREF_KEY_FIRST_START_FIELD_INFORMATION, false)
                    .apply();
            new TapTargetSequence(this).targets(
                    new TapTargetHelper(this,
                            ivFieldColor,
                            getString(R.string.color),
                            getString(R.string.color_description)).Create(),
                    new TapTargetHelper(this, etFieldName,
                            getString(R.string.field_name),
                            getString(R.string.field_name_description)).Create()
            ).start();
        }
    }

    /**
     * Method to get the list of the valid biomes in that moment.
     * @return an Array set of strings with the valid biomes.
     */
    private String[] getValidBiomes() {
        HashSet<String> set = new HashSet<>(Arrays.asList(Constants.biomes));
        set.removeAll(biomesInUse);
        set.add(name);
        return set.toArray(new String[set.size()]);
    }

    /**
     * Method called by system.
     * @param item Related to the menu item of the activity.
     * @return true value.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    /**
     * Class for the implementation of the buttons.
     */
    private class ButtonActions implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {
        Context context;

        /**
         * We pass and assign the context of the activity.
         * @param context context of the correct activity.
         */
        public ButtonActions(Context context) {
            this.context = context;
        }

        /**
         * Tap configuration so we can choose the color of a terrain,
         * the initial and final tile fo the character, and finally
         * the ok button to proceed with the app.
         * @param v the corresponding with in the activity.
         */
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
                returnIntent.putExtra("name", etFieldName.getText().toString());
                returnIntent.putExtra("color", color);
                returnIntent.putExtra("isInitial", cbIsInitial.isChecked());
                returnIntent.putExtra("isFinal", cbIsFinal.isChecked());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        }

        /**
         *
         * @param v
         * @param event
         * @return
         */
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

        /**
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = validBiomes[position];
            etFieldName.setText(item);
            lpw.dismiss();
        }
    }
}

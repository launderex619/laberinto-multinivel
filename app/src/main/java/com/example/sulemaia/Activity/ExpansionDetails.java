package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sulemaia.Helper.Constants;
import com.example.sulemaia.Helper.TapTargetHelper;
import com.example.sulemaia.R;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;

/**
 * The instantiate of the class that gets the node expansion order from the user.
 */
public class ExpansionDetails extends AppCompatActivity {
    private static final int SLOT_QUANTITY = 4;
    Intent intent;
    private Button btn_start_algorithms, btn_start_manual;
    private ImageButton btn_down, btn_up, btn_left, btn_right;
    private TextView slots[], tv_start_algorithms;
    private ArrayList<String> expansionOrder;
    private Boolean state[][] = {{true, true, true, true}, {false, false, false, false}};

    @Override
    /**
     * On the creation of the class, we have four slots for the four possible directions in which
     * the character can move, creating an expansion order. All this works with buttons.
     * If its the first time the app is running, we show the tutorial.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expansion_details);
        intent = getIntent();
        slots = new TextView[SLOT_QUANTITY];
        slots[0] = findViewById(R.id.slot_1);
        slots[1] = findViewById(R.id.slot_2);
        slots[2] = findViewById(R.id.slot_3);
        slots[3] = findViewById(R.id.slot_4);
        tv_start_algorithms = findViewById(R.id.tv_start_algorithms);
        btn_down = findViewById(R.id.btn_down);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_up = findViewById(R.id.btn_up);
        btn_start_algorithms = findViewById(R.id.btn_start_algorithms);
        btn_start_manual = findViewById(R.id.btn_start_manual);

        btn_start_algorithms.setVisibility(View.GONE);
        expansionOrder = new ArrayList<>(4);
        ButtonActions buttonActions = new ButtonActions();
        tv_start_algorithms.setOnClickListener(buttonActions);
        btn_down.setOnClickListener(buttonActions);
        btn_left.setOnClickListener(buttonActions);
        btn_right.setOnClickListener(buttonActions);
        btn_up.setOnClickListener(buttonActions);
        btn_start_algorithms.setOnClickListener(buttonActions);
        btn_start_manual.setOnClickListener(buttonActions);


        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Constants.PREF_KEY_FIRST_START_EXPANSION_DETAILS, true);
        //checamos si es la primera vez que la aplicacion inicia:
        if (firstStart) {
            //iniciamos el tutorial
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean(Constants.PREF_KEY_FIRST_START_EXPANSION_DETAILS, false)
                    .apply();
            new TapTargetSequence(this).targets(
                    new TapTargetHelper(this,
                            slots[0],
                            getString(R.string.slot_position),
                            getString(R.string.slot_position_description)).Create(),
                    new TapTargetHelper(this,
                            btn_down,
                            getString(R.string.expansion_order_title_button),
                            getString(R.string.expansion_order_description_button)).Create(),
                    new TapTargetHelper(this,
                            tv_start_algorithms,
                            getString(R.string.start_algorithm),
                            getString(R.string.start_algorithm_description)).Create(),
                    new TapTargetHelper(this,
                    btn_start_manual,
                    getString(R.string.start_algorithm),
                    getString(R.string.start_manual_algorithm_description)).Create()
            ).start();
        }


    }

    /**
     * Method called by system on back button pressed.
     * @param item menu item corresponding to the activity.
     * @return a true value.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    /**
     * Implementation of the functions of the buttons.
     */
    private class ButtonActions implements View.OnClickListener {
        private static final String DOWN = "d", UP = "u", LEFT = "l", RIGHT = "r";

        /**
         * We get an order depending on the order tapped by the user on the app,
         * and proceed depending on the selection of the automatic or manual
         * algorithm.
         * @param v the button view tapped.
         */
        @Override
        public void onClick(View v) {
            if (v == btn_down) {
                int nextSlot = getNextSlotAvailable();
                expansionOrder.add(DOWN);
                switchViews(v, slots[nextSlot]);
                slots[nextSlot].setVisibility(View.INVISIBLE);
                v.setOnClickListener(null);
                if (isEndingState()) {
                    Toast.makeText(ExpansionDetails.this, "true", Toast.LENGTH_SHORT).show();
                    btn_start_algorithms.setVisibility(View.VISIBLE);
                    tv_start_algorithms.setVisibility(View.GONE);
                }
            } else if (v == btn_left) {
                int nextSlot = getNextSlotAvailable();
                expansionOrder.add(LEFT);
                switchViews(v, slots[nextSlot]);
                slots[nextSlot].setVisibility(View.INVISIBLE);
                v.setOnClickListener(null);
                if (isEndingState()) {
                    btn_start_algorithms.setVisibility(View.VISIBLE);
                    tv_start_algorithms.setVisibility(View.GONE);
                }

            } else if (v == btn_up) {
                int nextSlot = getNextSlotAvailable();
                expansionOrder.add(UP);
                switchViews(v, slots[nextSlot]);
                slots[nextSlot].setVisibility(View.INVISIBLE);
                v.setOnClickListener(null);
                if (isEndingState()) {
                    btn_start_algorithms.setVisibility(View.VISIBLE);
                    tv_start_algorithms.setVisibility(View.GONE);
                }

            } else if (v == btn_right) {
                int nextSlot = getNextSlotAvailable();
                expansionOrder.add(RIGHT);
                switchViews(v, slots[nextSlot]);
                slots[nextSlot].setVisibility(View.INVISIBLE);
                v.setOnClickListener(null);
                if (isEndingState()) {
                    btn_start_algorithms.setVisibility(View.VISIBLE);
                    tv_start_algorithms.setVisibility(View.GONE);
                }
            } else if (v == btn_start_manual) {
                intent.setClass(getApplicationContext(), GameScreen.class);
                startActivity(intent);
            } else if (v == btn_start_algorithms) {
                intent.setClass(getApplicationContext(), SearchAlgorithms.class);
                intent.putStringArrayListExtra("expansionOrder", expansionOrder);
                startActivity(intent);
            }
        }

        /**
         * We can proceed once the last order state is implemented by the user.
         * @return a true or false value.
         */
        private boolean isEndingState() {
            for (int i = 0; i < SLOT_QUANTITY; i++) {
                if (state[0][i]) {
                    return false;
                }
            }
            return true;
        }

        /**
         *
         * @param first
         * @param second
         */
        private void switchViews(View first, View second) {
            if (second == null)
                return;
            ViewGroup firstAnchor = (ViewGroup) first.getParent();
            ViewGroup secondAnchor = (ViewGroup) second.getParent();

            int firstIndex = firstAnchor.indexOfChild(first);
            int secondIndex = secondAnchor.indexOfChild(second);

            ViewGroup.LayoutParams temp = first.getLayoutParams();

            first.setLayoutParams(second.getLayoutParams());
            second.setLayoutParams(temp);

            firstAnchor.removeView(first);
            secondAnchor.removeView(second);

            firstAnchor.addView(second, firstIndex);
            secondAnchor.addView(first, secondIndex);
        }

        /**
         * Return the position of the next available slot for the expansion order selection.
         * @return an integer that is the position of the next slot available.
         */
        private int getNextSlotAvailable() {
            for (int i = 0; i < SLOT_QUANTITY; i++) {
                if (state[0][i]) {
                    state[0][i] = !state[0][i];
                    state[1][i] = !state[1][i];
                    return i;
                }
            }
            return -1;
        }
    }

}

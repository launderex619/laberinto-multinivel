package com.example.sulemaia.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sulemaia.R;

import java.util.ArrayList;


public class ExpansionDetails extends AppCompatActivity {
    private static final int SLOT_QUANTITY = 4;
    private Button btn_start_algorithms, btn_start_manual;
    private ImageButton btn_down, btn_up, btn_left, btn_right;
    private TextView slots[], tv_start_algorithms;
    private ArrayList<String> expansionOrder;
    private Boolean state[][] = {{true, true, true, true}, {false, false, false, false}};
    Intent intent;

    @Override
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    private class ButtonActions implements View.OnClickListener {
        private static final String DOWN = "d", UP = "u", LEFT = "l", RIGHT = "r";
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
            }
            else if (v == btn_start_manual){
                intent.setClass(getApplicationContext(), GameScreen.class);
                startActivity(intent);
            }
            else if (v == btn_start_algorithms){
                intent.setClass(getApplicationContext(), SearchAlgorithms.class);
                intent.putStringArrayListExtra("expansionOrder", expansionOrder);
                startActivity(intent);
            }
        }

        private boolean isEndingState() {
            for (int i = 0; i < SLOT_QUANTITY; i++) {
                if (state[0][i]) {
                    return false;
                }
            }
            return true;
        }

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

package com.example.sulemaia.Model;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

public class LandItem  {
    private Button btnItemMap;
    private int code;
    private boolean isInitial;
    private boolean isFinal;
    private String Name;
    private int color;

    public LandItem(Button btnItemMap, int code) {
        this.btnItemMap = btnItemMap;
        this.code = code;
        isInitial = false;
        isFinal = false;
        Name = "";
        color = 0;
    }

    public Button getBtnItemMap() {
        return btnItemMap;
    }

    public void setBtnItemMap(Button btnItemMap) {
        this.btnItemMap = btnItemMap;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
        if (initial){
            btnItemMap.setText("I");
            btnItemMap.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            btnItemMap.setTextColor(Color.WHITE);
        }
        else{
            btnItemMap.setText(String.valueOf(code));
            btnItemMap.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            btnItemMap.setTextColor(Color.BLACK);
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
        if (aFinal){
            btnItemMap.setText("F");
            btnItemMap.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            btnItemMap.setTextColor(Color.WHITE);
        }
        else{
            btnItemMap.setText(String.valueOf(code));
            btnItemMap.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            btnItemMap.setTextColor(Color.BLACK);
        }
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getColor() {
        return color;
    }

    public int getCode() {
        return code;
    }

    public void setColor(int color) {
        this.color = color;
        btnItemMap.setBackgroundColor(color);
    }
}
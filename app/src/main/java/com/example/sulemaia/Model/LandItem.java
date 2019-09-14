package com.example.sulemaia.Model;

import android.graphics.Color;
import android.widget.Button;

public class LandItem {
    private Button btnItemMap;
    private int code;
    private boolean isInitial;
    private boolean isFinal;
    private String Name;
    private int color;

    public LandItem(Button btnItemMap, int code) {
        this.btnItemMap = btnItemMap;
        this.code = code;
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
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
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

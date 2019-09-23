package com.example.sulemaia.Model;

import android.graphics.Point;
import android.widget.EditText;

public class GameLandItem {

    private EditText etItemMap;
    private int code;
    private boolean isInitial;
    private boolean isFinal;
    private String name;
    private int color;
    private Point position;

    public GameLandItem(EditText et, int code, Point position) {
        this.etItemMap = et;
        this.code = code;
        this.position = position;
    }

    public EditText getEtItemMap() {
        return etItemMap;
    }

    public void setEtItemMap(EditText etItemMap) {
        this.etItemMap = etItemMap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}

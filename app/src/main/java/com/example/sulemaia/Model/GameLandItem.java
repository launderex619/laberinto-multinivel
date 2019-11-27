package com.example.sulemaia.Model;

import android.graphics.Point;
import android.widget.EditText;

/**
 * Class for the creation and obtain of the land information.
 */
public class GameLandItem {

    private EditText etItemMap;
    private int code;
    private boolean isInitial;
    private boolean isFinal;
    private String name;
    private int color;
    private Point position;

    /**
     * Constructor of the land.
     * @param et Edit text view for the name.
     * @param code Code of the land.
     * @param position Position of the land.
     */
    public GameLandItem(EditText et, int code, Point position) {
        this.etItemMap = et;
        this.code = code;
        this.position = position;
    }

    /**
     * Get the edit text of the land <B>Deprecated.</B>
     * @return Edit text.
     */
    public EditText getEtItemMap() {
        return etItemMap;
    }

    /**
     * Set the edit text of the land <B>Deprecated.</B>
     * @param etItemMap
     */
    public void setEtItemMap(EditText etItemMap) {
        this.etItemMap = etItemMap;
    }

    /**
     * Get the code of the land.
     * @return land code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Set the code of the land.
     * @param code land code.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Check if the land is initial.
     * @return boolean value.
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * Set a land as initial.
     * @param initial boolean value.
     */
    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    /**
     * Check if the land is final.
     * @return boolean value.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Set a land as final.
     * @param aFinal boolean value.
     */
    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    /**
     * Get the name of the land.
     * @return Land name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name to a land.
     * @param name Land name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the color code of a land.
     * @return Color code.
     */
    public int getColor() {
        return color;
    }

    /**
     * Set the color to a land.
     * @param color Color code.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Get the position of the land.
     * @return Position value.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Set the position to a land.
     * @param position position value.
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}

package com.example.sulemaia.Model;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.widget.Button;

/**
 * Class to configure the information of a land item in the map.
 */
public class LandItem {
    private Button btnItemMap;
    private int code;
    private boolean isInitial;
    private boolean isFinal;
    private String Name;
    private int color;
    private Point position;

    /**
     * Get the position, in the map, of the land tapped.
     * @return a position value.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Set the position to a certain land value.
     * @param position position value.
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Constructor to the land value.
     * @param btnItemMap The button so it can be tapped by the user and obtain information.
     * @param code Code of the land.
     * @param pos Position, in the map, of the tile.
     */
    public LandItem(Button btnItemMap, int code, Point pos) {
        this.btnItemMap = btnItemMap;
        this.code = code;
        isInitial = false;
        isFinal = false;
        Name = "";
        color = 0;
        position = pos;
    }

    /**
     * Get the button of the land.
     * @return a button.
     */
    public Button getBtnItemMap() {
        return btnItemMap;
    }

    /**
     * Set a button to the land. <B>Deprecated.</B>
     * @param btnItemMap Button to the land tile.
     */
    public void setBtnItemMap(Button btnItemMap) {
        this.btnItemMap = btnItemMap;
    }

    /**
     * Check if the tile was designated as initial by the user.
     * @return boolean variable.
     */
    public boolean isInitial() {
        return isInitial;
    }

    /**
     * Set as initial a specific tile, putting the I char over it in the UI.
     * @param initial Boolean variable.
     */
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

    /**
     * Check if a certain tile is the final one, designated by the user.
     * @return a boolean variable.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Set a tile as final, by the user. Also, set the F char into it in the IU.
     * @param aFinal a boolean variable.
     */
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

    /**
     * Get the name of a land item tapped.
     * @return Name.
     */
    public String getName() {
        return Name;
    }

    /**
     * Set the name to a specific land.
     * @param name Name.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Get the color related to a specific land.
     * @return Color code.
     */
    public int getColor() {
        return color;
    }

    /**
     * Obtain the code related to the land.
     * @return Code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Set the color to a specific land.
     * @param color Color code.
     */
    public void setColor(int color) {
        this.color = color;
        btnItemMap.setBackgroundColor(color);
    }

    /**
     * Obtain the X coordinate of a tile.
     * @return X coordinate value.
     */
    public int getX_Coordinate(){
        return position.x;
    }

    /**
     * Obtain the Y coordinate of a tile.
     * @return Y coordinate value.
     */
    public int getY_Coordinate(){
        return position.y;
    }
}

package com.example.sulemaia.Model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Hashtable;

public class CharacterItem {
    private String name;
    private String mainLand;
    private Drawable icon;
    private ArrayList<String> lands;
    private ArrayList<Float> landsCosts;

    public ArrayList<Integer> getLandsColors() {
        return landsColors;
    }

    public void setLandsColors(ArrayList<Integer> landsColors) {
        this.landsColors = landsColors;
    }

    private ArrayList<Integer> landsColors;


    public CharacterItem(Drawable icon, String name, String mainLand) {
        this.name = name;
        this.mainLand = mainLand;
        this.icon = icon;
    }

    public ArrayList<String> getLands() {
        return lands;
    }

    public void setLands(ArrayList<String> lands) {
        this.lands = lands;
    }

    public ArrayList<Float> getLandsCosts() {
        return landsCosts;
    }

    public void setLandsCosts(ArrayList<Float> landsCosts) {
        this.landsCosts = landsCosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainLand() {
        return mainLand;
    }

    public void setMainLand(String mainLand) {
        this.mainLand = mainLand;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


}

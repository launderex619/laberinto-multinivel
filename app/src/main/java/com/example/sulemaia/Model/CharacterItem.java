package com.example.sulemaia.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class CharacterItem implements Serializable {
    private String name;
    private String mainLand;
    private int icon;
    private ArrayList<String> lands;
    private ArrayList<Float> landsCosts;
    private ArrayList<Integer> landsColors;
    private ArrayList<Boolean> canPass;

    public ArrayList<Boolean> getCanPass() {
        return canPass;
    }

    public void setCanPass(ArrayList<Boolean> canPass) {
        this.canPass = canPass;
    }

    public CharacterItem(int icon, String name, String mainLand) {
        this.name = name;
        this.mainLand = mainLand;
        this.icon = icon;
    }

    public ArrayList<Integer> getLandsColors() {
        return landsColors;
    }

    public void setLandsColors(ArrayList<Integer> landsColors) {
        this.landsColors = landsColors;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}

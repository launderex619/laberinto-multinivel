package com.example.sulemaia.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementation class for the Character item.
 */
public class CharacterItem implements Serializable {
    private String name;
    private String mainLand;
    private int icon;
    private ArrayList<String> lands;
    private ArrayList<Float> landsCosts;
    private ArrayList<Integer> landsColors;
    private ArrayList<Boolean> canPass;

    /**
     * Check if the character is able to pass through a certain terrain.
     * @return a boolean value.
     */
    public ArrayList<Boolean> getCanPass() {
        return canPass;
    }

    /**
     * Set to a character the ability to go through a certain terrain.
     * @param canPass boolean variable.
     */
    public void setCanPass(ArrayList<Boolean> canPass) {
        this.canPass = canPass;
    }

    /**
     * Constructor of the character.
     * @param icon Icon to the character.
     * @param name Name of the character.
     * @param mainLand Main land in which the character moves.
     */
    public CharacterItem(int icon, String name, String mainLand) {
        this.name = name;
        this.mainLand = mainLand;
        this.icon = icon;
    }

    /**
     * Get the land colors for the execution.
     * @return An integer list.
     */
    public ArrayList<Integer> getLandsColors() {
        return landsColors;
    }

    /**
     * Set the land colors to the execution.
     * @param landsColors Integer list of the codes.
     */
    public void setLandsColors(ArrayList<Integer> landsColors) {
        this.landsColors = landsColors;
    }

    /**
     * Get the lands for the execution.
     * @return Lands list.
     */
    public ArrayList<String> getLands() {
        return lands;
    }

    /**
     * Set the lands for the execution.
     * @param lands Lands list.
     */
    public void setLands(ArrayList<String> lands) {
        this.lands = lands;
    }

    /**
     * Get the lands costs for the executions.
     * @return Lands cost for the execution.
     */
    public ArrayList<Float> getLandsCosts() {
        return landsCosts;
    }

    /**
     * Set the lands costs to the actual execution.
     * @param landsCosts Lands costs list.
     */
    public void setLandsCosts(ArrayList<Float> landsCosts) {
        this.landsCosts = landsCosts;
    }

    /**
     * Get the name of a land.
     * @return Land name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of a land.
     * @param name Land name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the main land of a character.
     * @return Main land value.
     */
    public String getMainLand() {
        return mainLand;
    }

    /**
     * Set the main land to a character.
     * @param mainLand Main land value.
     */
    public void setMainLand(String mainLand) {
        this.mainLand = mainLand;
    }

    /**
     * Get the icon of a character.
     * @return icon.
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Set the icon to a character.
     * @param icon icon value.
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }


}

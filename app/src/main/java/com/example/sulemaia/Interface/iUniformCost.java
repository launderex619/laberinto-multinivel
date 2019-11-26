package com.example.sulemaia.Interface;

import com.example.sulemaia.Model.HeuristicPathTree;

/**
 * Public interface to control de implementation of the methods for Uniform Cost algorithm.
 */
public interface iUniformCost {
    /**
     * Declaration of the method to move the character, both logically and for the tree,
     * @param posY actual Y coordinate.
     * @param posX actual X coordinate.
     */
    void moveCharacter(int posY, int posX);

    /**
     * Metho to set color to a specific tile.
     * @param posY actual Y coordinate.
     * @param posX actual X coordinate.
     */
    void setColorToField(int posY, int posX);

    /**
     * Method to draw the path once the algorithm has finished.
     * @param heuristicPathTree the tree for guidance.
     */
    void drawPath(HeuristicPathTree heuristicPathTree);

    /**
     * Method to show a failure message if the algorithm is not able to find the last tile.
     */
    void showFailureMessage();
}

package com.example.sulemaia.Interface;

import com.example.sulemaia.Model.HeuristicPathTree;

public interface iAStar {
    void moveCharacter(int posY, int posX);
    void setColorToField(int posY, int posX);
    void drawPath(HeuristicPathTree heuristicPathTree);
    void showFailureMessage();
}
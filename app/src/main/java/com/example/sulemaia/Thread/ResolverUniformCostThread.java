package com.example.sulemaia.Thread;

import android.os.AsyncTask;

import com.example.sulemaia.Interface.iUniformCost;
import com.example.sulemaia.Model.HeuristicPathTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class ResolverUniformCostThread extends AsyncTask<Integer, HeuristicPathTree.Node, HeuristicPathTree> {
    private static final String DOWN = "d", UP = "u", LEFT = "l", RIGHT = "r";
    private ArrayList<HeuristicPathTree.Node> expandedNodes;
    private HashSet<HeuristicPathTree.Node> visitedNodes;
    private HeuristicPathTree.Node nodes[][];
    private boolean isPathFound;
    private iUniformCost iMethods;
    private long updateTime;
    private ArrayList<String> expansionOrder;
    private HeuristicPathTree tree;
    private int lastY, lastX;
    private int actualStep = 1;

    public ResolverUniformCostThread(HeuristicPathTree.Node[][] nodes,
                                     ArrayList<String> expansionOrder,
                                     long updateTime,
                                     iUniformCost iMethods){

        this.nodes = nodes;
        this.iMethods = iMethods;
        this.updateTime = updateTime;
        this.expansionOrder = expansionOrder;
        expandedNodes = new ArrayList<>(225);
        visitedNodes = new HashSet<>(225);
        isPathFound = false;
        tree = new HeuristicPathTree();
    }

    @Override
    protected HeuristicPathTree doInBackground(Integer... values){
        int initialY = values[0];
        int initialX = values[1];
        int finalY = lastY = values[2];
        int finalX = lastX = values[3];
        //code for pre execute the thread.
        iMethods.setColorToField(initialY, initialX);
        visitedNodes.add(nodes[initialY][initialX]);
        getNodesNextStep(nodes[initialY][initialX], expandedNodes, visitedNodes);
        tree.setAnchor(nodes[initialY][initialX]);
        tree.setInitial(nodes[initialY][initialX]);

        //while queue not empty...
        while(expandedNodes.size() > 0){
            HeuristicPathTree.Node actualNode = expandedNodes.remove(0);
            actualNode.setStep(actualStep++);

            //Evaluate if actualNode is finalNode.
            if(actualNode == nodes[finalY][finalX]){
                publishProgress(actualNode);
                expandedNodes.clear();
                tree.setLastNode(actualNode);
                tree.setFinal(actualNode);
                return tree;
            }

            //If actualNode != finalNode, we add it to the visited list.
            visitedNodes.add(actualNode);
            //We add node's children.
            publishProgress(actualNode);
            getNodesNextStep(actualNode, expandedNodes, visitedNodes);

            try{
                Thread.sleep(updateTime);
            }   catch (InterruptedException e){
                    e.printStackTrace();
            }
        }
        iMethods.showFailureMessage();
        return null;
    }

    @Override
    protected void onProgressUpdate(HeuristicPathTree.Node... values){
        super.onProgressUpdate(values);
        iMethods.moveCharacter(values[0].getPosY(), values[0].getPosX());
    }

    @Override
    protected  void onPostExecute(HeuristicPathTree heuristicPathTree){
        super.onPostExecute(heuristicPathTree);
        heuristicPathTree.drawDotTree(HeuristicPathTree.UNIFORM_COST);
        heuristicPathTree.endTree();
        iMethods.drawPath(heuristicPathTree);
    }

    private void getNodesNextStep(HeuristicPathTree.Node node,
                                  ArrayList<HeuristicPathTree.Node> expandedNodes,
                                  HashSet<HeuristicPathTree.Node> visitedNodes){

        int y = node.getPosY();
        int x = node.getPosX();
        for(String direction : expansionOrder){
            HeuristicPathTree.Node response = expandInDirection(direction, y, x, visitedNodes);
            if(response != null){
                if(!expandedNodes.contains(response)){
                    expandedNodes.add(response);
                }
            }
        }

        Collections.sort(expandedNodes, new Comparator<HeuristicPathTree.Node>(){
            @Override
            public int compare(HeuristicPathTree.Node o1, HeuristicPathTree.Node o2){
                return Float.compare(o1.getAccumulative(), o2.getAccumulative());
            }
        });
    }

    private HeuristicPathTree.Node expandInDirection(String direction, int y, int x,
                                                     HashSet<HeuristicPathTree.Node> visitedNodes){

        switch (direction) {
            case UP:
                //up
                if (y - 1 >= 0) {
                    iMethods.setColorToField(y - 1, x);
                    if (visitedNodes.contains(nodes[y - 1][x])) {
                        return null;
                    }
                    if (!nodes[y - 1][x].isAccessible()) {
                        visitedNodes.add(nodes[y - 1][x]);
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y - 1][x]);
                    return nodes[y - 1][x];
                }
                break;
            case DOWN:
                //down
                if (y + 1 < nodes.length) {
                    iMethods.setColorToField(y + 1, x);
                    if (visitedNodes.contains(nodes[y + 1][x])) {
                        return null;
                    }
                    if (!nodes[y + 1][x].isAccessible()) {
                        visitedNodes.add(nodes[y + 1][x]);
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y + 1][x]);
                    return nodes[y + 1][x];
                }
                break;
            case LEFT:
                //left
                if (x - 1 >= 0) {
                    iMethods.setColorToField(y, x - 1);
                    if (visitedNodes.contains(nodes[y][x - 1])) {
                        return null;
                    }
                    if (!nodes[y][x - 1].isAccessible()) {
                        visitedNodes.add(nodes[y][x - 1]);
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y][x - 1]);
                    return nodes[y][x - 1];
                }
                break;
            case RIGHT:
                //right
                if (x + 1 < nodes[0].length) {
                    iMethods.setColorToField(y, x + 1);
                    if (visitedNodes.contains(nodes[y][x + 1])) {
                        return null;
                    }
                    if (!nodes[y][x + 1].isAccessible()) {
                        visitedNodes.add(nodes[y][x + 1]);
                        return null;
                    }
                    tree.addNode(nodes[y][x], nodes[y][x + 1]);
                    return nodes[y][x + 1];
                }
                break;
        }
        return null;
    }
}

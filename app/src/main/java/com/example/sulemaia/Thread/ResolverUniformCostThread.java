package com.example.sulemaia.Thread;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sulemaia.Interface.iUniformCost;
import com.example.sulemaia.Model.HeuristicPathTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Class to control all the app flow when the Uniform Cost algorithm is being executed.
 */
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

    /**
     * Constructor of the whole resolver for the Uniform Cost Thread.
     * @param nodes Heuristic nodes.
     * @param expansionOrder Expansion order set by the user.
     * @param updateTime Update time for the thread.
     * @param iMethods Method, related to which algorithm is being used.
     */
    public ResolverUniformCostThread(HeuristicPathTree.Node[][] nodes,
                                     ArrayList<String> expansionOrder,
                                     long updateTime,
                                     iUniformCost iMethods) {

        this.nodes = nodes;
        this.iMethods = iMethods;
        this.updateTime = updateTime;
        this.expansionOrder = expansionOrder;
        expandedNodes = new ArrayList<>(225);
        visitedNodes = new HashSet<>(225);
        isPathFound = false;
        tree = new HeuristicPathTree();
    }

    /**
     * In the background execution of the thread, we are actually executing the main algorithm.
     * @param values Values related to the initial and final coordinates.
     * @return Either the tree or null value.
     */
    @Override
    protected HeuristicPathTree doInBackground(Integer... values) {
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
        Log.i("doInBackground start", "doInBackground: started");
        //while queue not empty...
        while (expandedNodes.size() > 0) {
            if (isCancelled()) {
                break;
            }
            HeuristicPathTree.Node actualNode = expandedNodes.remove(0);
            actualNode.setStep(actualStep++);

            //Evaluate if actualNode is finalNode.
            if (actualNode == nodes[finalY][finalX]) {
                publishProgress(actualNode);
                expandedNodes.clear();
                tree.setLastNode(actualNode);
                tree.setFinal(actualNode);
                return tree;
            }

            //If actualNode != finalNode, we add it to the visited list.
            visitedNodes.add(actualNode);
            publishProgress(actualNode);
            getNodesNextStep(actualNode, expandedNodes, visitedNodes);

            try {
                Thread.sleep(updateTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        iMethods.showFailureMessage();
        return null;
    }

    /**
     * Every time the thread refreshes, we have to move the character in the UI.
     * @param values Values related to the position of the character.
     */
    @Override
    protected void onProgressUpdate(HeuristicPathTree.Node... values) {
        super.onProgressUpdate(values);
        iMethods.moveCharacter(values[0].getPosY(), values[0].getPosX());
    }

    /**
     * Whenever the algorithm finishes, we have to finish the thread.
     * @param heuristicPathTree Tree to process.
     */
    @Override
    protected void onPostExecute(HeuristicPathTree heuristicPathTree) {
        super.onPostExecute(heuristicPathTree);
        heuristicPathTree.drawDotTree(HeuristicPathTree.UNIFORM_COST);
        heuristicPathTree.endTree();
        iMethods.drawPath(heuristicPathTree);
    }

    /**
     * Expand the nodes correctly, according to the limitations of the map.
     * @param node Node to be processed.
     * @param expandedNodes Expanded nodes list so we dont repeat.
     * @param visitedNodes Visited nodes list so we dont repeat.
     */
    private void getNodesNextStep(HeuristicPathTree.Node node,
                                  ArrayList<HeuristicPathTree.Node> expandedNodes,
                                  HashSet<HeuristicPathTree.Node> visitedNodes) {

        int y = node.getPosY();
        int x = node.getPosX();
        for (String direction : expansionOrder) {
            HeuristicPathTree.Node response = expandInDirection(direction, y, x, visitedNodes);
            if (response != null) {
                if (!expandedNodes.contains(response)) {
                    setFitness(response, node);
                    expandedNodes.add(response);
                }
            }
        }

        Collections.sort(expandedNodes, new Comparator<HeuristicPathTree.Node>() {
            /**
             * Compare method to control the algorithm correctly.
             * @param o1 First node to compare.
             * @param o2 Second node to compare.
             * @return Compared value.
             */
            @Override
            public int compare(HeuristicPathTree.Node o1, HeuristicPathTree.Node o2) {
                return Float.compare(o1.getAccumulative(), o2.getAccumulative());
            }
        });
    }

    /**
     * Fitness method so we know which node to select each time.
     * @param response Node to process.
     */
    private void setFitness(HeuristicPathTree.Node response, HeuristicPathTree.Node Father) {
        response.setAccumulative(Father.getAccumulative() + Father.getCost());
    }

    /**
     * check if the direction in which the node is trying to be expanded is available or has any limitations.
     * @param direction Direction to be expanded.
     * @param y Y coordinate of the actual node.
     * @param x X coordinate of the actual node.
     * @param visitedNodes Visited nodes list so we dont repeat.
     * @return a null value and/or the next coordinate to process.
     */
    private HeuristicPathTree.Node expandInDirection(String direction, int y, int x,
                                                     HashSet<HeuristicPathTree.Node> visitedNodes) {

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

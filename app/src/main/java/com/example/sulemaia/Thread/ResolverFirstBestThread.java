package com.example.sulemaia.Thread;

import android.os.AsyncTask;

import com.example.sulemaia.Interface.iFirstTheBest;
import com.example.sulemaia.Model.HeuristicPathTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Resolver class for the thread and execution of First Best Algorithm.
 */
public class ResolverFirstBestThread extends AsyncTask<Integer, HeuristicPathTree.Node, HeuristicPathTree> {
    private static final int MANHATTAN = 0;
    private static final int EUCLIDIANA = 1;
    private static final String DOWN = "d", UP = "u", LEFT = "l", RIGHT = "r";
    private ArrayList<HeuristicPathTree.Node> expandedNodes;
    private HashSet<HeuristicPathTree.Node> visitedNodes;
    private HeuristicPathTree.Node nodes[][];
    private boolean isPathFounded;
    private iFirstTheBest iMethods;
    private long updateTime;
    private ArrayList<String> expansionOrder;
    private HeuristicPathTree tree;
    private int measureMode;
    private int lastY, lastX;
    private int actualStep = 1;

    /**
     * First Best algorithm constructor.
     * @param nodes Heuristic nodes.
     * @param expansionOrder List for expanded nodes.
     * @param updateTime Value for the thread refresh.
     * @param iMethods Method (related to the algorithm).
     * @param measureMode Measure mode, since First Best uses Manhattan or Euclidean to weight the nodes.
     */
    public ResolverFirstBestThread(HeuristicPathTree.Node[][] nodes,
                                   ArrayList<String> expansionOrder,
                                   long updateTime,
                                   iFirstTheBest iMethods,
                                   int measureMode) {
        this.nodes = nodes;
        this.iMethods = iMethods;
        this.updateTime = updateTime;
        this.expansionOrder = expansionOrder;
        this.measureMode = measureMode;
        expandedNodes = new ArrayList<>(225);
        visitedNodes = new HashSet<>(225);
        isPathFounded = false;
        tree = new HeuristicPathTree();
    }

    /**
     * In the background execution of the thread, we execute the actual algorithm.
     * @param values Values related to the coordinates for initial and final positions.
     * @return final tree of null.
     */
    @Override
    protected HeuristicPathTree doInBackground(Integer... values) {
        int initialY = values[0];
        int initialX = values[1];
        int finalY = lastY = values[2];
        int finalX = lastX = values[3];
        //code for pre execute the thread
        iMethods.setColorToField(initialY, initialX);
        visitedNodes.add(nodes[initialY][initialX]);
        getNodesNextStep(nodes[initialY][initialX], expandedNodes, visitedNodes);
        tree.setAnchor(nodes[initialY][initialX]);
        tree.setInitial(nodes[initialY][initialX]);

        // mientras la cola no este vacia
        while (expandedNodes.size() > 0) {
            if(isCancelled()){
                break;
            }
            HeuristicPathTree.Node actualNode = expandedNodes.remove(0);
            actualNode.setStep(actualStep++);
            //evaluamos el nodo
            if (actualNode == nodes[finalY][finalX]) {
                publishProgress(actualNode);
                expandedNodes.clear();
                tree.setLastNode(actualNode);
                tree.setFinal(actualNode);
                return tree;
            }
            //lo agregamos al set de visitados
            visitedNodes.add(actualNode);
            //agregamos a los hijos
            //expandedNodes.addAll(getNodesNextStep(actualNode.getPosY(), actualNode.getPosX(), expandedNodes, visitedNodes));.
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
     * Every time the thread makes a step, we change the character's position.
     * @param values Values of the positions.
     */
    @Override
    protected void onProgressUpdate(HeuristicPathTree.Node... values) {
        super.onProgressUpdate(values);
        iMethods.moveCharacter(values[0].getPosY(), values[0].getPosX());
    }

    /**
     * Once the algorithm has finished, we actually need to end the thread and draw the path over the UI.
     * @param heuristicPathTree The tree whose path needs to be drawn.
     */
    @Override
    protected void onPostExecute(HeuristicPathTree heuristicPathTree) {
        super.onPostExecute(heuristicPathTree);
        heuristicPathTree.drawDotTree(HeuristicPathTree.FIRST_BEST);
        heuristicPathTree.endTree();
        iMethods.drawPath(heuristicPathTree);
    }

    /**
     * Get the children of a node, if they are reachable (the is map available), and if the node
     * is accessible by the options written by the user.
     * @param node Node to be expanded.
     * @param expandedNodes List of expanded nodes so we dont repeat.
     * @param visitedNodes List of visited nodes so we dont repeat.
     */
    private void getNodesNextStep(
            HeuristicPathTree.Node node,
            ArrayList<HeuristicPathTree.Node> expandedNodes,
            HashSet<HeuristicPathTree.Node> visitedNodes) {
        int y = node.getPosY();
        int x = node.getPosX();
        for (String direction : expansionOrder) {
            HeuristicPathTree.Node response = expandInDirection(direction, y, x, visitedNodes);
            //area for fitness
            if (response != null) {
                if (!expandedNodes.contains(response)) {
                    setFitness(response);
                    expandedNodes.add(response);
                }
            }
        }
        
        Collections.sort(expandedNodes, new Comparator<HeuristicPathTree.Node>() {
            /**
             * Compare two nodes to be able to execute the algorithm correctly.
             * @param o1 First node to compare.
             * @param o2 Second node to compare.
             * @return the comparative value.
             */
            @Override
            public int compare(HeuristicPathTree.Node o1, HeuristicPathTree.Node o2) {
                return Float.compare(o1.getRemaining(), o2.getRemaining());
            }
        });
//        Collections.sort(expandedNodes, new Comparator<HeuristicPathTree.Node>() {
//            @Override
//            public int compare(HeuristicPathTree.Node o1, HeuristicPathTree.Node o2) {
//                return Float.compare(o1.getCost(), o2.getCost());
//            }
//        });
        //return nodes;
    }

    /**
     * With the fitness, we either use the Euclidean or Manhattan measurment, and set the remaining
     * measures to the node.
     * @param response the node to process.
     */
    private void setFitness(HeuristicPathTree.Node response) {
        float distance = 0f;
        switch (measureMode) {
            case EUCLIDIANA:
                distance = (float) Math.sqrt(Math.pow(lastX - response.getPosX(), 2) + Math.pow(lastY - response.getPosY(), 2));
                response.setRemaining(distance);
                break;
            case MANHATTAN:
                distance = (float) (Math.abs(response.getPosX() - lastX) + Math.abs(response.getPosY() - lastY));
                response.setRemaining(distance);
                break;
        }
    }

    /**
     * Method to expand a node in all directions, following the expansion order set by the user,
     * making sure the expansion is possible, rather for map access, or limitations by the user.
     * @param direction Direction to check to be expanded.
     * @param y Y actual coordinate.
     * @param x X actual coordinate.
     * @param visitedNodes List of visited nodes so we dont expand incorrectly.
     * @return
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


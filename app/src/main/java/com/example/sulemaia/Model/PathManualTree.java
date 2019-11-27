package com.example.sulemaia.Model;

import com.example.sulemaia.Helper.GraphViz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Whole class for the creation of the tree that belongs to the manual
 * gameplay (i.e.: when the game is not running with automatic algorithms
 * like A*, Uniform Cost or BestCost.
 */
public class PathManualTree {

    private Node anchor;
    private GraphViz graphVizHelper;
    private HashSet<Node> visitedNodes;
    private HashSet<Node> visitedInExpanded;
    private ArrayList<Node> expansionOrder;

    /**
     * Initialize a graphViz tree with an initial node received by parameter,
     * which is the anchor.
     * @param anchor initial node for the tree.
     */
    public PathManualTree(Node anchor) {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
        this.anchor = anchor;
        this.anchor.setFather(anchor);
        visitedNodes = new HashSet<>();
        visitedNodes.add(anchor);
        expansionOrder = new ArrayList<>();
        visitedInExpanded = new HashSet<>();
        expansionOrder.add(anchor);
        visitedInExpanded.add(anchor);
    }

    /**
     * this method adds a node to a list of inaccessible nodes
     * @param node node to set inaccessible
     */
    public void addNodeToInvalidNodes(Node node){
        visitedNodes.add(node);
    }

    /**
     * This method adds a child to the specified node. If the father already has that node,
     * child is not added
     *
     * @param father parent node
     * @param child  child node
     */
    public void addNode(Node father, Node child) {
        if(exists(child)){
            return;
        }
        father.addChild(child);
        visitedNodes.add(child);
    }

    /**
     * Create a step movement between father and child
     * @param child final node
     */
    public void addMovement(Node child){
//        graphVizHelper.addConectorWithLabel(father.name, child.getName(),
//                String.valueOf(child.getCost()));
//        graphVizHelper.addNodeStep(child.getName(), child.getStep());
        expansionOrder.add(child);
    }

    /**
     * Method to check if the node already exist as a child, so we wont repeat nodes,
     * or even that we have the chance to change it to another father to correct the
     * flow of the tree.
     * @param child Child node to look for.
     * @return true or false depending on if we find the node or not.
     */
    public boolean exists(Node child) {
        return visitedNodes.contains(child);
    }

    /**
     * We have to manually end the tree, again, so the nodes wont be displayed in
     * an incorrect order on the graphic implementation. This is rather complicated
     * because a lot of things have to be taken in account for the manual gameplay.
     * All these problems are out of the panorama when the game is played automatically
     * by any algorithm.
     */
    public void endTree() {
        for(int i = 1; i < expansionOrder.size()-1; i++){
            if(!expansionOrder.get(i-1).getChildren().contains(expansionOrder.get(i))){
                expansionOrder.get(i).getFather().getChildren().remove(expansionOrder.get(i));
                expansionOrder.get(i).setFather(expansionOrder.get(i-1));
                expansionOrder.get(i-1).addChild(expansionOrder.get(i));
            }
        }
        expansionOrder.get(expansionOrder.size()-1).getFather().getChildren().remove(expansionOrder.get(expansionOrder.size()-1));
        expansionOrder.get(expansionOrder.size()-2).addChild(expansionOrder.get(expansionOrder.size()-1));
        for(int i = 0; i < expansionOrder.size()-1; i++){
            drawTree(expansionOrder.get(i));
        }
        graphVizHelper.addNodeStep(expansionOrder.get(expansionOrder.size()-1).getName(),
                expansionOrder.get(expansionOrder.size()-1).getStep());
        graphVizHelper.end_graph();
    }

    /**
     * We draw the tree node by node checking which nodes are the actual node's children.
     * @param node Father node to look for children.
     */
    private void drawTree(Node node) {
        //if(node.getFather() != node){
            for(Node child : node.getChildren()){
                if(!visitedInExpanded.contains(child)){
//                    if(child.getFather() != node){
//                        child.getFather().getChildren().remove(child);
//                        child.setFather(node);
//                    }
                    graphVizHelper.addConectorWithLabel(node.getName(), child.getName(),
                            String.valueOf(node.getCost()));
                }
                visitedInExpanded.add(child);
            }
            graphVizHelper.addNodeStep(node.getName(), node.getStep());
            visitedInExpanded.add(node);
        //}
    }

    /**
     * Get the source for the tree creation from the GraphViz class. This is part of the
     * configuration of the URL to create the tree in the git.
     * @return a dot-language source for the tree.
     */
    public String getDotTree() {
        return graphVizHelper.getDotSource();
    }

    /**
     * Set a certain node as the initial node for the graphical implementation.
     * @param node The original node to initiate the graphic tree.
     */
    public void setInitial(Node node) {
        graphVizHelper.setInitial(node.getName());
    }

    /**
     * Set a certain node as the final node for the graphical implementation.
     * @param node The final node to get the graphic tree to an end.
     */
    public void setFinal(Node node) {
        graphVizHelper.setFinal(node.getName());
    }

    /**
     * Public class that contains all the attributes of the node that corresponds
     * to a Manual Tree (Different from a Heuristic Tree Node).
     */
    public static class Node {
        private ArrayList<Node> children;
        private Node father;
        private StringBuilder step;
        private String name;
        private float cost;

        /**
         * Method to check if a certain tile (node) is accessible by the character. <B>Deprecated.</B>
         * @return True or false variable.
         */
        public boolean isAccessible() {
            return accessible;
        }

        /**
         * Set to a node the validation that it is accessible by the character. <B>Deprecated.</B>
         * @param accessible Full the attribute.
         */
        public void setAccessible(boolean accessible) {
            this.accessible = accessible;
        }

        private boolean accessible;

        /**
         * Constructor of the node.
         * @param name Name of the node.
         * @param cost Cost of the node (from the tile configured by the user).
         * @param accessible Whether the node is accessible or not by the character.
         */
        public Node(String name, float cost, boolean accessible) {
            this.children = new ArrayList<>(4); //4 por que no puede tener mas de 4 hijos de momento
            this.accessible = accessible;
            this.step = new StringBuilder();
            this.name = name;
            this.cost = cost;
        }

        /**
         * Return all of a node's children.
         * @return An arraylist of children.
         */
        public ArrayList<Node> getChildren() {
            return children;
        }

        /**
         * Add a node as the child of a father node.
         * @param child The node to be advocated as child.
         */
        private void addChild(Node child) {
            child.setFather(this);
            this.children.add(child);
        }

        /**
         * Get the father node of a certain node.
         * @return Father node of the actual node.
         */
        public Node getFather() {
            return father;
        }

        /**
         * Set a node as a father of another certain node.
         * @param father Father node.
         */
        public void setFather(Node father) {
            this.father = father;
        }

        /**
         * Get the actual step in which the algorithm is working right now.
         * @return The value of the step.
         */
        public String getStep() {
            return step.toString();
        }

        /**
         * Set the atual step to a certain node.
         * @param step The value of the actual step.
         */
        public void setStep(int step) {
            this.step.append(step);
            this.step.append(", ");
        }

        /**
         * Get the name of a node.
         * @return Name of the node.
         */
        public String getName() {
            return name;
        }

        /**
         * Set the name to a node.
         * @param name Name of the node.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Get the cost of a node.
         * @return The cost value.
         */
        public float getCost() {
            return cost;
        }

        /**
         * Set a cost to a node. <B>Deprecated.</B> because we put it in directly through
         * the constructor, and dont use it anymore later, since the manual algorithm
         * doesnt care about the weights of the nodes.
         * @param cost Cost to be put into the node.
         */
        public void setCost(float cost) {
            this.cost = cost;
        }
    }
}

package com.example.sulemaia.Model;

import com.example.sulemaia.Helper.GraphViz;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class for the creation of the graphic tree in the case of the execution
 * of an automatic resolution algorithm (i.e: Uniform Cost, First Best, A*).
 */
public class HeuristicPathTree {

    public static final int UNIFORM_COST = 0;
    public static final int FIRST_BEST = 1;
    public static final int A_STAR = 2;
    private Node anchor;
    private GraphViz graphVizHelper;
    private Node lastNode;

    /**
     * Constructor of the Heuristic Path Tree. <B>Deprecated.</B>
     * @param anchor Anchor node to start the tree.
     */
    public HeuristicPathTree(Node anchor) {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
        this.anchor = anchor;
        this.anchor.setFather(anchor);
    }

    /**
     * Constructor of the first important URL part of the Heuristic Path Tree.
     */
    public HeuristicPathTree() {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
    }

    /**
     * Method to set the anchor node to the heuristic tree.
     * @param anchor Anchor node to the heuristic tree.
     */
    public void setAnchor(Node anchor) {
        this.anchor = anchor;
        this.anchor.setFather(anchor);
    }

    /**
     * This method adds a child to the specified node. If the father or child is null, it does nothing
     *
     * @param father parent node
     * @param child  child node
     */
    public void addNode(Node father, Node child) {
        if (child == null || father == null) {
            return;
        }
        father.addChild(child);
        child.setFather(father);
    }

    /**
     * Create a step movement between father and child for Uniform Cost Algorithm.
     * @param child final node
     */
    public void addMovementAccumulative(Node child) {
        graphVizHelper.addConectorWithLabel(child.getFather().getName(), child.getName(),
                String.valueOf(child.getAccumulative()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }

    /**
     * Create a step movement between father and child for First Best Algorithm.
     * @param child final node
     */
    public void addMovementRemaining(Node child) {
        graphVizHelper.addConectorWithLabel(child.getFather().getName(), child.getName(),
                String.valueOf(child.getRemaining()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }

    /**
     * Create a step movement between father and child for A* algorithm.
     * @param child final node
     */
    public void addMovementAll(Node child) {
        graphVizHelper.addConectorWithLabel(child.getFather().getName(), child.getName(),
                child.getAccumulative() + " + " + child.getRemaining() + " = " +
                        (child.getAccumulative() + child.getRemaining()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }

    /**
     * GraphViz URL ending part for the graphic tree.
     */
    public void endTree() {
        graphVizHelper.end_graph();
    }

    /**
     * Method to get the whole tree in dot language for the graphic implementation.
     * @return A whole tree in dot language.
     */
    public String getDotTree() {
        return graphVizHelper.getDotSource();
    }

    /**
     * Method to set the initial configuration of the tree.
     * @param node Initial node.
     */
    public void setInitial(Node node) {
        graphVizHelper.setInitial(node.getName());
    }

    /**
     * Method to set the final configuration of the tree.
     * @param node Final node.
     */
    public void setFinal(Node node) {
        graphVizHelper.setFinal(node.getName());
    }

    /**
     * Method to set the last node for the tree.
     * @param lastNode Last node of the tree.
     */
    public void setLastNode(Node lastNode) {
        this.lastNode = lastNode;

    }

    /**
     * Obtain the final node of the tree.
     * @return the final node.
     */
    public Node getFinalNode() {
        return lastNode;
    }

    /**
     * Method to get the first node of the tree.
     * @return anchor node.
     */
    public Node getAnchor() {
        return anchor;
    }

    /**
     * Method for the creation of the whole dot-language tree depending of what
     * heuristic algorithm is being executed at the moment.
     * @param expansionAlgorithm The code of the heuristic algorithm being used,
     *                           defined at the top of this document, as final
     *                           values.
     */
    private void createDotTree(int expansionAlgorithm) {
        switch (expansionAlgorithm) {
            case UNIFORM_COST:
                inUniformCost(anchor);
                break;
            case FIRST_BEST:
                inOrderFirst(anchor);
                break;
            case A_STAR:
                inAstar(anchor);
                break;
        }
    }

    /**
     * Algorithm to obtain the tree in an Inorder organization, to make sure it will
     * be displayed correctly.
     * @param father Father node each time.
     */
    private void inOrderFirst(Node father) {
        for (Node child : father.getChildren()) {
            addMovementRemaining(child);
        }
        for (Node child : father.getChildren()) {
            inOrderFirst(child);
        }
    }

    private void inUniformCost(Node father) {
        for (Node child : father.getChildren()) {
            addMovementAccumulative(child);
        }
        for (Node child : father.getChildren()) {
            addMovementAccumulative(child);
        }
    }

    private void inAstar(Node father) {
        for (Node child : father.getChildren()) {
            addMovementAll(child);
        }
        for (Node child : father.getChildren()) {
            addMovementAll(child);
        }
    }

    /**
     * Method to call the whole drawing of the tree.
     * @param expansionAlgorithm Code related to one of the three expansion algorithms.
     */
    public void drawDotTree(int expansionAlgorithm) {
        createDotTree(expansionAlgorithm);
    }

    /**
     * Class for the creation of a node related to any Heuristic Algorithm.
     */
    public static class Node {
        private ArrayList<Node> children;
        private Node father;
        private StringBuilder step;
        private String name;
        private float cost;
        private int posY, posX;
        private float accumulative;
        private float remaining;
        private boolean accessible;

        /**
         * Constructor of the Heuristic Algorithm Node.
         * @param name Name of the node.
         * @param cost Cost of the node.
         * @param accessible Whether the node is accessible to the character or not.
         * @param y Y coordinate.
         * @param x X coordinate.
         */
        public Node(String name, float cost, boolean accessible, int y, int x) {
            this.children = new ArrayList<>(4); //4 por que no puede tener mas de 4 hijos.
            this.accessible = accessible;
            this.step = new StringBuilder();
            this.name = name;
            this.cost = cost;
            this.accumulative = 0f;
            this.remaining = 0f;
            this.posY = y;
            this.posX = x;
        }

        /**
         * Check if a node is accessible by the character.
         * @return a boolean variable.
         */
        public boolean isAccessible() {
            return this.accessible;
        }

        /**
         * Check if the actual node is the final node. <B>Deprecated.</B>
         * @param y Y coordinate.
         * @param x X coordinate.
         * @return a boolean variable.
         */
        public boolean isFinal(int y, int x) {
            return (x == posX && y == posY);
        }

        /**
         * Method to set a node as accessible or not to the character. <B>Deprecated</B>
         * @param accessible boolean variable.
         */
        public void setAccessible(boolean accessible) {
            this.accessible = accessible;
        }

        /**
         * Set the current step to the node. <B>Deprecated.</B>
         * @param step Step number.
         */
        public void setStep(StringBuilder step) {
            this.step = step;
        }

        /**
         * Get the Y coordinate of the node.
         * @return a position.
         */
        public int getPosY() {
            return posY;
        }

        /**
         * Set y coordinate to a node. <B>Deprecated.</B>
         * @param posY Y coordinate.
         */
        public void setPosY(int posY) {
            this.posY = posY;
        }

        /**
         * Get the X coordinate from a node.
         * @return X coordinate.
         */
        public int getPosX() {
            return posX;
        }

        /**
         * Set the X coordinate to a node. <B>Deprecated.</B>
         * @param posX X coordinate.
         */
        public void setPosX(int posX) {
            this.posX = posX;
        }

        /**
         * Get the accumulative value of the node. Criteria used for the
         * Uniform Cost algorithm.
         * @return Node weight at that moment.
         */
        public float getAccumulative() {
            return accumulative;
        }

        /**
         * Method to get the accumulative value of the node. Criteria used for the
         * Uniform Cost algorithm.
         * @param accumulative accumulative value of the node.
         */
        public void setAccumulative(float accumulative) {
            this.accumulative = accumulative;
        }

        /**
         * Get the remaining value from the current node to the final node.
         * @return a remaining value.
         */
        public float getRemaining() {
            return remaining;
        }

        /**
         * Set the remaining value from a certain node to the final node.
         * @param remaining remaining value.
         */
        public void setRemaining(float remaining) {
            this.remaining = remaining;
        }

        /**
         * Get the children related to a certain node.
         * @return an Arraylist containing the children list.
         */
        public ArrayList<Node> getChildren() {
            return children;
        }

        /**
         * Add a child to a specific father node.
         * @param child Child node to be added to the father.
         */
        private void addChild(Node child) {
            child.setFather(this);
            this.children.add(child);
        }

        /**
         * Get the father related to a certain node.
         * @return the father node.
         */
        public Node getFather() {
            return father;
        }

        /**
         * Set a father to a certain node.
         * @param father Father node.
         */
        public void setFather(Node father) {
            this.father = father;
        }

        /**
         * Get the current step of the algorithm.
         * @return the step value.
         */
        public String getStep() {
            return step.toString();
        }

        /**
         * Set the current step to the node.
         * @param step current step.
         */
        public void setStep(int step) {
            this.step.append(step);
            this.step.append(", ");
        }

        /**
         * Get the name of the node.
         * @return Name of the node.
         */
        public String getName() {
            return name;
        }

        /**
         * Set the name to a node.
         * @param name Node name.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Get the current cost of a node.
         * @return Cost value.
         */
        public float getCost() {
            return cost;
        }

        /**
         * Set the current cost to the node.
         * @param cost Current cost.
         */
        public void setCost(float cost) {
            this.cost = cost;
        }
    }
}

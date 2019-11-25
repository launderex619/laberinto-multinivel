package com.example.sulemaia.Model;

import com.example.sulemaia.Helper.GraphViz;

import java.util.ArrayList;
import java.util.HashSet;

public class HeuristicPathTree {

    public static final int UNIFORM_COST = 0;
    public static final int FIRST_BEST = 1;
    public static final int A_STAR = 2;
    private Node anchor;
    private GraphViz graphVizHelper;
    private Node lastNode;

    public HeuristicPathTree(Node anchor) {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
        this.anchor = anchor;
        this.anchor.setFather(anchor);
    }

    public HeuristicPathTree() {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
    }

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
     * Create a step movement between father and child
     *
     * @param child final node
     */
    public void addMovementAccumulative(Node child) {
        graphVizHelper.addConectorWithLabel(child.getFather().getName(), child.getName(),
                String.valueOf(child.getAccumulative()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }

    /**
     * Create a step movement between father and child
     *
     * @param child final node
     */
    public void addMovementRemaining(Node child) {
        graphVizHelper.addConectorWithLabel(child.getFather().getName(), child.getName(),
                String.valueOf(child.getRemaining()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }

    /**
     * Create a step movement between father and child
     *
     * @param child final node
     */
    public void addMovementAll(Node child) {
        graphVizHelper.addConectorWithLabel(child.getFather().getName(), child.getName(),
                child.getAccumulative() + " + " + child.getRemaining() + " = " +
                        (child.getAccumulative() + child.getRemaining()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }


    public void endTree() {
        graphVizHelper.end_graph();
    }

    public String getDotTree() {
        return graphVizHelper.getDotSource();
    }

    public void setInitial(Node node) {
        graphVizHelper.setInitial(node.getName());
    }

    public void setFinal(Node node) {
        graphVizHelper.setFinal(node.getName());
    }

    public void setLastNode(Node lastNode) {
        this.lastNode = lastNode;

    }

    public Node getFinalNode() {
        return lastNode;
    }

    public Node getAnchor() {
        return anchor;
    }

    private void createDotTree(int expansionAlgorithm) {
        switch (expansionAlgorithm) {
            case UNIFORM_COST:
                inOrderFirst(anchor);
                break;
            case FIRST_BEST:
                inOrderFirst(anchor);
                break;
            case A_STAR:
                break;
        }
    }

    private void inOrderFirst(Node father) {
        for (Node child : father.getChildren()) {
            addMovementRemaining(child);
        }
        for (Node child : father.getChildren()) {
            inOrderFirst(child);
        }
    }

    public void drawDotTree(int expansionAlgorithm) {
        createDotTree(expansionAlgorithm);
    }

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

        public Node(String name, float cost, boolean accessible, int y, int x) {
            this.children = new ArrayList<>(4); //4 por que no puede tener mas de 4 hijos de momento
            this.accessible = accessible;
            this.step = new StringBuilder();
            this.name = name;
            this.cost = cost;
            this.accumulative = 0f;
            this.remaining = Float.MAX_VALUE;
            this.posY = y;
            this.posX = x;
        }

        public boolean isAccessible() {
            return this.accessible;
        }

        public boolean isFinal(int y, int x) {
            return (x == posX && y == posY);
        }

        public void setAccessible(boolean accessible) {
            this.accessible = accessible;
        }


        public void setStep(StringBuilder step) {
            this.step = step;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public float getAccumulative() {
            return accumulative;
        }

        public void setAccumulative(float accumulative) {
            this.accumulative = accumulative;
        }

        public float getRemaining() {
            return remaining;
        }

        public void setRemaining(float remaining) {
            this.remaining = remaining;
        }

        public ArrayList<Node> getChildren() {
            return children;
        }

        private void addChild(Node child) {
            child.setFather(this);
            this.children.add(child);
        }

        public Node getFather() {
            return father;
        }

        public void setFather(Node father) {
            this.father = father;
        }

        public String getStep() {
            return step.toString();
        }

        public void setStep(int step) {
            this.step.append(step);
            this.step.append(", ");
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }
    }
}

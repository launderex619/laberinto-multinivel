package com.example.sulemaia.Model;

import com.example.sulemaia.Helper.GraphViz;

import java.util.ArrayList;
import java.util.HashSet;

public class HeuristicPathTree {

    private Node anchor;
    private GraphViz graphVizHelper;

    public HeuristicPathTree(Node anchor) {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
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
        graphVizHelper.addConectorWithLabel(father.getName(), child.getName(),
                String.valueOf(father.getCost()));
        graphVizHelper.addNodeStep(father.getName(), father.getStep());
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
//        for(int i = 1; i < expansionOrder.size()-1; i++){
//            if(!expansionOrder.get(i-1).getChildren().contains(expansionOrder.get(i))){
//                expansionOrder.get(i).getFather().getChildren().remove(expansionOrder.get(i));
//                expansionOrder.get(i).setFather(expansionOrder.get(i-1));
//                expansionOrder.get(i-1).addChild(expansionOrder.get(i));
//            }
//        }
//        expansionOrder.get(expansionOrder.size()-1).getFather().getChildren().remove(expansionOrder.get(expansionOrder.size()-1));
//        expansionOrder.get(expansionOrder.size()-2).addChild(expansionOrder.get(expansionOrder.size()-1));
//        for(int i = 0; i < expansionOrder.size()-1; i++){
//            drawTree(expansionOrder.get(i));
//        }
//        graphVizHelper.addNodeStep(expansionOrder.get(expansionOrder.size()-1).getName(),
//                expansionOrder.get(expansionOrder.size()-1).getStep());
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

    public static class Node {
        private ArrayList<Node> children;
        private Node father;
        private StringBuilder step;
        private String name;
        private float cost;
        private int posY, posX;
        private float accumulative;
        private float remaining;

        public boolean isAccessible() {
            return accessible;
        }

        public boolean isFinal(int y, int x) {
            return (x == posX && y == posY);
        }

        public void setAccessible(boolean accessible) {
            this.accessible = accessible;
        }

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

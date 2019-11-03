package com.example.sulemaia.Model;

import com.example.sulemaia.Helper.GraphViz;

import java.util.ArrayList;
import java.util.HashSet;

public class PathTree {

    private Node anchor;
    private GraphViz graphVizHelper;
    private HashSet<Node> visitedNodes;

    public PathTree(Node anchor) {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
        this.anchor = anchor;
        this.anchor.setFather(anchor);
        visitedNodes = new HashSet<>();
        visitedNodes.add(anchor);
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
        visitedNodes.add(child);
        graphVizHelper.addConectorWithLabel(father.name, child.getName(),
                String.valueOf(child.getCost()));
        father.addChild(child);
    }

    /**
     * Create a step movement between father and child
     * @param father initial node
     * @param child final node
     */
    public void addMovement(Node father, Node child){
        graphVizHelper.addConectorWithLabel(father.name, child.getName(),
                String.valueOf(child.getCost()));
        graphVizHelper.addNodeStep(child.getName(), child.getStep());
    }

    public boolean exists(Node child) {
        return visitedNodes.contains(child);
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

    public static class Node {
        private ArrayList<Node> children;
        private Node father;
        private StringBuilder step;
        private String name;
        private float cost;

        public boolean isAccessible() {
            return accessible;
        }

        public void setAccessible(boolean accessible) {
            this.accessible = accessible;
        }

        private boolean accessible;

        public Node(String name, float cost, boolean accessible) {
            this.children = new ArrayList<>(4); //4 por que no puede tener mas de 4 hijos de momento
            this.accessible = accessible;
            this.step = new StringBuilder();
            this.name = name;
            this.cost = cost;
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

package com.example.sulemaia.Model;

import com.example.sulemaia.Helper.GraphViz;

public class PathTree {

    private Node anchor;
    private GraphViz graphVizHelper;
    private Node lastNode;

    public PathTree(Node anchor) {
        this.graphVizHelper = new GraphViz();
        this.graphVizHelper.start_graph();
        this.anchor = anchor;
        this.lastNode = anchor;
    }

    public void addLeft(Node node){
        //si ya poseo un nodo a la izquierda solo le agrego el paso en el que voy
        if(lastNode.getLeft() != null){
            graphVizHelper.addConectorWithLabel(lastNode.name, lastNode.getLeft().getName(), node.getStep());
            lastNode = lastNode.getLeft();
            lastNode.setStep(node.getStep());
        }
        else{
            graphVizHelper.addNodeCost(node.getName(), String.valueOf(node.getCost()));
            graphVizHelper.addConectorWithLabel(lastNode.name, node.getName(), node.getStep());
            lastNode.setLeft(node);
            node.setFather(lastNode);
            lastNode = lastNode.getLeft();
        }
    }
    public void addRight(Node node){
        //si ya poseo un nodo a la derecha solo le agrego el paso en el que voy
        if(lastNode.getRight() != null){
            graphVizHelper.addConectorWithLabel(lastNode.name, lastNode.getRight().getName(), node.getStep());
            lastNode = lastNode.getRight();
            lastNode.setStep(node.getStep());
        }
        else{
            graphVizHelper.addNodeCost(node.getName(), String.valueOf(node.getCost()));
            graphVizHelper.addConectorWithLabel(lastNode.name, node.getName(), node.getStep());
            lastNode.setRight(node);
            node.setFather(lastNode);
            lastNode = lastNode.getRight();
        }
    }
    public void addUp(Node node){
        //si ya poseo un nodo arriba solo le agrego el paso en el que voy
        if(lastNode.getUp() != null){
            graphVizHelper.addConectorWithLabel(lastNode.name, lastNode.getUp().getName(), node.getStep());
            lastNode = lastNode.getUp();
            lastNode.setStep(node.getStep());
        }
        else{
            graphVizHelper.addNodeCost(node.getName(), String.valueOf(node.getCost()));
            graphVizHelper.addConectorWithLabel(lastNode.name, node.getName(), node.getStep());
            lastNode.setUp(node);
            node.setFather(lastNode);
            lastNode = lastNode.getUp();
        }
    }
    public void addDown(Node node){
        //si ya poseo un nodo abajo solo le agrego el paso en el que voy
        if(lastNode.getDown() != null){
            graphVizHelper.addConectorWithLabel(lastNode.name, lastNode.getDown().getName(), node.getStep());
            lastNode = lastNode.getDown();
            lastNode.setStep(node.getStep());
        }
        else{
            graphVizHelper.addNodeCost(node.getName(), String.valueOf(node.getCost()));
            graphVizHelper.addConectorWithLabel(lastNode.name, node.getName(), node.getStep());
            lastNode.setDown(node);
            node.setFather(lastNode);
            lastNode = lastNode.getDown();
        }
    }

    public void endTree(){
        graphVizHelper.end_graph();
    }

    public String getDotTree(){
        return graphVizHelper.getDotSource();
    }

    public Node getLastNode(){
        return lastNode;
    }
    public Node getAnchor() {
        return anchor;
    }

    public void setAnchor(Node anchor) {
        this.anchor = anchor;
    }

    public static class Node{
        private Node up;
        private Node down;
        private Node left;
        private Node rigth;
        private Node father;
        private StringBuilder step;
        private String name;
        private float cost;

        public Node(String name, float cost, String step) {
            this.up = null;
            this.down = null;
            this.left = null;
            this.rigth = null;
            this.step = new StringBuilder(step);
            this.name = name;
            this.cost = cost;
        }

        private String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }

        private float getCost() {
            return cost;
        }

        private void setCost(float cost) {
            this.cost = cost;
        }

        private String getStep() {
            return step.toString();
        }

        private void setStep(String step) {
            this.step.append(", " + step);
        }

        private Node getUp() {
            return up;
        }

        private void setUp(Node up) {
            this.up = up;
        }

        private Node getDown() {
            return down;
        }

        private void setDown(Node down) {
            this.down = down;
        }

        private Node getLeft() {
            return left;
        }

        private void setLeft(Node left) {
            this.left = left;
        }

        private Node getRight() {
            return rigth;
        }

        private void setRight(Node rigth) {
            this.rigth = rigth;
        }

        private Node getFather() {
            return father;
        }

        private void setFather(Node father) {
            this.father = father;
        }
    }
}

package com.example.sulemaia.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * <dl>
 * <dt>Purpose: GraphViz Java API
 * <dd>
 *
 * <dt>Description:
 * <dd> With this Java class you can simply call dot
 * from your Java programs
 * <dt>Example usage:
 * <dd>
 * <pre>
 *    GraphViz gv = new GraphViz();
 *    gv.addln(gv.start_graph());
 *    gv.addln("A -> B;");
 *    gv.addln("A -> C;");
 *    gv.addln(gv.end_graph());
 *    System.out.println(gv.getDotSource());
 * </pre>
 * </dd>
 *
 * </dl>
 *
 * @author Laszlo Szathmary (<a href="jabba.laci@gmail.com">jabba.laci@gmail.com</a>)
 * @author Carlos Carvajal (Modificaciones al codigo original)
 * @author Alejandro Silva (Modificaciones al codigo original)
 * @version v0.1, 2003/12/04 (December) -- first release
 */
public class GraphViz {

    /**
     * The source of the graph written in dot language.
     */
    private StringBuilder graph = new StringBuilder();

    /**
     * Constructor: creates a new GraphViz object that will contain
     * a graph.
     */
    public GraphViz() {
    }

    /**
     * Returns the graph's source description in dot language.
     *
     * @return Source of the graph in dot language.
     */
    public String getDotSource() {
        return graph.toString();
    }

    /**
     * Adds a string to the graph's source (without newline).
     */
    public void add(String line) {
        graph.append(line);
    }

    /**
     * Adds a string to the graph's source (with newline).
     */
    public void addln(String line) {
        graph.append(line + "\n");
    }

    /**
     * Adds a newline to the graph's source.
     */
    public void addln() {
        graph.append('\n');
    }

    /**
     * Adds to a node the current given step
     * @param node Node to describe
     * @param step current step of the node that will be displayed
     */
    public void addNodeStep(String node, String step){
        add("\"" + node + "\"" + "[xlabel = \"" + step + "\"]");
        addln();
    }

    /**
     * Adds a conector from given node to given node adding a label in the conector
     * @param from Initial node
     * @param to Final node
     * @param cost Value to be placed
     */
    public void addConectorWithLabel(String from, String to, String cost){
        add("\"" + from +"\""+ " -> " + "\"" + to + "\"" + "[label = \"" + cost +  "\"]");
        addln();
    }

    /**
     * Returns a string that is used to start a graph.
     *
     * @return A string to open a graph.
     */
    public void start_graph() {
        add("strict digraph G {\nconcentrate=true\nforcelabels=true\n");
    }

    /**
     * Returns a string that is used to end a graph.
     *
     * @return A string to close a graph.
     */
    public void end_graph() {
        add("}");
    }

    /**
     * Changes the color of the initial node to green
     * @param name initial node name
     */
    public void setInitial(String name) {
        add("\"" + name +"\" [shape=circle, style=filled, fillcolor=green]");
        addln();
    }
    /**
     * Changes the color of the final node to red
     * @param name final node name
     */
    public void setFinal(String name) {
        add("\"" + name +"\" [shape=circle, style=filled, fillcolor=red]");
        addln();
    }
} // end of class GraphViz
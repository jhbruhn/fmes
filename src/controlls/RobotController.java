package controlls;


import graph.Graph;
import modell.Territorium;

import java.util.ArrayList;

public class RobotController {
    Graph graph;
    ArrayList<ArrayList<Territorium.Richtung>> robotMoves;

    public RobotController(Graph graph, ArrayList<ArrayList<Territorium.Richtung>> robotMoves) {
        this.graph = graph;
        this.robotMoves=robotMoves;
    }

    //Move the robot to the best possible position
    public void doNextStep() {
        //todo
        //run:
        nextStep();
    }

    //there is a chance to reach the goal
    public boolean isNotTerminated() {
        //todo
        return true;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }


    //calculate the next step
    public ArrayList<Territorium.Richtung>  nextStep() {
        //todo
        return null;
    }

}

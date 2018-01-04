package controlls;


import graph.Graph;

import javax.swing.text.Position;

public class RobotMovement {
    Enum lastStep;
    Graph graph;

    public RobotMovement(Graph graph) {
        this.graph = graph;
    }

    public void doNext() {
        runStep(nextStep());
    }

    public boolean isNotTerminated(){
        return true;
    }
    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void clearGraph(Position position) {

    }

    public Enum nextStep() {
        return null;
    }

    public void runStep(Enum nextstep) {
        //do nextStep
    }

    public Enum getLastStep() {
        return lastStep;
    }

    public void setLastStep(Enum lastStep) {
        this.lastStep = lastStep;
    }
}

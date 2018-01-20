package controlls;

import Util.ListToArrayList;
import Util.StateUtil;
import graph.*;
import modell.Roboter;
import modell.Territorium;

import java.util.ArrayList;

public class RobotController {
    private final Roboter roboter;
    Graph graph;
    ArrayList<Territorium.Richtung> robotMove;
    Territorium territorium;

    public RobotController(Roboter roboter, Territorium territorium) {
        this.roboter = roboter;
        this.territorium = territorium;
    }

    //Move the robot to the best possible position
    public void doNextMove() {
        robotMove = ListToArrayList.convert(graph.getNextRobotMove(graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true)));
        for (int i = 0; i < robotMove.size(); i++) {
            doNextStep(robotMove.get(i));
        }
    }

    public void doNextStep(Territorium.Richtung richtung) {
        while(roboter.isSleeping()) Thread.yield();
        roboter.bewege(richtung);
    }

    //there is a chance to reach the goal
    public boolean isSolvable() {
        System.out.println(graph.toDotString());
        if (territorium == null) {
            System.out.println("debug: territorium is null");
            return false;
        }
        if (graph == null) {
            System.out.println("debug: graph is null");
            return false;
        }
        if (StateUtil.getRobotPosition(territorium) == null) {
            System.out.println("debug: robotposition is null");
            return false;
        }
        if (graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true) == null) {
            System.out.println("debug: findState=null");
            return false;
        }
        if (!graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true).isSolvableFromHere()) {
            System.out.println("debug: not solvable");
            return false;
        }
        return true;
    }
    /* All goals from 1-k are reached.
    // Start a new run.
    public boolean isTerminated() {
        //todo
        return false;
    }*/

    //todo
    public boolean enoughEnergy() {
        return true;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public boolean isTerminated(int y, int x) {
        if(territorium.feldReiheRoboter==y&&territorium.feldSpalteRoboter==x){
            return true;
        }
        System.out.println("not terminated");
        return false;
    }
}

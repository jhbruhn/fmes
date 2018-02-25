package controlls;

import Util.ListToArrayList;
import Util.StateUtil;
import graph.Graph;
import graph.State;
import modell.Roboter;
import modell.Territorium;

import java.util.ArrayList;
import java.util.List;

public class RobotController {
    private final Roboter roboter;
    Graph graph;
    ArrayList<Territorium.Richtung> robotMove;
    Territorium territorium;
    List<Graph> enforcedBatteryGraphs;

    public RobotController(Roboter roboter, Territorium territorium) {
        this.roboter = roboter;
        this.territorium = territorium;
    }

    //Move the robot to the best possible position
    public void doNextMove() {
        robotMove = ListToArrayList.convert(graph.getNextRobotMove(graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true)));
        for (Territorium.Richtung aRobotMove : robotMove) {
            doNextStep(aRobotMove);
        }
        System.out.println(robotMove.size());
    }

    public void doNextStep(Territorium.Richtung richtung) {
        while (roboter.isSleeping()) Thread.yield();
        roboter.bewege(richtung);
    }

    //there is a chance to reach the goal
    public boolean isSolvable() {
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

        State s = graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true);

        if (s == null) {
            System.out.println("debug: findState=null");
            return false;
        }
        if (!s.isSolvableFromHere()) {
            System.out.println(graph.toDotString(s));

            System.out.println("debug: not solvable. Enforce=" + s.enforceValue);
            return false;
        }
        return true;
    }


    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public boolean isTerminated(int y, int x) {
        if (territorium.feldReiheRoboter == y && territorium.feldSpalteRoboter == x) {
            return true;
        }
        System.out.println("not terminated");
        return false;
    }

    public int getEnergyLevel() {
        return roboter.getTankFuellung();
    }
}

package controlls;
import Util.ListToArrayList;
import Util.StateUtil;
import graph.*;
import modell.Territorium;

import java.util.ArrayList;

public class RobotController {
    Graph graph;
    ArrayList<Territorium.Richtung> robotMove;
    Territorium territorium;

    public RobotController(Territorium territorium) {
        this.territorium=territorium;
    }

    //Move the robot to the best possible position
    public void doNextMove() {
        robotMove = ListToArrayList.convert(graph.getNextRobotMove(graph.findStateForPositions(StateUtil.getRobotPosition(territorium),StateUtil.getChildPosition(territorium))));
        for(int i = 0;i< robotMove.size();i++){

            doNextStep(robotMove.get(i));
        }
    }
    public void doNextStep(Territorium.Richtung richtung) {
        territorium.getRoboter().bewege(richtung);
    }
    //there is a chance to reach the goal
    public boolean isSolvable() {
        graph.findStateForPositions(StateUtil.getRobotPosition(territorium),StateUtil.getChildPosition(territorium)).isSolvableFromHere();
        return true;
    }
    /* All goals from 1-k are reached.
    // Start a new run.
    public boolean isTerminated() {
        //todo
        return false;
    }*/

    //todo
    public boolean enoughEnergy(){
        return true;
    }
    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}

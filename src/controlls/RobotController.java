package controlls;
import Util.ListToArrayList;
import graph.*;
import modell.Territorium;

import java.util.ArrayList;
import java.util.List;

public class RobotController {
    Graph graph;
    ArrayList<Territorium.Richtung> robotMove;
    Territorium territorium;

    public RobotController(Territorium territorium,Graph graph) {
        this.graph = graph;
        this.territorium=territorium;
    }

    //Move the robot to the best possible position
    public void doNextMove() {
        robotMove = ListToArrayList.convert(graph.getNextRobotMove(graph.findStateForPositions(getRobotPosition(),getChildPosition())));
        for(int i = 0;i< robotMove.size();i++){

            doNextStep(robotMove.get(i));
        }
    }
    public void doNextStep(Territorium.Richtung richtung) {
        territorium.getRoboter().bewege(richtung);
    }
    //there is a chance to reach the goal
    public boolean isSolvable() {
        graph.findStateForPositions(getRobotPosition(),getChildPosition()).isSolvableFromHere();
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


    public State getState(){
        boolean[][] walls = new boolean[territorium.getFeldHoehe()][territorium.getFeldBreite()];
        for (int y=0;y<walls.length;y++){
            for (int x =0;x<walls[y].length;x++){
                walls[y][x]=territorium.istNichtBesuchbar(y,x);
            }
        }
        Field field = new Field(walls);
        return new State(field, getRobotPosition(), getChildPosition(),true);
    }
    public Vector2 getRobotPosition(){
        return new Vector2(territorium.getFeldSpalteRoboter(),territorium.getFeldReiheRoboter());
    }
    public Vector2 getChildPosition(){
        return new Vector2(territorium.getFeldSpalteKind(),territorium.getFeldReiheKind());
    }
}

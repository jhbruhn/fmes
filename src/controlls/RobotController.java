package controlls;
import graph.*;
import modell.Territorium;

import java.util.ArrayList;
import java.util.List;

public class RobotController {
    Graph graph;
    ArrayList<ArrayList<Territorium.Richtung>> robotMoves;
    Territorium territorium;

    public RobotController(Territorium territorium,Graph graph, ArrayList<ArrayList<Territorium.Richtung>> robotMoves) {
        this.graph = graph;
        this.robotMoves = robotMoves;
        this.territorium=territorium;
    }

    //Move the robot to the best possible position
    public void doNextMove() {
        robotMoves = getConvertedMoves(getMovesFromGraph());

    }
    public void doNextStep(Territorium.Richtung richtung) {
        //todo

    }
    //there is a chance to reach the goal
    public boolean isSolvable() {
        //todo
        return true;
    }
    // All goals from 1-k are reached.
    // Start a new run.
    public boolean isTerminated() {
        //todo
        return false;
    }

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

    public ArrayList<ArrayList<Territorium.Richtung>> getConvertedMoves(List<Move> moves) {
        //todo

        return robotMoves;
    }
    public List<Move> getMovesFromGraph(){
        boolean[][] walls = new boolean[territorium.getFeldHoehe()][territorium.getFeldBreite()];
        for (int y=0;y<walls.length;y++){
            for (int x =0;x<walls[y].length;x++){
                walls[y][x]=territorium.istNichtBesuchbar(y,x);
            }
        }
        Field field = new Field(walls);
        Vector2 robotPos = new Vector2(territorium.getFeldSpalteRoboter(),territorium.getFeldReiheRoboter());
        Vector2 childPos = new Vector2(territorium.getFeldSpalteKind(),territorium.getFeldSpalteKind());
        State state=new State(field, robotPos, childPos,true);
        return graph.getNextMove(state);
    }
}

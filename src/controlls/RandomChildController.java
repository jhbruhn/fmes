package controlls;

import Util.ListListToArrayListList;
import Util.ListToArrayList;
import Util.StateUtil;
import graph.Graph;
import modell.Territorium;
import modell.Kind;

import java.util.ArrayList;
import java.util.Random;

public class RandomChildController {
    Kind kind;
    Territorium territorium;
    ArrayList<ArrayList<Territorium.Richtung>> childMoves;
    private Random randomGenerator;
    Graph graph;

    public RandomChildController(Kind kind, ArrayList<ArrayList<Territorium.Richtung>> childMoves) {
        this.kind = kind;
        this.territorium = kind.getTerritorium();
        this.childMoves = childMoves;
        randomGenerator = new Random();
    }

    public void doNextSteps() {
        int index;
        boolean success = false;
        while (!success) {
            ArrayList<ArrayList<Territorium.Richtung>> nextMoves = ListListToArrayListList.convert(graph.getNextChildMoves(graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium))));
            index = randomGenerator.nextInt(nextMoves.size());
            move(nextMoves.get(index));
            success = true;

        }
    }

    private void move(ArrayList<Territorium.Richtung> richtungen) {
        for (Territorium.Richtung richtung : richtungen) {
            territorium.getChild().bewege(richtung);
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}

package controlls;

import Util.ListListToArrayListList;
import Util.StateUtil;
import graph.Graph;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import modell.Kind;
import modell.Territorium;

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
        ArrayList<ArrayList<Territorium.Richtung>> nextMoves = ListListToArrayListList.convert(graph.getNextChildMoves(graph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), false)));
        if(!nextMoves.isEmpty()) {
            index = randomGenerator.nextInt(nextMoves.size());
            move(nextMoves.get(index));
        } else{
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Finished");
                alert.setHeaderText("Finished");
                alert.setContentText("The child can't move anymore");
                alert.showAndWait();
            });}

    }

    private void move(ArrayList<Territorium.Richtung> richtungen) {
        for (Territorium.Richtung richtung : richtungen) {
            while (territorium.getChild().isSleeping()) Thread.yield();
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

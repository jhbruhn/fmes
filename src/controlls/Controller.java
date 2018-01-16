package controlls;

import Util.ArrayListListToListList;
import graph.*;
import javafx.scene.control.Alert;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;
import modell.ZielFeld;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    Territorium territorium;
    Kind child;
    Roboter robot;
    RandomChildController randomChildController;
    RobotController robotController;
    ArrayList<ZielFeld> goalfield;
    Graph graph;

    public Controller(Territorium territorium) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = territorium.getRoboter();
        randomChildController = new RandomChildController(child, territorium.childMoves);
        this.goalfield = territorium.getZielFelder();
        Field f = new Field(getWalls(territorium));
        State initial = new State(f, new Vector2(territorium.feldReiheRoboter, territorium.feldSpalteRoboter), new Vector2(territorium.getFeldReiheKind(), territorium.getFeldSpalteKind()), true);
        List<List<Move>> robotMoves = ArrayListListToListList.convert(territorium.robotMoves);
        List<List<Move>> childMoves = ArrayListListToListList.convert(territorium.childMoves);
        graph = Graph.generateGraph(initial, robotMoves, childMoves);
        this.robotController = new RobotController(territorium);
        this.goalfield = territorium.getZielFelder();
    }

    public void run() {
        for (ZielFeld ziel : goalfield) {
            Graph enforcedGraph = graph.calculateEnforcedGraph(new Vector2(ziel.getReihe(), ziel.getSpalte()));
            robotController.setGraph(enforcedGraph);
            randomChildController.setGraph(enforcedGraph);
            while (robotController.isSolvable()) {
                robotController.doNextMove();
                randomChildController.doNextSteps();

            }
        }
        /*Graph enforcedGraph=graph.calculateEnforcedGraph(new Vector2(0,0));
        randomChildController.setGraph(enforcedGraph);
        while (true){
            randomChildController.doNextSteps();
        }*/

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Finished");
        alert.setHeaderText("Finished");
        alert.setContentText("We reached our goal./Problem is not solvable at this state.");
        alert.showAndWait();
    }


    public boolean[][] getWalls(Territorium territorium) {
        final boolean[][] WALLS = new boolean[territorium.getFeldHoehe() + 2][territorium.getFeldBreite() + 2];
        for (int hoehe = 0; hoehe < territorium.getFeldHoehe() + 2; hoehe++) {
            for (int breite = 0; breite < territorium.getFeldBreite() + 2; breite++) {
                if (hoehe == 0 || breite == 0 || hoehe == territorium.getFeldHoehe() + 1 || breite == territorium.getFeldBreite() + 1)
                    WALLS[hoehe][breite] = true;
                else
                    WALLS[hoehe][breite] = territorium.istNichtBesuchbar(hoehe, breite);
            }
        }
        return WALLS;
    }
}

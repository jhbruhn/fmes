package controlls;

import Util.ArrayListListToListList;
import graph.*;
import javafx.scene.control.Alert;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;
import modell.ZielFeld;

import java.util.ArrayList;

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
        State initial = new State(f, new Vector2(territorium.feldSpalteRoboter, territorium.getFeldReiheRoboter()), new Vector2(territorium.getFeldSpalteKind(), territorium.getFeldReiheKind()), true);
        graph = Graph.generateGraph(initial, ArrayListListToListList.convert(territorium.robotMoves), ArrayListListToListList.convert(territorium.childMoves));
        this.robotController = new RobotController(territorium);
        this.goalfield = territorium.getZielFelder();
    }

    public void run() {
        for(ZielFeld ziel:goalfield) {
            robotController.setGraph(graph.calculateEnforcedGraph(new Vector2(ziel.getReihe(),ziel.getSpalte())));
            while (robotController.isSolvable()) {
                robotController.doNextMove();
                randomChildController.doNextSteps();

            }
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Finished");
        alert.setHeaderText("Finished");
        alert.setContentText("We reached our goal./Problem is not solvable at this state.");
        alert.showAndWait();
    }


    public boolean[][] getWalls(Territorium territorium) {
        final boolean[][] WALLS = new boolean[territorium.getFeldHoehe()][territorium.getFeldBreite()];
        for (int hoehe = 0; hoehe < territorium.getFeldHoehe(); hoehe++) {
            for (int breite = 0; breite < territorium.getFeldBreite(); breite++) {
                WALLS[hoehe][breite] = territorium.istNichtBesuchbar(hoehe, breite);
            }
        }
        return WALLS;
    }
}

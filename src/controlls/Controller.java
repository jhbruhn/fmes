package controlls;

import Util.ArrayListListToListList;
import graph.Field;
import graph.Graph;
import graph.Move;
import graph.Vector2;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;
import modell.ZielFeld;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Thread {
    Territorium territorium;
    Kind child;
    Roboter robot;
    RandomChildController randomChildController;
    RobotController robotController;
    ArrayList<ZielFeld> goalfield;
    Graph graph;
    private boolean stopped = false;

    public Controller(Roboter r, Territorium territorium) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = r;
        randomChildController = new RandomChildController(child, territorium.childMoves);
        this.goalfield = territorium.getZielFelder();
        Field f = new Field(getWalls(territorium));
        graph.State initial = new graph.State(f, new Vector2(territorium.feldSpalteRoboter, territorium.feldReiheRoboter), new Vector2(territorium.getFeldSpalteKind(), territorium.getFeldReiheKind()), true);
        List<List<Move>> robotMoves = ArrayListListToListList.convert(territorium.robotMoves);
        List<List<Move>> childMoves = ArrayListListToListList.convert(territorium.childMoves);
        graph = Graph.generateGraph(initial, robotMoves, childMoves);
        this.robotController = new RobotController(r, territorium);
        this.goalfield = territorium.getZielFelder();
    }

    @Override
    public void run() {
        //System.out.println(graph.toDotString());
        while(true) {
            for (ZielFeld ziel : goalfield) {
                territorium.setNextGoalField(ziel);
                Graph enforcedGraph = graph.calculateEnforcedGraph(new Vector2(ziel.getSpalte(), ziel.getReihe()));
                robotController.setGraph(enforcedGraph);
                randomChildController.setGraph(enforcedGraph);
                while (robotController.isSolvable() && !robotController.isTerminated(ziel.getReihe(), ziel.getSpalte())) {
                    robotController.doNextMove();

                    randomChildController.doNextSteps();
                }
                if(!robotController.isSolvable()) break;
                if (stopped) return;
            }
            if(!robotController.isSolvable()) break;
        }
        /*Graph enforcedGraph=graph.calculateEnforcedGraph(new Vector2(0,0));
        randomChildController.setGraph(enforcedGraph);
        while (true){
            randomChildController.doNextSteps();
        }*/
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Finished");
            alert.setHeaderText("Finished");
            alert.setContentText("We reached our goal./Problem is not solvable at this state.");
            alert.showAndWait();
        });
    }


    public boolean[][] getWalls(Territorium territorium) {
        final boolean[][] walls = new boolean[territorium.getFeldHoehe()][territorium.getFeldBreite()];
        for (int hoehe = 0; hoehe < territorium.getFeldHoehe(); hoehe++) {
            for (int breite = 0; breite < territorium.getFeldBreite(); breite++) {
                /*if (hoehe == 0 || breite == 0 || hoehe == territorium.getFeldHoehe() + 1 || breite == territorium.getFeldBreite() + 1)
                    walls[hoehe][breite] = true;
                else*/
                    walls[hoehe][breite] = territorium.istNichtBesuchbar(hoehe, breite);
            }
        }
        return walls;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}

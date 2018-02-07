package controlls;

import Util.ArrayListListToListList;
import graph.Field;
import graph.Graph;
import graph.Move;
import graph.Vector2;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import modell.*;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Thread {
    Territorium territorium;
    Kind child;
    Roboter robot;
    RandomChildController randomChildController;
    RobotController robotController;
    ArrayList<ZielFeld> goalfields;
    ZielFeld nextBattery;
    List<Battery> enforcedBatteryGraphs;
    Graph graph;
    private boolean stopped = false;
    Field field;

    public Controller(Roboter r, Territorium territorium) {
        this.territorium = territorium;
        this.territorium.setTrankfuellungBeachten(true);
        this.child = territorium.getChild();
        this.robot = r;
        randomChildController = new RandomChildController(child, territorium.childMoves);
        this.goalfields = territorium.getZielFelder();
        this.field = new Field(getWalls(territorium));
        graph.State initial = new graph.State(field, new Vector2(territorium.feldSpalteRoboter, territorium.feldReiheRoboter), new Vector2(territorium.getFeldSpalteKind(), territorium.getFeldReiheKind()), true);
        List<List<Move>> robotMoves = ArrayListListToListList.convert(territorium.robotMoves);
        List<List<Move>> childMoves = ArrayListListToListList.convert(territorium.childMoves);
        graph = Graph.generateGraph(initial, robotMoves, childMoves);
        this.enforcedBatteryGraphs = generateEnforcedBatteryGraphs(graph, initial, robotMoves, childMoves);
        this.robotController = new RobotController(r, territorium);
        this.goalfields = territorium.getZielFelder();
    }

    @Override
    public void run() {
        while (true) {
            for (ZielFeld ziel : goalfields) {
                territorium.setNextGoalField(ziel);
                Graph enforcedGraph = graph.calculateEnforcedGraph(new Vector2(ziel.getSpalte(), ziel.getReihe()));
                randomChildController.setGraph(enforcedGraph);

                while (robotController.isSolvable() && !robotController.isTerminated(ziel.getReihe(), ziel.getSpalte())) {

                    if (!enoughEnergy(enforcedGraph)) {
                        if (nextBattery != null) {
                            Graph enforcedBatteryGraph = graph.calculateEnforcedGraph(new Vector2(nextBattery.getSpalte(), nextBattery.getReihe()));
                            robotController.setGraph(enforcedBatteryGraph);
                        } else {
                            showNoTargetError();
                        }
                    } else {
                        robotController.setGraph(enforcedGraph);
                    }

                    robotController.doNextMove();

                    randomChildController.doNextSteps();
                }
                if (!robotController.isSolvable()) break;
                if (stopped) return;
            }
            if (!robotController.isSolvable()) break;
        }
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Finished");
            alert.setHeaderText("Finished");
            alert.setContentText("We reached our goal./Problem is not solvable at this state.");
            alert.showAndWait();
        });
    }

    private void showNoTargetError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Problem");
            alert.setHeaderText("Problem");
            alert.setContentText("We can't reached our goal, because of battery level.");
            alert.showAndWait();
        });
    }


    public boolean[][] getWalls(Territorium territorium) {
        final boolean[][] walls = new boolean[territorium.getFeldHoehe()][territorium.getFeldBreite()];
        for (int hoehe = 0; hoehe < territorium.getFeldHoehe(); hoehe++) {
            for (int breite = 0; breite < territorium.getFeldBreite(); breite++) {
                walls[hoehe][breite] = territorium.istNichtBesuchbar(hoehe, breite);
            }
        }
        return walls;
    }

    private List<Battery> generateEnforcedBatteryGraphs(Graph graph, graph.State initial, List<List<Move>> robotMoves, List<List<Move>> childMoves) {
        List<Battery> batteryList = new ArrayList<>();
        for (ZielFeld battery : territorium.getBatterieFelder()) {
            batteryList.add(new Battery(battery, graph.calculateEnforcedGraph(new Vector2(battery.getSpalte(), battery.getReihe()))));
        }
        return batteryList;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean enoughEnergy(Graph robotGraph) {
        graph.State state = new graph.State(field, new Vector2(territorium.feldSpalteRoboter, territorium.feldReiheRoboter), new Vector2(territorium.getFeldSpalteKind(), territorium.getFeldReiheKind()), true);
        int bestWorstEnergyPath = enforcedBatteryGraphs.get(0).getEnforcedGraph().getLongestPathToTarget(state);
        Battery next = enforcedBatteryGraphs.get(0);

        for (int i = 1; i < enforcedBatteryGraphs.size(); i++) {
            int cost = enforcedBatteryGraphs.get(i).getEnforcedGraph().getLongestPathToTarget(state);
            if (bestWorstEnergyPath > cost) {
                next = enforcedBatteryGraphs.get(i);
                bestWorstEnergyPath = cost;
            }
        }

        nextBattery = next.getBattery();

        if (bestWorstEnergyPath + robotGraph.getLongestPathToTarget(state) <= robotController.getEnergyLevel()) {
            return true;
        } else {
            System.out.println("Not enough energy! Going to battery");
            return false;
        }
    }//todo nextBattery
}

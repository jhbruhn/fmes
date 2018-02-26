package controlls;

import Util.ArrayListListToListList;
import Util.StateUtil;
import graph.Field;
import graph.Graph;
import graph.Move;
import graph.Vector2;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import modell.*;
import views.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Thread {
    private Alert dialog;
    Territorium territorium;
    Kind child;
    Roboter robot;
    RandomChildController randomChildController;
    RobotController robotController;
    ArrayList<ZielFeld> goalfields;
    List<Battery> batteries;
    Graph graph;
    private boolean stopped = false;
    Field field;

    public Controller(Roboter r, Territorium territorium) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = r;
        randomChildController = new RandomChildController(child, territorium.childMoves);
        this.goalfields = territorium.getZielFelder();
        this.field = new Field(getWalls(territorium));
        for (ZielFeld feld : territorium.getBatterieFelder())
            this.field.loadingStations.add(new Vector2(feld.getSpalte(), feld.getReihe()));


        this.robotController = new RobotController(r, territorium);
        this.goalfields = territorium.getZielFelder();
        Platform.runLater(() -> {
            this.dialog = WaitingDialog.getWaitingDialog();
        });

    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            this.dialog.show();
        });

        graph.State initial = new graph.State(field, new Vector2(territorium.feldSpalteRoboter, territorium.feldReiheRoboter), new Vector2(territorium.getFeldSpalteKind(), territorium.getFeldReiheKind()), true);
        List<List<Move>> robotMoves = ArrayListListToListList.convert(territorium.robotMoves);
        List<List<Move>> childMoves = ArrayListListToListList.convert(territorium.childMoves);
        graph = Graph.generateGraph(initial, robotMoves, childMoves);
        this.batteries = generateEnforcedBatteryGraphs(graph);

        try {
            Platform.runLater(dialog::hide);
            Platform.runLater(dialog::hide);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            for (ZielFeld ziel : goalfields) {
                System.out.println("New Target calculating graph!");
                territorium.setNextGoalField(ziel);
                Graph enforcedGraph = graph.calculateEnforcedGraph(new Vector2(ziel.getSpalte(), ziel.getReihe()));
                randomChildController.setGraph(enforcedGraph);
                robotController.setGraph(enforcedGraph);
                while (!stopped && robotController.isSolvable() && !robotController.isTerminated(ziel.getReihe(), ziel.getSpalte())) {
                    calculateEnergyDemands(enforcedGraph, new Vector2(ziel.getSpalte(), ziel.getReihe()));

                    System.out.println("Doing Robot Move");
                    robotController.doNextMove();
                    System.out.println("Doing Child Move");
                    randomChildController.doNextSteps();
                }
                if (!robotController.isSolvable()) break;
            }
            if(stopped) return;
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

    private boolean isVisitable(Vector2 pos) {
        return !territorium.istNichtBesuchbar(pos.y, pos.x);
    }

    private List<Battery> generateEnforcedBatteryGraphs(Graph graph) {
        List<Battery> batteryList = new ArrayList<>();
        for (ZielFeld battery : territorium.getBatterieFelder()) {
            Vector2 pos = new Vector2(battery.getSpalte(), battery.getReihe());
            Vector2 posT;
            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++)
                    if (isVisitable(posT = pos.add(x, y)))
                        batteryList.add(new Battery(battery, graph.calculateEnforcedGraph(posT), posT));
        }
        return batteryList;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void calculateEnergyDemands(Graph targetGraph, Vector2 targetLocation) {
        graph.State state = targetGraph.findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true);

        // robot - > target
        int longestPathToTarget = targetGraph.getCostToTarget(state);


        //state = batteries.get(0).getEnforcedGraph().findStateForPositions(targetLocation, StateUtil.getChildPosition(territorium), true);

        // Will be: target -> battery
        int bestWorstEnergyPath = Integer.MAX_VALUE;


        for (Battery bat : batteries) {
            List<graph.State> states = bat.getEnforcedGraph().findStatesForRobotPosition(targetLocation);
            for (graph.State s : states) {
                if (s.enforceValue != -1 && s.isRobotState) { // Ignore bad enforce values.
                    int cost = bat.getEnforcedGraph().getCostToTarget(s);

                    if (bestWorstEnergyPath > cost) {
                        bestWorstEnergyPath = cost;
                    }
                }
            }
        }

        System.out.println("BestWorstEnergyPath: " + bestWorstEnergyPath);
        System.out.println("LongestPathToTarget: " + longestPathToTarget);
        System.out.println("Sum: " + (bestWorstEnergyPath + longestPathToTarget));

        // robot -> target -> battery <= energy
        boolean canGoToTarget = bestWorstEnergyPath + longestPathToTarget <= robotController.getEnergyLevel();

        if (canGoToTarget) {
            System.out.println("Going to Target");
            robotController.setGraph(targetGraph);
            randomChildController.setGraph(targetGraph); // Should not make a difference, but lets keep it consistent
        } else {
            // We cannot go to the target and then to a battery, so we go to the battery closest to the target

            // Get all batteries we can reach
            List<Battery> reachableBatteries = new ArrayList<>();

            for (Battery bat : batteries) {
                graph.State bState = bat.getEnforcedGraph().findStateForPositions(StateUtil.getRobotPosition(territorium), StateUtil.getChildPosition(territorium), true);
                if (bState.enforceValue != -1) {
                    int cost = bat.getEnforcedGraph().getCostToTarget(bState);
                    if (cost <= robotController.getEnergyLevel()) {
                        reachableBatteries.add(bat);
                    }
                }
            }

            // Now choose the battery which is closest to the target
            Battery closestBat = null;
            int closestCost = 0;
            for (Battery bat : reachableBatteries) {

                List<graph.State> bStates = targetGraph.findStatesForRobotPosition(bat.position);
                for (graph.State bState : bStates) {
                    if (bState.enforceValue != -1) {
                        int cost = targetGraph.getCostToTarget(bState);
                        if (closestBat == null || closestCost > cost) {
                            closestBat = bat;
                            closestCost = cost;
                        }
                    }
                }

            }

            if (closestBat == null) {
                System.out.println("No Battery found!");
                showNoTargetError();
            } else {

                System.out.println("Going to Battery");
                robotController.setGraph(closestBat.getEnforcedGraph());
                randomChildController.setGraph(closestBat.getEnforcedGraph()); // Should not make a difference, but let's keep it consistent.
            }
        }
    }
}

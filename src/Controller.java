import controlls.RandomChildController;
import controlls.RobotController;
import graph.*;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;

import java.util.ArrayList;

public class Controller {
    Territorium territorium;
    Kind child;
    Roboter robot;
    RandomChildController randomChildController;
    RobotController robotController;
    //ArrayList<Goal> goals;
    ArrayList<ArrayList<Territorium.Richtung>> childMoves;
    ArrayList<ArrayList<Territorium.Richtung>> robotMoves;

    public Controller(Territorium territorium, ArrayList<ArrayList<Territorium.Richtung>> childMoves, ArrayList<ArrayList<Territorium.Richtung>> robotMoves) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = territorium.getRoboter();
        randomChildController = new RandomChildController(child, childMoves);
        robotController = new RobotController(null, robotMoves);
        //goals = territorium.getGoals();
    }

    public void run() {
        //todo
        while (true) {
            randomChildController.doNextStep();
        }
    }
}

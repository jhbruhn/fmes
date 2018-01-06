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

    public Controller(Territorium territorium) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = territorium.getRoboter();
        //randomChildController = new RandomChildController(child, territorium.getChildMoves());
        //robotController = new RobotController(null, territorium.getChildMoves());
        //goals = territorium.getGoals();
    }

    public void run() {
        //todo
        while (true) {
            randomChildController.doNextStep();
        }
    }
}

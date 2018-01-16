import controlls.RandomChildController;
import controlls.RobotController;
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

    public Controller(Territorium territorium) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = territorium.getRoboter();
        randomChildController = new RandomChildController(child, territorium.childMoves);
        this.goalfield = territorium.getZielFelder();

        Graph graph = null;//todo
        this.robotController = new RobotController(territorium);
        this.goalfield = territorium.getZielFelder();
    }

    public void run() {
        if(robotController.isSolvable()) {
            //todo
            while (true) {
                randomChildController.doNextSteps();
            }
        } else{
            //todo
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ErrorHeader");
            alert.setContentText("ErrorText");
            alert.showAndWait();
        }
    }
}

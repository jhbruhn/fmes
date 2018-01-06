import controlls.RandomChildController;
import controlls.RobotController;
import graph.*;
import javafx.scene.control.Alert;
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
        //randomChildController = new RandomChildController(child, territorium.chi);
        //robotController = new RobotController(null, territorium.getChildMoves());
        //goals = territorium.getGoals();
    }

    public void run() {
        if(robotController.isSolvable()) {
            //todo
            while (true) {
                randomChildController.doNextStep();
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

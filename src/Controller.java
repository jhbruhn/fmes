/*import controlls.RandomChildController;
import controlls.RobotMovement;
import graph.*;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;

public class Controller {
    Territorium territorium;
    Kind child;
    Roboter robot;
    RandomChildController randomChildController;

    public Controller(Territorium territorium, RobotMoves robotMoves, ChildMoves childMoves) {
        this.territorium = territorium;
        this.child = territorium.getChild();
        this.robot = territorium.getRoboter();
        randomChildController = new RandomChildController(child, childMoves);
    }

    public void run() {
        while (true) {
            randomChildMovement.doNextStep();
        }
    }

    /*

        Territorium territorium = new Territorium();
        Kind kind = new Kind(territorium);
        //Roboter roboter = new Roboter(territorium);
        //Field f = new Field(createWalls(territorium));
        //State initial = new State(f, new Vector2(1, 1), new Vector2(4, 4), true);
        //Graph g = null;//=generateGraph(initial);
        //RobotMovement robotMovement = new RobotMovement(g);
        RandomChildMovement randomChildMovement = new RandomChildMovement(kind,territorium);
        /*while(robotMovement.isNotTerminated()) {
            robotMovement.doNextStep();
            randomChildMovement.doNextStep();
        }

    }*/

//}

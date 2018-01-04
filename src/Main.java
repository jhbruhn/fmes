import controlls.RandomChildMovement;
import controlls.RobotMovement;
import controlls.Start;
import graph.*;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;

import java.util.List;
import java.util.Stack;

public class Main {
    private static final boolean[][] WALLS = new boolean[][]{
            {true, true, true, true, true, true},
            {true, false, false, false, false, true},
            {true, false, false, false, false, true},
            {true, false, false, false, false, true},
            {true, false, false, false, false, true},
            {true, true, true, true, true, true}};

    public static void main(String[] args) {
        Territorium territorium = new Territorium();
        Kind kind = new Kind(territorium);
        Roboter roboter = new Roboter(territorium);
        Field f = new Field(WALLS);
        State initial = new State(f, new Vector2(1, 1), new Vector2(4, 4), true);
        Graph g = null;//=generateGraph(initial);
        RobotMovement robotMovement = new RobotMovement(g);
        RandomChildMovement randomChildMovement = new RandomChildMovement(kind,territorium);
        Start.launch();
        while(robotMovement.isNotTerminated()) {
            robotMovement.doNext();
            System.out.println("Robot step: " + robotMovement.getLastStep());
            randomChildMovement.doNextStep();
        }
        System.out.println("Done");

    }

}

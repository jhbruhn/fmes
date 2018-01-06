import controlls.RandomChildMovement;
import controlls.RobotMovement;
import graph.*;
import modell.Kind;
import modell.Roboter;
import modell.Territorium;

public class Main {
    public static void main(String[] args) {
        Territorium territorium = new Territorium();
        Kind kind = new Kind(territorium);
        Roboter roboter = new Roboter(territorium);
        Field f = new Field(createWalls(territorium));
        State initial = new State(f, new Vector2(1, 1), new Vector2(4, 4), true);
        Graph g = null;//=generateGraph(initial);
        RobotMovement robotMovement = new RobotMovement(g);
        RandomChildMovement randomChildMovement = new RandomChildMovement(kind,territorium);
        while(robotMovement.isNotTerminated()) {
            robotMovement.doNextStep();
            randomChildMovement.doNextStep();
        }

    }

    private static boolean[][] createWalls(Territorium territorium) {
        final boolean[][] WALLS = new boolean[territorium.getFeldBreite()][territorium.getFeldHoehe()];

        return WALLS;
    }

}

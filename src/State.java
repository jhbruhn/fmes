import java.util.ArrayList;
import java.util.List;

public class State {
    public Vector2 robot;
    public Vector2 child;
    Field field;
    boolean isRobotState; // True if it is the robots turn, false when not

    public State(Field field, Vector2 robot, Vector2 child, boolean isRobotState) {
        this.field = field;
        this.robot = robot;
        this.child = child;
        this.isRobotState = isRobotState;
    }

    public List<Transition> generateRobotMoves() {
        if (!isRobotState) throw new RuntimeException("Cannot create robot moves from nonrobotstate");

        List<Transition> transitions = new ArrayList<>();
        for (Move m : Move.values()) {
            if (isMovePossible(m, true)) {
                State s = new State(field, robot.add(m.directionVector), child, false);
                Transition t = new Transition(this, m, s);
                transitions.add(t);
            }
        }


        return transitions;
    }

    public List<Transition> generateChildMoves() {
        if (isRobotState) throw new RuntimeException("Cannot create child moves from robotstate");

        List<Transition> transitions = new ArrayList<>();
        for (Move m : Move.values()) {
            if (isMovePossible(m, false)) {
                State s = new State(field, robot, child.add(m.directionVector), true);
                Transition t = new Transition(this, m, s);
                transitions.add(t);
            }
        }
        return transitions;
    }

    public boolean isMovePossible(Move move, boolean isRobot) {
        Vector2 position = isRobot ? robot : child;
        // TODO: Add Child here.
        return !field.isWallAt(position.add(move.directionVector));
    }

    public boolean equals(State other) {
        return other.robot.equals(this.robot) && other.child.equals(this.child) && other.isRobotState == this.isRobotState;
    }
}

package graph;

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

        // Check whether the new position will be in a wall
        Vector2 position = isRobot ? robot : child;

        if(field.isWallAt(position.add(move.directionVector)))
            return false;

        // Check whether the child and robot would be too close to each other
        Vector2 posA, posB;
        if(isRobot) {
            posA = robot.add(move.directionVector);
            posB = child;
        } else {
            posA = robot;
            posB = child.add(move.directionVector);
        }
        double distance = posA.sub(posB).abs();
        if(distance <= 1) return false;

        // Check whether the child would be too close to a charging station
        if(!isRobot) {
            for(Vector2 station : field.loadingStations) {
                distance = child.add(move.directionVector).sub(station).abs();
                if(distance <= 1) return false;
            }
        }


        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State other = (State) o;
        return other.robot.equals(this.robot) && other.child.equals(this.child) && other.isRobotState == this.isRobotState;
    }

    public List<Transition> generateNextStates() {
        if(isRobotState) {
            return generateRobotMoves();
        } else {
            return generateChildMoves();
        }
    }

    public String toString() {
        return robot + "; " + child + " " + (isRobotState ? "r" : "e");
    }
}

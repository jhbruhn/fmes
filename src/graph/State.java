package graph;

import java.util.ArrayList;
import java.util.List;

public class State implements Cloneable {
    public Vector2 robot;
    public Vector2 child;
    Field field;
    public boolean isRobotState; // True if it is the robots turn, false when not
    public int enforceValue = -1;

    public State(Field field, Vector2 robot, Vector2 child, boolean isRobotState) {
        this.field = field;
        this.robot = robot;
        this.child = child;
        this.isRobotState = isRobotState;
    }

    public List<Transition> generateRobotMoves(List<List<Move>> movePossibilities) {
        if (!isRobotState) throw new RuntimeException("Cannot create robot moves from nonrobotstate");

        List<Transition> transitions = new ArrayList<>();
        for (List<Move> sequence : movePossibilities) {
            State state = this;
            boolean valid = true;
            for(Move m : sequence) {
                if (state.isMovePossible(m, true)) {
                    state = new State(state.field, state.robot.add(m.directionVector), state.child, false);
                } else {
                    valid = false;
                    break;
                }
            }
            if(!valid) {
                continue;
            }

            Transition t = new Transition(this, sequence, state);
            transitions.add(t);
        }


        return transitions;
    }

    public List<Transition> generateChildMoves(List<List<Move>> movePossibilities) {
        if (isRobotState) throw new RuntimeException("Cannot create child moves from robotstate");


        List<Transition> transitions = new ArrayList<>();
        for (List<Move> sequence : movePossibilities) {
            State state = this;
            boolean valid = true;
            for(Move m : sequence) {
                if (state.isMovePossible(m, false)) {
                    state = new State(state.field, state.robot, state.child.add(m.directionVector), true);
                } else {
                    valid = false;
                }
            }
            if(!valid) continue;

            Transition t = new Transition(this, sequence, state);
            transitions.add(t);
        }
        return transitions;
    }

    public boolean isMovePossible(Move move, boolean isRobot) {

        // Check whether the new position will be in a wall
        Vector2 position = isRobot ? robot : child;
        Vector2 newPos = position.add(move.directionVector);
        if(field.isWallAt(newPos))
            return false;


        // Check whether the child and robot would be too close to each other
        Vector2 posA;
        Vector2 posB;
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
        return other.robot.equals(this.robot) && other.child.equals(this.child) && other.isRobotState == this.isRobotState && this.enforceValue == other.enforceValue;
    }

    public String toString() {
        return robot + "; " + child + " " + "\\n" + enforceValue;
    }

    public List<Transition> generateNextStates(List<List<Move>> movePossibilities) {
        if(isRobotState) {
            return generateRobotMoves(movePossibilities);
        } else {
            return generateChildMoves(movePossibilities);
        }
    }

    public State clone() {
        return new State(this.field, this.robot, this.child, this.isRobotState);
    }

    public boolean isSolvableFromHere() {
        return enforceValue >= 0;
    }
}

package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Graph {
    public State initialState;
    public List<State> states = new ArrayList<>();
    public List<Transition> transitions = new ArrayList<>();

    public String toDotString() {
        StringBuilder s = new StringBuilder("digraph G {\n");

        for(State state : states) {
            s.append("\"");
            s.append(state);
            s.append("\" [shape=");
            s.append(state.isRobotState ? "oval" : "box");
            s.append("]\n");
        }

        for(Transition t : transitions) {
            s.append("\"");
            s.append(t.from);
            s.append("\" -> \"");
            s.append(t.to);
            s.append("\" [ label=\"");
            s.append(t.move.toString());
            s.append("\"]\n");
        }
        s.append("}");
        return s.toString();
    }

    public List<Transition> getTransitionsFrom(State state) {
        List<Transition> ts = new ArrayList<>();
        for(Transition t : transitions) {
            if(t.from.equals(state))
                ts.add(t);
        }
        return ts;
    }

    public List<Transition> getTransitionsTo(State state) {
        List<Transition> ts = new ArrayList<>();
        for(Transition t : transitions) {
            if(t.to.equals(state))
                ts.add(t);
        }
        return ts;
    }

    public State findStateForPositions(Vector2 robotPosition, Vector2 childPosition) {
        for(State state : states) {
            if(state.robot.equals(robotPosition) && state.child.equals(childPosition))
                return state;
        }
        return null;
    }

    public List<State> findStatesForRobotPosition(Vector2 robotPosition) {
        List<State> foundStates = new ArrayList<>();
        for(State state : states) {
            if(state.robot.equals(robotPosition))
                foundStates.add(state);
        }
        return foundStates;
    }

    public void calculateEnforce(Vector2 robotTarget) {
        // TODO: Maybe move
        // Clear enforce values of all states.
        for(State s : states) {
            s.enforceValue = -1;
        }

        List<State> targetStates = findStatesForRobotPosition(robotTarget);

        // Set enforce value to 0 for targetstates.
        for(State s : targetStates)
            s.enforceValue = 0;

        // TODO: Go from here and calculate more enforce values for 1-acceptance
        // TODO: Eliminate Transitions for Buechi-Acceptance
    }

    public static Graph generateGraph(State initial) {
        Graph g = new Graph();

        g.initialState = initial;
        g.states.add(initial);

        Stack<State> statesToGenerate = new Stack<>();
        statesToGenerate.add(initial);

        while (!statesToGenerate.empty()) {
            State currentState = statesToGenerate.pop();

            List<Transition> transitions = currentState.generateNextStates();

            // Check whether we already have that state in our set.
            for (Transition t : transitions) {
                boolean newState = true;
                for (State s : g.states) {
                    if (s.equals(t.to)) {
                        newState = false;
                        t.to = s;
                        break;
                    }

                }
                if (newState) {
                    g.states.add(t.to);
                    statesToGenerate.push(t.to);
                }

                if(!g.transitions.contains(t)) {
                    g.transitions.add(t);
                }
            }
        }


        return g;
    }
}

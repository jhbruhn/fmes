package graph;

import java.util.*;

public class Graph implements Cloneable {
    public State initialState;
    public List<State> states = new ArrayList<>();
    public List<Transition> transitions = new ArrayList<>();

    public String toDotString() {
        StringBuilder s = new StringBuilder("digraph G {\n");

        for (State state : states) {
            s.append("\"");
            s.append(state);
            s.append("\" [shape=");
            s.append(state.isRobotState ? "oval" : "box");
            s.append("]\n");
        }

        for (Transition t : transitions) {
            s.append("\"");
            s.append(t.from);
            s.append("\" -> \"");
            s.append(t.to);
            s.append("\" [ label=\"");
            for (Move m : t.moves)
                s.append(m.encodedString);
            s.append("\"]\n");
        }
        s.append("}");
        return s.toString();
    }

    public List<Transition> getTransitionsFrom(State state) {
        List<Transition> ts = new ArrayList<>();
        for (Transition t : transitions) {
            if (t.from.equals(state))
                ts.add(t);
        }
        return ts;
    }

    public List<Transition> getTransitionsTo(State state) {
        List<Transition> ts = new ArrayList<>();
        for (Transition t : transitions) {
            if (t.to.equals(state))
                ts.add(t);
        }
        return ts;
    }

    public State findStateForPositions(Vector2 robotPosition, Vector2 childPosition, boolean isRobotState) {
        System.out.println(robotPosition + "; " + childPosition);
        for (State state : states) {
            if (state.robot.equals(robotPosition) && state.child.equals(childPosition) && state.isRobotState == isRobotState)
                return state;
        }
        return null;
    }

    public List<State> findStatesForRobotPosition(Vector2 robotPosition) {
        List<State> foundStates = new ArrayList<>();
        for (State state : states) {
            if (state.robot.equals(robotPosition))
                foundStates.add(state);
        }
        return foundStates;
    }

    public Graph calculateEnforcedGraph(Vector2 robotTarget) {
        // We create a copy of this graph to apply the EnforcePlus Algorithm
        // We first set all target states (states where the robot is at the desired position)
        // to a Enforced value of 0. We then "recursively" apply the enforce values to the
        // states leading to the last calculated state with a value one higher than the current value.

        Graph g = this.clone();
        // Clear enforce values of all states.
        for (State s : g.states) {
            s.enforceValue = -1;
        }

        List<State> targetStates = g.findStatesForRobotPosition(robotTarget);
        System.out.println(robotTarget);

        // Set enforce value to 0 for targetstates.
        for (State s : targetStates)
            s.enforceValue = 0;

        // Go from here and calculate more enforce values for 1-acceptance
        Deque<State> calcStack = new ArrayDeque<>(targetStates);

        while (!calcStack.isEmpty()) {
            State enfI = calcStack.pop();

            List<Transition> transitionsTo = g.getTransitionsTo(enfI);
            for (Transition t : transitionsTo) {
                State enfIP1 = t.from;
                if (enfIP1.enforceValue == -1) {
                    enfIP1.enforceValue = enfI.enforceValue + 1;
                    calcStack.push(enfIP1);
                }
            }
        }

        // Eliminate Transitions for Buechi-Acceptance

        List<State> unreachableStates = new ArrayList<>();

        // Remove all states with an enforce value of -1, as the cannot be reached
        Iterator<State> it = g.states.iterator();
        while (it.hasNext()) {
            State s = it.next();
            if (s.enforceValue == -1) {
                it.remove();
                unreachableStates.add(s);
            }
        }

        g.transitions.removeIf(t -> unreachableStates.contains(t.from) || unreachableStates.contains(t.to));

        return g;
    }

    public List<Transition> getViableTransitionsFromState(State state) {
        List<Transition> viableMoves = new ArrayList<>();
        List<Transition> transitionsFrom = getTransitionsFrom(state);

        for (Transition t : transitionsFrom) {
            if (t.to.enforceValue < state.enforceValue) {
                viableMoves.add(t);
            }
        }

        return viableMoves;
    }

    public List<List<Move>> getNextChildMoves(State state) {
        if (state.isRobotState) throw new RuntimeException("You cannot do always do all moves from a robotstate.");
        List<List<Move>> moves = new ArrayList<>();

        List<Transition> transitionsFrom = getTransitionsFrom(state);

        for (Transition t : transitionsFrom) {
            moves.add(t.moves);
        }

        return moves;
    }

    public List<Move> getNextRobotMove(State state) {
        if (!state.isRobotState) throw new RuntimeException("You cannot do a move from a childstate.");
        List<Transition> viableMoves = getViableTransitionsFromState(state);
        viableMoves.sort(Comparator.comparingInt(o -> o.to.enforceValue));
        if (viableMoves.isEmpty()) return Collections.emptyList();
        return viableMoves.get(0).moves;
    }

    public int getCostToTarget(State from) {
        if (from == null) return -1;
        if (from.enforceValue == -1) throw new RuntimeException("You cannot calculate a path on an unenforced graph");
        System.out.println("Calculating cost");
        int cost = 0;
        State state = from;
        while (state.enforceValue != 0) {
            List<Transition> transitions = getTransitionsFrom(state);
            if (transitions.isEmpty()) {
                return Integer.MAX_VALUE;
            }

            State newState = null;
            Transition transition = null;

            for (Transition t : transitions) {
                if (newState == null || t.to.enforceValue < newState.enforceValue) {
                    transition = t;
                    newState = t.to;
                }
            }

            state = newState;
            if (state == null) return Integer.MAX_VALUE;

            if (!state.isRobotState) {
                System.out.println(transition.moves);

                for (Move move : transition.moves) {
                    System.out.println(move.energyUse);
                    cost += move.energyUse;
                }
            }
        }

        System.out.println("Calculated cost: " + cost);


        return cost;
    }


    public Graph clone() {
        Graph g = new Graph();
        g.states = new ArrayList<>();
        g.transitions = new ArrayList<>();
        for (State s : this.states)
            g.states.add(s.clone());
        for (Transition t : this.transitions) {
            State from = null, to = null;
            for (State s : g.states) {
                if (s.equals(t.from))
                    from = s;
                if (s.equals(t.to))
                    to = s;
            }
            Transition tNew = new Transition(from, t.moves, to);
            g.transitions.add(tNew);
        }

        return g;
    }

    public static Graph generateGraph(State initial, List<List<Move>> robotMoves, List<List<Move>> childMoves) {
        Graph g = new Graph();

        g.initialState = initial;
        g.states.add(initial);

        Deque<State> statesToGenerate = new ArrayDeque<>();
        statesToGenerate.add(initial);

        while (!statesToGenerate.isEmpty()) {
            State currentState = statesToGenerate.pop();

            List<Transition> transitions = currentState.generateNextStates(currentState.isRobotState ? robotMoves : childMoves);

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

                if (!g.transitions.contains(t)) {
                    g.transitions.add(t);
                }
            }
        }


        return g;
    }
}

package graph;

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
        Field f = new Field(WALLS);
        State initial = new State(f, new Vector2(1, 1), new Vector2(4, 4), true);
        Graph g = generateGraph(initial);
        System.out.println(g.toDotString());
    }

    public static Graph generateGraph(State initial) {
        Graph g = new Graph();

        g.initialState = initial;

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
            }
            g.transitions.addAll(transitions);
        }


        return g;
    }
}

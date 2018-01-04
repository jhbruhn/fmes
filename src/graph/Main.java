package graph;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final boolean[][] WALLS = new boolean[][] {
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

        List<State> statesToGenerate = new ArrayList<>();
        statesToGenerate.add(initial);

        while(statesToGenerate.size() > 0) {
            List<State> newStates = new ArrayList<>();
            for(State currentState : statesToGenerate) {
                List<Transition> transitions;

                transitions = currentState.generateMoves();

                // Check whether we already have that state in our set.
                for(Transition t : transitions) {
                    boolean newState = true;
                    for(State s : g.states) {
                        if(s.equals(t.to)) {
                            newState = false;
                            t.to = s;
                            break;
                        }
                    }
                    if(newState) {
                        g.states.add(t.to);
                        newStates.add(t.to);
                    }
                }
                g.transitions.addAll(transitions);
            }
            statesToGenerate.clear();
            statesToGenerate.addAll(newStates);
        }


        return g;
    }
}

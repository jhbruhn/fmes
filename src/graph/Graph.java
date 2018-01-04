package graph;

import java.util.ArrayList;
import java.util.List;

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
}

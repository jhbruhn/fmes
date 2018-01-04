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
        f.loadingStations.add(new Vector2(2, 2));
        f.loadingStations.add(new Vector2(2, 4));
        State initial = new State(f, new Vector2(1, 1), new Vector2(4, 4), true);
        Graph g = Graph.generateGraph(initial);
        System.out.println(g.toDotString());
    }
}

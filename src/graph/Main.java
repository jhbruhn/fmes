package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final boolean[][] WALLS = new boolean[][]{
            {true, true,  true,  true,  true,  true},
            {true, false, false, false, false, true},
            {true, false, false, false, false, true},
            {true, false, false, false, false, true},
            {true, false, false, false, false, true},
            {true, true,  true,  true,  true,  true}};

    public static void main(String[] args) {
        Field f = new Field(WALLS);
        //f.loadingStations.add(new Vector2(2, 2));
        //f.loadingStations.add(new Vector2(2, 4));

        List<List<Move>> robotMoves = new ArrayList<>();
        robotMoves.add(Arrays.asList(Move.LEFT, Move.RIGHT, Move.UP, Move.UP));
        robotMoves.add(Arrays.asList(Move.NOTHING));
        robotMoves.add(Arrays.asList(Move.DOWN));
        robotMoves.add(Arrays.asList(Move.RIGHT));
        robotMoves.add(Arrays.asList(Move.UP));
        robotMoves.add(Arrays.asList(Move.RIGHT, Move.RIGHT, Move.UP));
        robotMoves.add(Arrays.asList(Move.RIGHT, Move.UP));

        List<List<Move>> childMoves = new ArrayList<>();
        childMoves.add(Arrays.asList(Move.DOWN, Move.LEFT, Move.RIGHT, Move.UP));
        childMoves.add(Arrays.asList(Move.LEFT, Move.RIGHT, Move.LEFT, Move.RIGHT));
        childMoves.add(Arrays.asList(Move.LEFT, Move.DOWN));
        childMoves.add(Arrays.asList(Move.RIGHT, Move.DOWN));
        childMoves.add(Arrays.asList(Move.UP, Move.LEFT));

        State initial = new State(f, new Vector2(1, 1), new Vector2(4, 4), true);
        Graph g = Graph.generateGraph(initial, robotMoves, childMoves);
        //System.out.println(g.toDotString());
        g = g.calculateEnforcedGraph(new Vector2(4, 4));
        //System.out.println(g.toDotString());
    }
}

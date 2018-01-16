package Util;

import graph.Move;
import modell.Territorium;

import java.util.ArrayList;
import java.util.List;

public class ArrayListToList {
    public static List<List<Move>> convert(ArrayList<ArrayList<Territorium.Richtung>> childMoves) {
        List<List<Move>> list = new ArrayList<>();
        List<Move> move = new ArrayList<>();
        for(int y =0;y<childMoves.size();y++){
            for(int x=0;x<childMoves.get(y).size();x++){
                move.add(Move.values()[childMoves.get(y).get(x).ordinal()]);
            }
            list.add(move);
        }
        return list;
    }
}

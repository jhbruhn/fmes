package Util;

import graph.Move;
import modell.Territorium;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sven
 */
public class ListListToArrayListList {
    public static ArrayList<ArrayList<Territorium.Richtung>> convert(List<List<Move>> moveFromGraph) {
        ArrayList<ArrayList<Territorium.Richtung>> moves = new ArrayList<>();
        ArrayList<Territorium.Richtung> move = new ArrayList<>();
        for (List<Move> list:moveFromGraph) {
            for(Move graphMove:list){
                move.add(Territorium.Richtung.values()[graphMove.ordinal()]);
            }
            moves.add(move);
            move.clear();
        }
        return moves;
    }
}

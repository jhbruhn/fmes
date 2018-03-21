package Util;

import graph.Move;
import modell.Territorium;

import java.util.ArrayList;
import java.util.List;

public class ListToArrayList {
    public static ArrayList<Territorium.Richtung> convert(List<Move> moveFromGraph) {
        ArrayList<Territorium.Richtung> arrayList= new ArrayList<>();
        for(Move move:moveFromGraph){
            arrayList.add(Territorium.Richtung.values()[move.ordinal()]);
        }
        return arrayList;
    }
}

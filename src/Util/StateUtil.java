package Util;

import graph.Field;
import graph.State;
import graph.Vector2;
import modell.Territorium;

public class StateUtil {
    public static State getState(Territorium territorium){
        boolean[][] walls = new boolean[territorium.getFeldHoehe()][territorium.getFeldBreite()];
        for (int y=0;y<walls.length;y++){
            for (int x =0;x<walls[y].length;x++){
                walls[y][x]=territorium.istNichtBesuchbar(y,x);
            }
        }
        Field field = new Field(walls);
        return new State(field, getRobotPosition(territorium), getChildPosition(territorium),true);
    }
    public static Vector2 getRobotPosition(Territorium territorium){
        return new Vector2(territorium.getFeldSpalteRoboter(),territorium.getFeldReiheRoboter());
    }
    public static Vector2 getChildPosition(Territorium territorium){
        return new Vector2(territorium.getFeldSpalteKind(),territorium.getFeldReiheKind());
    }
}

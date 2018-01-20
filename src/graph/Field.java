package graph;

import java.util.ArrayList;
import java.util.List;

public class Field {
    public boolean[][] walls;
    public List<Vector2> loadingStations = new ArrayList<>();

    public Field(boolean[][] walls) {
        this.walls = walls;
    }

    public boolean isWallAt(Vector2 pos) {
        return pos.y < 0 || pos.x < 0 || pos.x >= walls[0].length || pos.y >= walls.length || walls[pos.y][pos.x];

    }
}

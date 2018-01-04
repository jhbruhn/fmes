import java.util.ArrayList;
import java.util.List;

public class Field {
    public boolean[][] walls;
    public List<Vector2> targets = new ArrayList<>();
    public List<Vector2> loadingStations = new ArrayList<>();

    public Field(boolean[][] walls) {
        this.walls = walls;
    }

    public boolean isWallAt(Vector2 pos) {
        return walls[pos.y][pos.x];
    }
}

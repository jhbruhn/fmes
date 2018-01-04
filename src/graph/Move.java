package graph;

public enum Move {
    UP(new Vector2(0, -1)),
    DOWN(new Vector2(0, 1)),
    LEFT(new Vector2(-1, 0)),
    RIGHT(new Vector2(1, 0)),
    NOTHING(new Vector2(0, 0));

    public final Vector2 directionVector;

    Move(Vector2 directionVector) {
        this.directionVector = directionVector;
    }
}

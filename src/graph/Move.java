package graph;

public enum Move {
    UP(new Vector2(0, -1), "u"),
    DOWN(new Vector2(0, 1), "d"),
    LEFT(new Vector2(-1, 0), "l"),
    RIGHT(new Vector2(1, 0), "r"),
    NOTHING(new Vector2(0, 0), "e");

    public final Vector2 directionVector;
    public final String encodedString;

    Move(Vector2 directionVector, String encodedString) {
        this.directionVector = directionVector;
        this.encodedString = encodedString;
    }
}

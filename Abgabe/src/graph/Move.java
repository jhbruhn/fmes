package graph;

public enum Move {
    UP(new Vector2(0, -1), "u", 1),
    DOWN(new Vector2(0, 1), "d", 1),
    LEFT(new Vector2(-1, 0), "l", 1),
    RIGHT(new Vector2(1, 0), "r", 1),
    NOTHING(new Vector2(0, 0), "e", 0);

    public final Vector2 directionVector;
    public final String encodedString;
    public final int energyUse;

    Move(Vector2 directionVector, String encodedString, int energyUse) {
        this.directionVector = directionVector;
        this.encodedString = encodedString;
        this.energyUse = energyUse;
    }
}

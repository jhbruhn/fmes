public class Vector2 {
    public final int x;
    public final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(int x, int y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 v) {
        return add(v.x, v.y);
    }

    public Vector2 sub(int x, int y) {
        return new Vector2(this.x - x, this.y - y);
    }

    public Vector2 sub(Vector2 v) {
        return sub(v.x, v.y);
    }
}

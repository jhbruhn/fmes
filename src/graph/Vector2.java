package graph;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public double abs() {
        return Math.sqrt(x * x + y * y);
    }
}

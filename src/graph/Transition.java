package graph;

import java.util.List;

public class Transition {
    public State from;
    public State to;
    public List<Move> moves;

    public Transition(State from, List<Move> moves, State to) {
        this.from = from;
        this.to = to;
        this.moves = moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transition that = (Transition) o;

        if(that.from.equals(that.to) && this.from.equals(this.to))
            return that.moves.equals(this.moves);

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        return moves.equals(that.moves);
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (moves != null ? moves.hashCode() : 0);
        return result;
    }
}

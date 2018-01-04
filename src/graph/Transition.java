package graph;

public class Transition {
    public State from;
    public State to;
    public Move move;

    public Transition(State from, Move move, State to) {
        this.from = from;
        this.to = to;
        this.move = move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transition that = (Transition) o;

        if(that.from.equals(that.to) && this.from.equals(this.to))
            return that.move == this.move;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        return move == that.move;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (move != null ? move.hashCode() : 0);
        return result;
    }
}

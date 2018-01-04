public class Transition {
    public State from;
    public State to;
    public Move move;

    public Transition(State from, Move move, State to) {
        this.from = from;
        this.to = to;
        this.move = move;
    }
}

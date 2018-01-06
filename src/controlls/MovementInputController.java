package controlls;

import graph.Move;
import javafx.scene.control.TextField;
import modell.Territorium;

import java.util.ArrayList;

public class MovementInputController {
    public MovementInputController(Territorium t, TextField robotInput, TextField childInput) {
        robotInput.textProperty().addListener((observable, oldValue, newValue) -> {
            t.robotMoves = parseMoves(newValue);
        });
        childInput.textProperty().addListener((observable, oldValue, newValue) -> {
            t.childMoves = parseMoves(newValue);
        });
        t.robotMoves = parseMoves(robotInput.getText());
        t.childMoves = parseMoves(childInput.getText());
    }

    private ArrayList<ArrayList<Territorium.Richtung>> parseMoves(String text) {
        ArrayList<ArrayList<Territorium.Richtung>> moves = new ArrayList<>();
        String[] moveThings = text.split(",");
        for(String m : moveThings) {
            ArrayList<Territorium.Richtung> moveThing = new ArrayList<>();
            for(int i = 0; i < m.length(); i++) {
                switch(m.charAt(i)) {
                    case 'l':
                        moveThing.add(Territorium.Richtung.LEFT);
                        break;
                    case 'r':
                        moveThing.add(Territorium.Richtung.RIGHT);
                        break;
                    case 'u':
                        moveThing.add(Territorium.Richtung.UP);
                        break;
                    case 'd':
                        moveThing.add(Territorium.Richtung.DOWN);
                        break;
                    case 'e':
                        moveThing.add(Territorium.Richtung.EPSILON);
                        break;
                }
            }
            moves.add(moveThing);
        }

        System.out.println(moves.size());

        return moves;
    }
}

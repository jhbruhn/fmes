package controlls;

import modell.Territorium;
import modell.Kind;

import java.util.ArrayList;

public class RandomChildController {
    Kind kind;
    Territorium territorium;
    ArrayList<ArrayList<Territorium.Richtung>> childMoves;

    public RandomChildController(Kind kind, ArrayList<ArrayList<Territorium.Richtung>> childMoves) {
        this.kind = kind;
        this.territorium = kind.getTerritorium();
        this.childMoves = childMoves;
    }

    public void doNextStep() {
        //todo
        int zufallszahl = (int) ((4 * Math.random()) % 4);
        boolean success = false;
        while (!success) {
            switch (zufallszahl) {
                case 0://Ein Schritt nach links.
                    if (!territorium.istNichtBesuchbar(territorium.getFeldReiheKind(), territorium.getFeldSpalteKind() - 1)) {
                        kind.bewege(Territorium.Richtung.LEFT);
                        success = true;
                    }
                case 1://Ein Schritt nach rechts.
                    if (!territorium.istNichtBesuchbar(territorium.getFeldReiheKind(), territorium.getFeldSpalteKind() + 1)) {
                        kind.bewege(Territorium.Richtung.RIGHT);
                        success = true;
                    }
                case 2://Ein Schritt nach oben.
                    if (!territorium.istNichtBesuchbar(territorium.getFeldReiheKind() - 1, territorium.getFeldSpalteKind())) {
                        kind.bewege(Territorium.Richtung.UP);
                        success = true;
                    }
                case 3://Ein Schritt nach unten.
                    if (!territorium.istNichtBesuchbar(territorium.getFeldReiheKind() + 1, territorium.getFeldSpalteKind())) {
                        kind.bewege(Territorium.Richtung.DOWN);
                        success = true;
                    }
                case 4://Auf der Stelle stehen.
                    success = true;
            }
        }
    }

}

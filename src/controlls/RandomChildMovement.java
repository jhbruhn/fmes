package controlls;

import modell.Territorium;
import modell.Kind;

public class RandomChildMovement {
    Kind kind;
    Territorium territorium;

    public RandomChildMovement(Kind kind, Territorium territorium) {
        this.kind = kind;
        this.territorium = territorium;
    }

    public void doNextStep() {
        int zufallszahl = (int) ((4 * Math.random()) % 4);
        boolean success=false;
        while (!success) {
            switch (zufallszahl) {
                //todo Kind gegen Spielfeldende
                case 0:
                    if (territorium.felsenDa(territorium.getFeldReiheKind(),territorium.getFeldSpalteKind()-1 )){
                    kind.bewege(Territorium.Richtung.LEFT);
                    success = true;
                }
                case 1:
                    if (true){
                        kind.bewege(Territorium.Richtung.RIGHT);
                        success = true;
                    }
                case 2:
                    if (territorium.felsenDa(territorium.getFeldReiheKind()-1,territorium.getFeldSpalteKind())){
                        kind.bewege(Territorium.Richtung.UP);
                        success = true;
                }
                case 3:
                    if (territorium.felsenDa(territorium.getFeldReiheKind()+1,territorium.getFeldSpalteKind())){
                        kind.bewege(Territorium.Richtung.DOWN);
                        success = true;
                }
                case 4:
                    success = true;
            }
        }
    }

}

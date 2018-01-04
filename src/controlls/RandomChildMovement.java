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
                case 0:
                    if (territorium.felsenDa(territorium.getFeldReiheKind(),territorium.getFeldSpalteKind()-1 )){
                    kind.linksBewegen();
                    success = true;
                }
                case 1:
                    if (territorium.felsenDa(territorium.getFeldReiheKind(),territorium.getFeldSpalteKind()+1 )){
                        kind.rechtsBewegen();
                        success = true;
                    }
                case 2:
                    if (territorium.felsenDa(territorium.getFeldReiheKind()-1,territorium.getFeldSpalteKind())){
                        kind.vorFahren();
                        success = true;
                }
                case 3:
                    if (territorium.felsenDa(territorium.getFeldReiheKind()+1,territorium.getFeldSpalteKind())){
                        kind.rueckFahren();
                        success = true;
                }
                case 4:
                    success = true;
            }
        }
    }

}

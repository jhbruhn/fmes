package controlls;

import modell.Territorium;
import modell.Kind;

import java.util.ArrayList;
import java.util.Random;

public class RandomChildController {
    Kind kind;
    Territorium territorium;
    ArrayList<ArrayList<Territorium.Richtung>> childMoves;
    private Random randomGenerator;

    public RandomChildController(Kind kind, ArrayList<ArrayList<Territorium.Richtung>> childMoves) {
        this.kind = kind;
        this.territorium = kind.getTerritorium();
        this.childMoves = childMoves;
        randomGenerator = new Random();
    }

    public void doNextSteps() {
        int index;
        boolean success = false;
        while (!success) {
            index = randomGenerator.nextInt(childMoves.size());
            if(isPossibleMoves(childMoves.get(index))){
                move(childMoves.get(index));
                success=true;
            }
        }
    }

    private  void move(ArrayList<Territorium.Richtung> richtungen) {
        for(Territorium.Richtung richtung:richtungen){
            territorium.getChild().bewege(richtung);
        }
    }

    private boolean isPossibleMoves(ArrayList<Territorium.Richtung> moves) {
        int x=territorium.getFeldSpalteKind();
        int y=territorium.getFeldReiheKind();
        for (Territorium.Richtung richtung:moves) {
            switch (richtung) {
                case UP:
                    y-=1;
                case DOWN:
                    y+=1;
                    break;
                case LEFT:
                    x-=1;
                case RIGHT:
                    x+=1;
                    break;
                case EPSILON:
                    break;
            }
            if(territorium.istNichtBesuchbar(y,x)) return false;
        }

        return true;
    }

}

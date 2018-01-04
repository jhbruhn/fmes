public class RandomChildMovement {
    Kind kind;
    Terretorium terretorium;

    public RandomChildMovement(Kind kind, Terretorium terretorium) {
        this.kind = kind;
        this.terretorium = terretorium;
    }

    public void doNextStep() {
        int zufallszahl = (int) ((4 * Math.random()) % 4);
        boolean success;
        while (!success) {
            switch (zufallszahl) {
                case 0:
                    if (terretorium.isFree(terretorium.getChildPosition() - (1, 0))){//bullshit
                    kind.goLeft();
                    success = true;
                }
                case 1:
                    if (terretorium.isFree(terretorium.getChildPosition() + (1, 0)) {//bullshit
                        kind.goRight();
                        success = true;
                    }
                case 2:
                    if (terretorium.isFree(terretorium.getChildPosition() - (0, 1))){//bullshit
                    kind.goUp();
                    success = true;
                }
                case 3:
                    if (terretorium.isFree(terretorium.getChildPosition() + (0, 1))){//bullshit
                    kind.goDown();
                    success = true;
                }
                case 4:
                    kind.stayStill();
                    success = true;
            }
        }
    }

}
}

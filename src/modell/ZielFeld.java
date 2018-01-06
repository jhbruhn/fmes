package modell;

/**
 * Created by Raskur Sevenflame on 06.01.2018.
 */
public class ZielFeld {
    private int reihe;
    private int spalte;

    public ZielFeld(int reihe, int spalte){
        this.reihe = reihe;
        this.spalte = spalte;
    }

    public int getReihe() {
        return reihe;
    }

    public int getSpalte() {
        return spalte;
    }
}

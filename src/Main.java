import modell.Kind;
import modell.Roboter;
import modell.Territorium;

public class Main {
    public static void main(String[] args) {
        Territorium territorium = new Territorium();
        Kind kind = new Kind(territorium);
        Roboter roboter = new Roboter(territorium);
    }
}

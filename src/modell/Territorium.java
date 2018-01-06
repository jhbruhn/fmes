package modell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dbZugriffe.SelectStatements;
import javafx.scene.control.Alert;
import javafx.scene.media.AudioClip;

@XmlRootElement(name = "territorium")
public class Territorium extends Observable implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int MIN_WIDTH = 2;
    public static final int MIN_HEIGTH = 1;
    public static final int MAX_WIDTH = 64;
    public static final int MAX_HEIGTH = 64;
    public static final int OBJ_WIDTH = 50;
    public static final int OBJ_MIN_WIDTH = 30;

    public static enum FeldEigenschaft implements java.io.Serializable {
        Leer, Batterie, Felsen, Location
    }

    public static enum Richtung implements java.io.Serializable {
        UP, DOWN, LEFT, RIGHT, EPSILON
    }

    public ArrayList<ArrayList<Richtung>> robotMoves;
    public ArrayList<ArrayList<Richtung>> childMoves;

    private double objectResizedWidth = Territorium.OBJ_WIDTH;
    private double objectResizedHeigth = Territorium.OBJ_WIDTH;

    private boolean resizeable = true;

    private boolean levelGeschafft = false;

    private int feldHoehe = 10;
    private int feldBreite = 19;

    public int feldReiheRoboter = 4;
    public int feldSpalteRoboter = 4;
    public int feldReiheKind = 4;
    public int feldSpalteKind = 3;

    private int territoriumSpecialFinishNummer;

    private int startReiheRoboter;
    private int startReiheKind;
    private int startSpalteRoboter;
    private int startSpalteKind;
    private FeldEigenschaft[][] startFeld;
    private boolean start = true;

    private double entfernungBreiteDesSpielFeldes;
    private double entfernungHoeheDesSpielFeldes;

    private transient Roboter roboter;

    private transient Kind child;

    private FeldEigenschaft[][] feld;

    private boolean notify = true;

    private boolean death = false;

    private ZielFeld nextGoalField;

    private ArrayList<ZielFeld> zielFelder = new ArrayList<ZielFeld>();

    private boolean trankfuellungBeachten = false;

    private int startTankfuellung = 100;

    /*
     * bastelt das Teritorium, nach den default werten
     */
    public Territorium() {
        setFeld(new FeldEigenschaft[feldHoehe][feldBreite]);
        for (int i = 0; i < getFeld().length; i++) {
            Arrays.fill(getFeld()[i], FeldEigenschaft.Leer);
        }
        setTerritoriumSpecialFinishNummer(0);
        erstelleBeispielFeld();
        roboter = new Roboter(this);
        child = new Kind(this);
    }

    /*
     * erstellt das immer geladene Beispielfeld
     */
    private void erstelleBeispielFeld() {
        setzeObjektAufsFeld(3, 3, FeldEigenschaft.Felsen, false);
        setzeObjektAufsFeld(3, 4, FeldEigenschaft.Batterie, false);
        setzeObjektAufsFeld(3, 5, FeldEigenschaft.Location, false);
    }

    /*
     * Ein �berladener Konstruktor, zum erstellen von
     */
    public void ladeTerritorium(FeldEigenschaft[][] feldLaden, int reiheUboot, int spalteUboot) {
        setFeld(feldLaden);
        feldReiheRoboter = reiheUboot;
        feldSpalteRoboter = spalteUboot;
        feldReiheKind = reiheUboot;
        feldSpalteKind = spalteUboot - 1;
    }

    /*
     * Soll sp�ter mal, wenn das Spiel zu ende ist, ein folgelevel laden, falls
     * dieses angegeben wurde
     */
    private void ladeTerritoriumDurchNachFolgelevel(int nachfolgeLevelNachDiesem) {
        if (getRoboter().isRunning()) {
            getRoboter().setStopped(true);
        }
        SelectStatements.getSpecialFinishTerritorium(nachfolgeLevelNachDiesem, this);
    }

    /*
     * pr�ft ob das U-Boot noch im gegebenen Feld ist
     */
    public void checkUBoot() {
        if (feldReiheRoboter < 0 || feldReiheRoboter >= feldHoehe || feldSpalteRoboter < 0 || feldSpalteRoboter >= feldBreite
                || feldReiheKind < 0 || feldReiheKind >= feldHoehe || feldSpalteKind < 0
                || feldSpalteKind >= feldBreite) {
            getFeld()[0][0] = FeldEigenschaft.Leer;
            getFeld()[0][1] = FeldEigenschaft.Leer;
            feldReiheRoboter = 0;
            feldSpalteRoboter = 1;
            feldReiheKind = 0;
            feldSpalteKind = 0;
        }
        setChanged();
        notifyObservers();
    }

    /*
     * checkt nicht wirklich, sondern schickt einen notify raus, dass sich das
     * Tutorium ge�ndert hat. Generelle Methode welche bei �nderungen aufgerufen
     * wird
     */
    public void checkIfChanged() {
        if (isNotify()) {
            setChanged();
            notifyObservers();
        }
    }

    /*
     * Kopiert das Territorium was �bergeben wird in das jetzige, dieses wird
     * besonders f�r das Laden von Datein benutzt (also serialisieren usw.)
     */
    public void copyTerritoriumIntoThis(Territorium territorium) {
        this.setEntfernungBreiteDesSpielFeldes(territorium.getEntfernungBreiteDesSpielFeldes());
        this.setEntfernungHoeheDesSpielFeldes(territorium.getEntfernungHoeheDesSpielFeldes());
        this.setFeld(territorium.getFeld());
        this.setFeldBreite(territorium.getFeldBreite());
        this.setFeldHoehe(territorium.getFeldHoehe());
        this.setLevelGeschaft(false);
        this.setObjectResizedHeigth(territorium.getObjectResizedHeigth());
        this.setObjectResizedWidth(territorium.getObjectResizedWidth());
        this.setResizeable(territorium.isResizeable());
        this.setStartReiheRoboter(territorium.getStartReiheRoboter());
        this.setStartReiheKind(territorium.getStartReiheKind());
        this.setStartSpalteRoboter(territorium.getStartSpalteRoboter());
        this.setStartSpalteKind(territorium.getStartSpalteKind());
        this.setTerritoriumSpecialFinishNummer(territorium.getTerritoriumSpecialFinishNummer());
        this.feldReiheRoboter = territorium.feldReiheRoboter;
        this.feldReiheKind = territorium.feldReiheKind;
        this.feldSpalteRoboter = territorium.feldSpalteRoboter;
        this.feldSpalteKind = territorium.feldSpalteKind;
        this.setStartFeld(this.getFeld());
        checkIfChanged();
    }

    /*
     * gibt false zur�ck wenn es nicht ok, dass das U-Boot auf ein Feld gesetzt
     * wird, beim Feld erstellen. Es ist nicht ok, wenn ein Felsen darauf ist
     */
    public boolean setzeUBootAufsFeldAbfrage(int reihe, int spalte) {
        if (spalte >= 0 && getFeld()[reihe][spalte] != FeldEigenschaft.Felsen
                && getFeld()[reihe][spalte] != FeldEigenschaft.Felsen
                && (reihe != feldReiheKind && spalte != feldSpalteKind)) {
            return true;
        }
        return false;
    }

    /*
     * Setzt das U-Boot auf ein Feld, wenn das m�glich ist (also das Feld nicht
     * besetzt ist mit einem Felsen oder nicht vorhanden)
     */
    public void setzeUBootAufsFeld(int reihe, int spalte) {
        if (setzeUBootAufsFeldAbfrage(reihe, spalte)) {
            feldReiheRoboter = reihe;
            feldSpalteRoboter = spalte;
        }
        checkIfChanged();
    }

    /*
     * Setzt ein m�gliches Objekt auf das Spielfeld.
     */
    public void setzeObjektAufsFeld(int reihe, int spalte, FeldEigenschaft f, boolean child) {
        if (reihe < 0 || reihe >= feldHoehe || spalte < 0 || spalte >= feldBreite) {
            throw new IndexOutOfBoundsException();
        }
        if (child) {
            feldReiheKind = reihe;
            feldSpalteKind = spalte;
        } else {
            if (f == FeldEigenschaft.Location) {
                ZielFeld z = new ZielFeld(reihe, spalte);
                zielFelder.add(z);
                getFeld()[reihe][spalte] = f;
                if (nextGoalField == null) {
                    nextGoalField = z;
                }
            }
            if (f == FeldEigenschaft.Batterie && (!(feldReiheRoboter == reihe && feldSpalteRoboter == spalte))) {
                getFeld()[reihe][spalte] = f;
            }

            if (f == FeldEigenschaft.Leer) {
                getFeld()[reihe][spalte] = f;
                for (int k = 0; k < getZielFelder().size(); k++) {
                    if (getZielFelder().get(k).getReihe() == reihe
                            && getZielFelder().get(k).getSpalte() == spalte) {

                        if (getNextGoalField().getReihe() == reihe &&
                                getNextGoalField().getSpalte() == spalte) {
                            if (getZielFelder().size() > 1) {
                                setNextGoalField(getZielFelder().get((k + 1) % getZielFelder().size()));
                            } else {
                                setNextGoalField(null);
                            }
                        }
                        getZielFelder().remove(getZielFelder().get(k));
                    }
                }
            }

            if (f == FeldEigenschaft.Felsen) {
                if (!((feldReiheRoboter == reihe && feldSpalteRoboter == spalte)
                        || (feldReiheKind == reihe && feldSpalteKind == spalte))) {
                    getFeld()[reihe][spalte] = f;
                }
            }
        }
        checkIfChanged();
    }

    /*
     * erstellt ein neues Teritorium, mit der gew�hlten Spalten- und
     * Reihenanzahl
     */

    public void changeSize(int reihen, int spalten) {
        feldHoehe = reihen;
        feldBreite = spalten;
        FeldEigenschaft[][] feldAlt = getFeld();
        setFeld(new FeldEigenschaft[feldHoehe][feldBreite]);
        for (int i = 0; i < getFeld().length; i++) {
            Arrays.fill(getFeld()[i], FeldEigenschaft.Leer);
        }
        fuelleFeld(feldAlt);
        checkUBoot();
    }

    /*
     * fuellt das Feld mit einem �bergebenen 2 Dimensionalen Array aus
     * FeldEigenschaften. Dabei werden Reihen und Spalten die nicht existieren
     * �bersprungen
     */
    public void fuelleFeld(FeldEigenschaft[][] feldAlt) {
        for (int i = 0; i < feldAlt.length; i++) {
            for (int j = 0; j < feldAlt[i].length; j++) {
                if (i < getFeld().length && j < getFeld()[i].length) {
                    getFeld()[i][j] = feldAlt[i][j];
                }
            }
        }
    }

    /*
     * gibt an ob eine Batterie unter dem U-Boot ist
     */
    public boolean batterieDa() {
        if (getFeld()[feldReiheRoboter][feldSpalteRoboter] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter + 1][feldSpalteRoboter] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter][feldSpalteRoboter + 1] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter - 1][feldSpalteRoboter] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter][feldSpalteRoboter - 1] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter + 1][feldSpalteRoboter + 1] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter - 1][feldSpalteRoboter - 1] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter + 1][feldSpalteRoboter - 1] == FeldEigenschaft.Batterie
                || getFeld()[feldReiheRoboter - 1][feldSpalteRoboter + 1] == FeldEigenschaft.Batterie) {
            return true;
        }
        return false;
    }

    /*
     * Gibt an ob auf dem Spalten- und ReihenIndex ein Felsen ist oder das Spielfeld ueberschritten wurde.
     */
    public boolean istNichtBesuchbar(int reihe, int spalte) {
        if (reihe >= getFeld().length || reihe < 0 || spalte >= getFeld()[0].length
                || spalte < 0 || (getFeld()[reihe][spalte] == FeldEigenschaft.Felsen)) {
            return true;
        }
        return false;
    }

    /*
     * merkt sich wie das Spiel gestartet ist, damit es bei Bedarf wieder darauf
     * zur�ck gesetzt werden kann
     */
    public void startWerteSpeichern() {
        setStartFeld(getFeld());
        startReiheRoboter = feldReiheRoboter;
        startReiheKind = feldReiheKind;
        startSpalteRoboter = feldSpalteRoboter;
        startSpalteKind = feldSpalteKind;
        start = false;
    }

    public void bewege(Richtung richtung, boolean roboter) {
        try {
            if (start) {
                startWerteSpeichern();
            }
            boolean felsen = false;
            if (roboter) {
                if (getRoboter().getTankFuellung() <= 0 && trankfuellungBeachten) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Tank leer");
                    alert.setHeaderText("der Tank ist leer");
                    alert.showAndWait();
                }
                switch (richtung) {
                    case UP:
                        if (felsen = (!istNichtBesuchbar(feldReiheRoboter - 1, feldSpalteRoboter))) {
                            feldReiheRoboter = feldReiheRoboter - 1;
                        }
                        break;
                    case DOWN:
                        if (felsen = (!istNichtBesuchbar(feldReiheRoboter + 1, feldSpalteRoboter))) {
                            feldReiheRoboter = feldReiheRoboter + 1;
                        }
                        break;
                    case LEFT:
                        if (felsen = (!istNichtBesuchbar(feldReiheRoboter, feldSpalteRoboter - 1))) {
                            feldSpalteRoboter = feldSpalteRoboter - 1;
                        }
                        break;
                    case RIGHT:
                        if (felsen = (!istNichtBesuchbar(feldReiheRoboter, feldSpalteRoboter + 1))) {
                            feldSpalteRoboter = feldSpalteRoboter + 1;
                        }
                        break;
                    default:
                        break;
                }
                if (isTrankfuellungBeachten()) {
                    getRoboter().setTankFuellung(getRoboter().getTankFuellung() - 1);
                }
                if (childNearby()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kind ist nah");
                    alert.setHeaderText("Zu nah ans Kind gekommen");
                    alert.showAndWait();
                }
            } else {
                switch (richtung) {
                    case UP:
                        if (felsen = (!istNichtBesuchbar(feldReiheKind - 1, feldSpalteKind))) {
                            feldReiheKind = feldReiheKind - 1;
                        }
                        break;
                    case DOWN:
                        if (felsen = (!istNichtBesuchbar(feldReiheKind + 1, feldSpalteKind))) {
                            feldReiheKind = feldReiheKind + 1;
                        }
                        break;
                    case LEFT:
                        if (felsen = (!istNichtBesuchbar(feldReiheKind, feldSpalteKind - 1))) {
                            feldSpalteKind = feldSpalteKind - 1;
                        }
                        break;
                    case RIGHT:
                        if (felsen = (!istNichtBesuchbar(feldReiheKind, feldSpalteKind + 1))) {
                            feldSpalteKind = feldSpalteKind + 1;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (!felsen) {
                throw new FelsenDaException();
            }
            checkIfChanged();
        } catch (FelsenDaException e) {
            e.play();
            verlorenFelsenGerammt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean childNearby() {
        if ((feldReiheRoboter == feldReiheKind && feldSpalteRoboter == feldSpalteKind) ||
                (feldReiheRoboter + 1 == feldReiheKind && feldSpalteRoboter == feldSpalteKind) ||
                (feldReiheRoboter == feldReiheKind && feldSpalteRoboter + 1 == feldSpalteKind) ||
                (feldReiheRoboter - 1 == feldReiheKind && feldSpalteRoboter == feldSpalteKind) ||
                (feldReiheRoboter == feldReiheKind && feldSpalteRoboter - 1 == feldSpalteKind) ||
                (feldReiheRoboter + 1 == feldReiheKind && feldSpalteRoboter + 1 == feldSpalteKind) ||
                (feldReiheRoboter - 1 == feldReiheKind && feldSpalteRoboter - 1 == feldSpalteKind) ||
                (feldReiheRoboter + 1 == feldReiheKind && feldSpalteRoboter - 1 == feldSpalteKind) ||
                (feldReiheRoboter - 1 == feldReiheKind && feldSpalteRoboter + 1 == feldSpalteKind)) {
            return true;
        }
        return false;
    }

    /*
     * gibt an, dass das Spiel verloren wurde
     */
    public void verlorenFelsenGerammt() {
        if (!getRoboter().isRunning() && !getRoboter().isAlive()) {
            setDeath(true);
        }
        aufAnfang();
    }

    /*
     * Setzt das Spiel auf den Anfangszustand zur�ck
     */
    public void aufAnfang() {
        if (getRoboter().isRunning()) {
            getRoboter().setStopped(true);
        }
        setFeldVonStartFeld(startFeld);
        feldReiheRoboter = startReiheRoboter;
        feldReiheKind = startReiheKind;
        feldSpalteRoboter = startSpalteRoboter;
        feldSpalteKind = startSpalteKind;
        checkUBoot();
    }


    public int getTerritoriumSpecialFinishNummer() {
        return territoriumSpecialFinishNummer;
    }

    public void setTerritoriumSpecialFinishNummer(int territoriumSpecialFinishNummer) {
        this.territoriumSpecialFinishNummer = territoriumSpecialFinishNummer;
    }

    public int getStartReiheRoboter() {
        return startReiheRoboter;
    }

    public void setStartReiheRoboter(int startReiheRoboter) {
        this.startReiheRoboter = startReiheRoboter;
    }

    public int getStartReiheKind() {
        return startReiheKind;
    }

    public void setStartReiheKind(int startReiheKind) {
        this.startReiheKind = startReiheKind;
    }

    public int getStartSpalteRoboter() {
        return startSpalteRoboter;
    }

    public void setStartSpalteRoboter(int startSpalteRoboter) {
        this.startSpalteRoboter = startSpalteRoboter;
    }

    public int getStartSpalteKind() {
        return startSpalteKind;
    }

    public void setStartSpalteKind(int startSpalteKind) {
        this.startSpalteKind = startSpalteKind;
    }

    public int getFeldReiheRoboter() {
        return feldReiheRoboter;
    }

    public void setFeldReiheRoboter(int feldReiheRoboter) {
        this.feldReiheRoboter = feldReiheRoboter;
    }

    public int getFeldSpalteRoboter() {
        return feldSpalteRoboter;
    }

    public void setFeldSpalteRoboter(int feldSpalteRoboter) {
        this.feldSpalteRoboter = feldSpalteRoboter;
    }

    public int getFeldReiheKind() {
        return feldReiheKind;
    }

    public void setFeldReiheKind(int feldReiheKind) {
        this.feldReiheKind = feldReiheKind;
    }

    public int getFeldSpalteKind() {
        return feldSpalteKind;
    }

    public void setFeldSpalteKind(int feldSpalteKind) {
        this.feldSpalteKind = feldSpalteKind;
    }

    public FeldEigenschaft[][] getStartFeld() {
        return startFeld;
    }

    private void setStartFeld(FeldEigenschaft[][] s) {
        startFeld = new FeldEigenschaft[s.length][s[0].length];
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++) {
                startFeld[i][j] = s[i][j];
            }
        }
    }

    private void setFeldVonStartFeld(FeldEigenschaft[][] s) {
        setFeld(new FeldEigenschaft[s.length][s[0].length]);
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++) {
                getFeld()[i][j] = s[i][j];
            }
        }
    }

    public boolean isLevelGeschaft() {
        return levelGeschafft;
    }

    public void setLevelGeschaft(boolean levelGeschaft) {
        this.levelGeschafft = levelGeschaft;
    }

    public int getFeldBreite() {
        return feldBreite;
    }

    public void setFeldBreite(int breite) {
        feldBreite = breite;
    }

    public int getFeldHoehe() {
        return feldHoehe;
    }

    public void setFeldHoehe(int hoehe) {
        feldHoehe = hoehe;
    }

    public Territorium getTerritorium() {
        return this;
    }

    public FeldEigenschaft[][] getFeld() {
        return feld;
    }

    public void setFeld(FeldEigenschaft[][] feld) {
        this.feld = feld;
    }

    public double getEntfernungBreiteDesSpielFeldes() {
        return entfernungBreiteDesSpielFeldes;
    }

    public void setEntfernungBreiteDesSpielFeldes(double entfernungBreiteDesSpielFeldes) {
        this.entfernungBreiteDesSpielFeldes = entfernungBreiteDesSpielFeldes;
    }

    public double getEntfernungHoeheDesSpielFeldes() {
        return entfernungHoeheDesSpielFeldes;
    }

    public void setEntfernungHoeheDesSpielFeldes(double entfernungHoeheDesSpielFeldes) {
        this.entfernungHoeheDesSpielFeldes = entfernungHoeheDesSpielFeldes;
    }

    @XmlTransient
    public Kind getChild() {
        return child;
    }

    public void setChild(Kind child) {
        this.child = child;
    }

    @XmlTransient
    public Roboter getRoboter() {
        return roboter;
    }

    public void setRoboter(Roboter roboter) {
        this.roboter = roboter;
    }

    public boolean isNotify() {
        return notify;
    }

    /*
     * damit die Methoden in einem rutsch ausgef�hrt werden beim PopUpMenu
     */
    public void setNotify(boolean notify) {
        this.notify = notify;
        if (notify) {
            checkIfChanged();
        }
    }

    public double getObjectResizedWidth() {
        return objectResizedWidth;
    }

    public void setObjectResizedWidth(double objectResizedWidth) {
        this.objectResizedWidth = objectResizedWidth;
    }

    public double getObjectResizedHeigth() {
        return objectResizedHeigth;
    }

    public void setObjectResizedHeigth(double objectResizedHeigth) {
        this.objectResizedHeigth = objectResizedHeigth;
    }

    public boolean isResizeable() {
        return resizeable;
    }

    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
        checkIfChanged();
    }

    public boolean isDeath() {
        return death;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }


    public ArrayList<ZielFeld> getZielFelder() {
        return zielFelder;
    }

    public void setZielFelder(ArrayList<ZielFeld> zielFelder) {
        this.zielFelder = zielFelder;
    }

    public ZielFeld getNextGoalField() {
        return nextGoalField;
    }

    public void setNextGoalField(ZielFeld nextGoalField) {
        this.nextGoalField = nextGoalField;
    }

    public boolean isTrankfuellungBeachten() {
        return trankfuellungBeachten;
    }

    public void setTrankfuellungBeachten(boolean trankfuellungBeachten) {
        this.trankfuellungBeachten = trankfuellungBeachten;
    }

    public int getStartTankfuellung() {
        return startTankfuellung;
    }

    public void setStartTankfuellung(int startTankfuellung) {
        this.startTankfuellung = startTankfuellung;
    }
}

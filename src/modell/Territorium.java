package modell;

import java.util.Arrays;
import java.util.Observable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import dbZugriffe.SelectStatements;
import javafx.scene.media.AudioClip;

@XmlRootElement(name = "territorium")
public class Territorium extends Observable implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MIN_WIDTH = 2;
	public static final int MIN_HEIGTH = 1;
	public static final int MAX_WIDTH = 100;
	public static final int MAX_HEIGTH = 100;
	public static final int OBJ_WIDTH = 50;
	public static final int OBJ_MIN_WIDTH = 30;

	public static enum FeldEigenschaft implements java.io.Serializable {
		Leer, Batterie, Felsen, LeuchtFelsen, ZielFeld, Hai
	}

	private double objectResizedWidth = Territorium.OBJ_WIDTH;
	private double objectResizedHeigth = Territorium.OBJ_WIDTH;

	private boolean resizeable = true;

	private boolean levelGeschaft = false;

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

	private transient Roboter uboot;

	private FeldEigenschaft[][] feld;

	private boolean notify = true;

	private boolean death = false;

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
	}

	/*
	 * erstellt das immer geladene Beispielfeld
	 */
	private void erstelleBeispielFeld() {
		setzeObjektAufsFeld(3, 3, FeldEigenschaft.Felsen);
		setzeObjektAufsFeld(3, 4, FeldEigenschaft.Batterie);
		setzeObjektAufsFeld(3, 5, FeldEigenschaft.LeuchtFelsen);
		setzeObjektAufsFeld(3, 6, FeldEigenschaft.ZielFeld);
		setzeObjektAufsFeld(9, 9, FeldEigenschaft.Hai);
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
		if (getUboot().isRunning()) {
			getUboot().setStopped(true);
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
		if (spalte - 1 >= 0 && getFeld()[reihe][spalte] != FeldEigenschaft.Felsen
				&& getFeld()[reihe][spalte - 1] != FeldEigenschaft.Felsen
				&& getFeld()[reihe][spalte] != FeldEigenschaft.LeuchtFelsen
				&& getFeld()[reihe][spalte - 1] != FeldEigenschaft.LeuchtFelsen
				&& getFeld()[reihe][spalte] != FeldEigenschaft.Hai
				&& getFeld()[reihe][spalte - 1] != FeldEigenschaft.Hai
				&& getFeld()[reihe][spalte] != FeldEigenschaft.ZielFeld) {
			if (((spalte + 1 < feldBreite && getFeld()[reihe][spalte + 1] == FeldEigenschaft.Hai))) {
				return false;
			}
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
			feldReiheKind = reihe;
			feldSpalteRoboter = spalte;
			feldSpalteKind = spalte - 1;
		}
		checkIfChanged();
	}

	/*
	 * Setzt ein m�gliches Objekt auf das Spielfeld.
	 */
	public void setzeObjektAufsFeld(int reihe, int spalte, FeldEigenschaft f) {
		if (reihe < 0 || reihe >= feldHoehe || spalte < 0 || spalte >= feldBreite) {
			throw new IndexOutOfBoundsException();
		}
		if (f == FeldEigenschaft.ZielFeld && (!(feldReiheRoboter == reihe && feldSpalteRoboter == spalte))) {
			getFeld()[reihe][spalte] = f;
		}

		if (f == FeldEigenschaft.Felsen || f == FeldEigenschaft.LeuchtFelsen) {
			if (!((feldReiheRoboter == reihe && feldSpalteRoboter == spalte)
					|| (feldReiheKind == reihe && feldSpalteKind == spalte))) {
				getFeld()[reihe][spalte] = f;
			}
		} else if (f == FeldEigenschaft.Hai) {
			if (!((feldReiheRoboter == reihe && feldSpalteRoboter == spalte)
					|| (feldReiheKind == reihe && feldSpalteKind == spalte)
					|| (feldReiheRoboter == reihe && feldSpalteRoboter + 1 == spalte))) {
				getFeld()[reihe][spalte] = f;
			}
		} else {
			getFeld()[reihe][spalte] = f;
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
	 * nimmt die Batterie falls eine vorhanden ist, sonst gibt es eine
	 * KachelleerException raus
	 */
	public void nihmBatterie() {
		try {
			if (getFeld()[feldReiheRoboter][feldSpalteRoboter] == FeldEigenschaft.Batterie) {
				getFeld()[feldReiheRoboter][feldSpalteRoboter] = FeldEigenschaft.Leer;
			} else if (getFeld()[feldReiheKind][feldSpalteKind] == FeldEigenschaft.Batterie) {
				getFeld()[feldReiheKind][feldSpalteKind] = FeldEigenschaft.Leer;
			} else {
				throw new KachelleerException();
			}
		} catch (KachelleerException e) {
			e.play();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkIfChanged();
		}
	}

	/*
	 * gibt an ob eine Batterie unter dem U-Boot ist
	 */
	public boolean batterieDa() {
		if (getFeld()[feldReiheRoboter][feldSpalteRoboter] == FeldEigenschaft.Batterie
				|| getFeld()[feldReiheKind][feldSpalteKind] == FeldEigenschaft.Batterie) {
			return true;
		}
		return false;
	}

	/*
	 * Gibt an ob auf dem Spalten- und ReihenIndex ein Felsen ist. Beachte:
	 * Leuchtfelsen erbt von Felsen! Und es wird ber�cksichtigt ob es dunkel
	 * ist!
	 */
	public boolean felsenDa(int reihe, int spalte) {
		if ((getFeld()[reihe][spalte] == FeldEigenschaft.Felsen
				|| getFeld()[reihe][spalte] == FeldEigenschaft.LeuchtFelsen)) {
			return true;
		}
		return false;
	}

	/*
	 * Umgeht die Beleuchtungsabfrage
	 */
	private boolean felsenDaAdmin(int reihe, int spalte) {
		if (getFeld()[reihe][spalte] == FeldEigenschaft.Felsen
				|| getFeld()[reihe][spalte] == FeldEigenschaft.LeuchtFelsen) {
			return true;
		}
		return false;
	}

	/*
	 * Stellt eine Abfrage ob ein Fels vor dem U-Boot ist oder ob das Spielfeld
	 * zu Ende ist
	 */
	public boolean felsenDaOderSpielfeldEndeAbfrageVor() {
		if (feldReiheRoboter <= 0 || felsenDa(feldReiheRoboter - 1, feldSpalteRoboter)) {
			return true;
		}
		return false;
	}

	/*
	 * Stellt eine Abfrage ob ein Fels hinter dem U-Boot ist oder ob das
	 * Spielfeld zu Ende ist
	 */
	public boolean felsenDaOderSpielfeldEndeAbfrageRueck() {
		if (feldReiheKind >= getFeld().length - 1 || felsenDa(feldReiheKind + 1, feldSpalteKind)) {
			return true;
		}
		return false;
	}

	/*
	 * Stellt eine Abfrage ob ein Fels links vom dem U-Boot-Bug ist oder ob das
	 * Spielfeld zu Ende ist
	 */
	public boolean felsenDaOderSpielfeldEndeAbfrageLinks() {
		if (feldReiheRoboter >= getFeld().length - 1 || felsenDa(feldReiheRoboter + 1, feldSpalteRoboter)) {
			return true;
		}
		return false;
	}

	/*
	 * Stellt eine Abfrage ob ein Fels rechts vom dem U-Boot-Bug ist oder ob das
	 * Spielfeld zu Ende ist
	 */
	public boolean felsenDaOderSpielfeldEndeAbfrageRechts() {
		if (feldReiheRoboter >= getFeld().length - 1 || felsenDa(feldReiheRoboter + 1, feldSpalteRoboter)) {
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

	/*
	 * L�sst das U-Boot ein Feld vor fahren in Fahrtrichtung
	 */
	public void vorBewegen() {
		try {
			if (start) {
				startWerteSpeichern();
			}
			if (feldReiheRoboter > 0 && !felsenDaAdmin(feldReiheRoboter - 1, feldSpalteRoboter)) {
				feldReiheRoboter = feldReiheRoboter - 1;
				feldReiheKind = feldReiheKind - 1;
			} else {
				throw new FelsenDaException();
			}
			aggressiveFische();
			zielFeldErreicht();
			checkIfChanged();
		} catch (FelsenDaException e) {
			e.play();
			verlorenFelsenGerammt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * L�sst das U-Boot ein Feld zur�ck fahren in Fahrtrichtung
	 */
	public void rueckBewegen() {
		try {
			if (start) {
				startWerteSpeichern();
			}
			if (feldReiheKind < getFeld().length - 1 && !felsenDaAdmin(feldReiheKind + 1, feldSpalteKind)) {
				feldReiheRoboter = feldReiheRoboter + 1;
				feldReiheKind = feldReiheKind + 1;
			} else {
				throw new FelsenDaException();
			}
			aggressiveFische();
			zielFeldErreicht();
		} catch (FelsenDaException e) {
			e.play();
			verlorenFelsenGerammt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * L�sst das U-Boot sich nach links drehen in Fahrtrichtung
	 */
	public void rechtsBewegen() {
		try {
			if (start) {
				startWerteSpeichern();
			}
			if (feldReiheRoboter < getFeld().length - 1 && !felsenDaAdmin(feldReiheRoboter + 1, feldSpalteRoboter)) {
				feldReiheRoboter = feldReiheRoboter + 1;
				feldSpalteKind = feldSpalteKind + 1;
			} else {
				throw new FelsenDaException();
			}
			aggressiveFische();
			zielFeldErreicht();
		} catch (FelsenDaException e) {
			e.play();
			verlorenFelsenGerammt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * L�sst das U-Boot sich nach links drehen in Fahrtrichtung
	 */
	public void linksBewegen() {
		try {
			if (start) {
				startWerteSpeichern();
			}
			if (feldReiheRoboter < getFeld().length - 1 && !felsenDaAdmin(feldReiheRoboter + 1, feldSpalteRoboter)) {
				feldReiheRoboter = feldReiheRoboter + 1;
				feldSpalteKind = feldSpalteKind - 1;
			} else {
				throw new FelsenDaException();
			}
			aggressiveFische();
			zielFeldErreicht();
		} catch (FelsenDaException e) {
			e.play();
			verlorenFelsenGerammt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * l�sst die Haie wandern nach einer lauf-aktion des U-Boots, dabei laufen
	 * sie in einem drei Felder + Zeichen (Wie ein Turm beim Schach, nur 3
	 * Felder weit blickend und ein Feld laufend pro zug) Ein Hai z�hlt f�r das
	 * U-Boot wie ein Felsen!
	 */
	private void aggressiveFische() {
		for (int i = 1; i <= 3; i++) {
			if (feldReiheRoboter + i < feldHoehe && feldReiheKind + i < feldHoehe) {
				if (getFeld()[feldReiheRoboter + i][feldSpalteRoboter] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheRoboter + i - 1][feldSpalteRoboter] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheRoboter + i][feldSpalteRoboter] = FeldEigenschaft.Leer;
					getFeld()[feldReiheRoboter + i - 1][feldSpalteRoboter] = FeldEigenschaft.Hai;
				} else if (getFeld()[feldReiheKind + i][feldSpalteKind] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheKind + i - 1][feldSpalteKind] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheKind + i][feldSpalteKind] = FeldEigenschaft.Leer;
					getFeld()[feldReiheKind + i - 1][feldSpalteKind] = FeldEigenschaft.Hai;
				}
			}

			if (feldReiheRoboter - i >= 0 && feldReiheKind - i >= 0) {
				if (getFeld()[feldReiheRoboter - i][feldSpalteRoboter] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheRoboter - i + 1][feldSpalteRoboter] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheRoboter - i][feldSpalteRoboter] = FeldEigenschaft.Leer;
					getFeld()[feldReiheRoboter - i + 1][feldSpalteRoboter] = FeldEigenschaft.Hai;
				} else if (getFeld()[feldReiheKind - i][feldSpalteKind] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheKind - i + 1][feldSpalteKind] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheKind - i][feldSpalteKind] = FeldEigenschaft.Leer;
					getFeld()[feldReiheKind - i + 1][feldSpalteKind] = FeldEigenschaft.Hai;
				}
			}

			if (feldSpalteRoboter + i < feldBreite && feldSpalteKind + i < feldBreite) {
				if (getFeld()[feldReiheRoboter][feldSpalteRoboter + i] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheRoboter][feldSpalteRoboter + i - 1] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheRoboter][feldSpalteRoboter + i] = FeldEigenschaft.Leer;
					getFeld()[feldReiheRoboter][feldSpalteRoboter + i - 1] = FeldEigenschaft.Hai;
				} else if (getFeld()[feldReiheKind][feldSpalteKind + i] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheKind][feldSpalteKind + i - 1] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheKind][feldSpalteKind + i] = FeldEigenschaft.Leer;
					getFeld()[feldReiheKind][feldSpalteKind + i - 1] = FeldEigenschaft.Hai;
				}
			}

			if (feldSpalteRoboter - i >= 0 && feldSpalteKind - i >= 0) {
				if (getFeld()[feldReiheRoboter][feldSpalteRoboter - i] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheRoboter][feldSpalteRoboter - i + 1] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheRoboter][feldSpalteRoboter - i] = FeldEigenschaft.Leer;
					getFeld()[feldReiheRoboter][feldSpalteRoboter - i + 1] = FeldEigenschaft.Hai;
				} else if (getFeld()[feldReiheKind][feldSpalteKind - i] == FeldEigenschaft.Hai
						&& getFeld()[feldReiheKind][feldSpalteKind - i + 1] == FeldEigenschaft.Leer) {
					getFeld()[feldReiheKind][feldSpalteKind - i] = FeldEigenschaft.Leer;
					getFeld()[feldReiheKind][feldSpalteKind - i + 1] = FeldEigenschaft.Hai;
				}
			}
		}

		if (getFeld()[feldReiheRoboter][feldSpalteRoboter] == FeldEigenschaft.Hai
				|| getFeld()[feldReiheKind][feldSpalteKind] == FeldEigenschaft.Hai) {
			verlorenFelsenGerammt();
		}
		checkIfChanged();
	}

	/*
	 * gibt an, dass das Spiel verloren wurde
	 */
	public void verlorenFelsenGerammt() {
		if (!getUboot().isRunning() && !getUboot().isAlive()) {
			setDeath(true);
		}
		aufAnfang();
	}

	/*
	 * Setzt das Spiel auf den Anfangszustand zur�ck
	 */
	public void aufAnfang() {
		if (getUboot().isRunning()) {
			getUboot().setStopped(true);
		}
		setFeldVonStartFeld(startFeld);
		feldReiheRoboter = startReiheRoboter;
		feldReiheKind = startReiheKind;
		feldSpalteRoboter = startSpalteRoboter;
		feldSpalteKind = startSpalteKind;
		checkUBoot();
	}

	/*
	 * gibt an ob der Bug das Zielfeld erreicht hat.
	 */
	public void zielFeldErreicht() {
		if (getFeld()[feldReiheRoboter][feldSpalteRoboter] == FeldEigenschaft.ZielFeld) {
			if (getUboot().isRunning()) {
				getUboot().setStopped(true);
			} else {
				String s = "../resourcesPicturesAndSoundsVidoes/FinishWave" + getTerritoriumSpecialFinishNummer()
						+ ".wav";
				try {
					if (getClass().getResource(s) == null) {
						s = "../resourcesPicturesAndSoundsVidoes/FinishWave0.wav";
					}
				} catch (Exception e) {
					s = "../resourcesPicturesAndSoundsVidoes/FinishWave0.wav";
				}
				AudioClip clipFinish = new AudioClip(getClass().getResource(s).toString());
				clipFinish.play(1.0);
			}
			setLevelGeschaft(true);
			aufAnfang();
		}
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
		return levelGeschaft;
	}

	public void setLevelGeschaft(boolean levelGeschaft) {
		this.levelGeschaft = levelGeschaft;
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
	public Roboter getUboot() {
		return uboot;
	}

	public void setUboot(Roboter uboot) {
		this.uboot = uboot;
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
}

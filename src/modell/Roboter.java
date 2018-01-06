package modell;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import controlls.RunCodeController;
import resources.Invisible;
import javafx.beans.property.SimpleDoubleProperty;

public class Roboter extends Thread {

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private Territorium territorium;
	private boolean isRunning = false;
	private boolean pause = false;
	private boolean stopped = false;
	private String[] abbruchArray = new String[RunCodeController.endlossAbbruchKriterium];
	private int countAbbruch = 0;
	private SimpleDoubleProperty speed = new SimpleDoubleProperty();
	private int tankFuellung;

	/*
	 * I'll survive (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Invisible
	public void run() {
		try {
			setRunning(true);
			getTerritorium().getRoboter().getClass().getMethod("main", null).invoke(territorium.getRoboter());
		} catch (InvocationTargetException | ThreadStopException t) {
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} finally {
			resetAbbruch();
			setRunning(false);

		}
	}

	@Invisible
	public Roboter(Territorium t) {
		setTerritorium(t);
	}

	@Invisible
	protected Roboter() {
	}

	/*
	 * l�sst den Thread schlafen nach jeder Ausf�hrung einer Canvas ver�ndernden
	 * Methode. Hier wird auch Pause und Stop des Threads angegangen und
	 * weitergeleitet im Falle von Stop
	 */
	private  void checkRunning(Territorium.Richtung name) throws ThreadStopException {
		if (isRunning) {
			if (stopped) {
				throw new ThreadStopException();
			}

			/*
			 * Der folgende Teil pr�ft auf eine Endlosschleife von maxmialer
			 * Gr��e von RunCodeController.endlossAbbruchKriterium ab / 2.
			 * Sollte die Endlosschlaeife einen gr��eren Rahmen umfassen, so
			 * wird nicht abgebrochen. Dieses ist aber sehr unwahrscheinlich.
			 */
			getAbbruchArray()[getCountAbbruch()] = name.toString();
			setCountAbbruch(getCountAbbruch() + 1);
			if (getAbbruchArray()[RunCodeController.endlossAbbruchKriterium - 1] != null && !getAbbruchArray()[RunCodeController.endlossAbbruchKriterium - 1].equals("")) {
				ArrayList<String> abbruch = new ArrayList<String>();
				String anfangsString = getAbbruchArray()[RunCodeController.endlossAbbruchKriterium - 1];
				abbruch.add(anfangsString);
				int count = RunCodeController.endlossAbbruchKriterium - 2;
				while (count >= 0 && !getAbbruchArray()[count].equals(anfangsString)) {
					abbruch.add(getAbbruchArray()[count]);
					count = count - 1;
				}
				int zaehler = RunCodeController.endlossAbbruchKriterium - count;
				/*
				 * wenn zaehler gr��er als das halbe Abbruchkriterium, dann ist
				 * ein Vergleich sinnlos..
				 */
				if (zaehler <= RunCodeController.endlossAbbruchKriterium / 2) {
					int zaehlerImArray = 0;
					while (count >= 0) {
						if (!abbruch.get(zaehlerImArray).equals(getAbbruchArray()[count])) {
							break;
						}
						count = count - 1;
						zaehlerImArray = (zaehlerImArray + 1) % zaehler;
					}
					if (count == 0) {
						throw new ThreadStopException();
					}
				}

			}
			// end EndlosschleifenPr�fung
			try {
				sleep((long) (50 * (101.0 - getSpeed().getValue())));
				while (isPause()) {
					sleep((long) (50 * (101.0 - getSpeed().getValue())));
//					this.wait();
				}
			} catch (InterruptedException e) {
				System.out.println("this is the end, hold your hands and count to 10, feel the earth move an then");
				throw new ThreadStopException();
			}
			if (stopped) {
				throw new ThreadStopException();
			}
		}
	}

	public  void bewege(Territorium.Richtung richtung) throws FelsenDaException, ThreadStopException {
		getTerritorium().bewege(richtung, true);
		checkRunning(richtung);
	}

	public  boolean batterieDa() {
		return getTerritorium().batterieDa();
	}

	@Invisible
	public  Territorium getTerritorium() {
		return territorium;
	}

	@Invisible
	public  void setTerritorium(Territorium territorium) {
		this.territorium = territorium;
	}

	public  boolean isLevelGeschaft() {
		return getTerritorium().isLevelGeschaft();
	}

	@Invisible
	public  boolean isRunning() {
		return isRunning;
	}

	@Invisible
	public synchronized void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}


	@Invisible
	public  boolean isPause() {
		return pause;
	}

	@Invisible
	public synchronized void setPause(boolean pause) {
		this.pause = pause;
//		if(!pause){
//			notifyAll();
//		}
	}

	@Invisible
	public  boolean isStopped() {
		return stopped;
	}

	@Invisible
	public synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	@Invisible
	public  String[] getAbbruchArray() {
		return abbruchArray;
	}

	@Invisible
	public  void setAbbruchArray(String[] abbruchArray) {
		this.abbruchArray = abbruchArray;
	}

	@Invisible
	public  int getCountAbbruch() {
		return countAbbruch;
	}

	/*
	 * Modulo Rechnung um immer im String-Array zu bleiben!
	 */
	@Invisible
	public  void setCountAbbruch(int countAbbruch) {
		this.countAbbruch = countAbbruch % RunCodeController.endlossAbbruchKriterium;
	}

	@Invisible
	public  void resetAbbruch() {
		setCountAbbruch(0);
		setAbbruchArray(new String[RunCodeController.endlossAbbruchKriterium]);
	}

	
	@Invisible
	public  SimpleDoubleProperty getSpeed() {
		return speed;
	}
	
	@Invisible
	public  void setSpeed(SimpleDoubleProperty speed) {
		this.speed = speed;
	}

	public int getTankFuellung() {
		return tankFuellung;
	}

	public void setTankFuellung(int tankFuellung) {
		this.tankFuellung = tankFuellung;
	}
}

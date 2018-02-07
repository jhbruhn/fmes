package modell;

import controlls.RunCodeController;
import javafx.beans.property.SimpleDoubleProperty;
import resources.Invisible;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Kind extends Thread {

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
	private boolean sleeping;

	/*
	 * I'll survive (non-Javadoc)
	 *
	 * @see java.lang.Thread#run()
	 */
	@Invisible
	public void run() {
		try {
			setRunning(true);
			//getTerritorium().getRoboter().getClass().getMethod("main", null).invoke(territorium.getRoboter());

		} finally {
			//resetAbbruch();
			//setRunning(false);

		}
	}

	@Invisible
	public Kind(Territorium t) {
		setTerritorium(t);
	}

	@Invisible
	protected Kind() {
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

			sleeping = true;
			try {
				sleep((long) (10 * (101.0 - getSpeed().getValue())));
				while (isPause()) {
					sleep((long) (10 * (101.0 - getSpeed().getValue())));
				}
			} catch (InterruptedException e) {
				sleeping = false;
				System.out.println("this is the end, hold your hands and count to 10, feel the earth move an then");
				throw new ThreadStopException();
			}
			sleeping = false;
			if (stopped) {
				throw new ThreadStopException();
			}
		}
	}

	public  void bewege(Territorium.Richtung richtung) throws FelsenDaException, ThreadStopException {
		getTerritorium().bewege(richtung, false);
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

	public synchronized boolean isSleeping() {
		return sleeping;
	}
}

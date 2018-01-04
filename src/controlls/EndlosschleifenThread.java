package controlls;

import java.util.ArrayList;

import modell.Roboter;
import views.TerritoriumPanel;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class EndlosschleifenThread extends Thread {
	private TerritoriumPanel territoriumPanel;
	private Roboter roboter;
	private ArrayList<MenuItem> menu;
	private ArrayList<Button> button;
	private MenuItem stopMenuItem;
	private Button stop;
	private MenuItem pauseMenuItem;
	private Button pause;
	private MenuItem startMenuItem;
	private Button start;

	public EndlosschleifenThread(TerritoriumPanel t, Roboter u, ArrayList<MenuItem> menu, ArrayList<Button> button,
                                 MenuItem stopMenuItem, Button stop, MenuItem pauseMenuItem, Button pause, MenuItem startMenuItem,
                                 Button start) {
		setTerritoriumPanel(t);
		setUBoot(u);
		setMenu(menu);
		setButton(button);
		this.stopMenuItem = stopMenuItem;
		this.stop = stop;
		this.pauseMenuItem = pauseMenuItem;
		this.pause = pause;
		this.startMenuItem = startMenuItem;
		this.start = start;
	}

	private void setStartZustand() {
		stopMenuItem.setDisable(true);
		stop.setDisable(true);
		pauseMenuItem.setDisable(true);
		pause.setDisable(true);
		startMenuItem.setDisable(false);
		start.setDisable(false);
	}

	/*
	 * Die Methode Wartet erst 3 Sekunden damit �berhaupt irgendwas geschehen
	 * ist und pr�ft danach alle 5 Sekunden ab ob �berhaupt irgendetwas am
	 * Canvas passiert ist. Sollte dem nicht so sein stopt der Thread das UBoot
	 * (Au�er das UBoot steht auf Pause..). Sollte das UBoot schon gestoppt sein
	 * von irgendwo anders her stirbt auch dieser Thread nach kurzem.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		disableAndEnable(true);
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean pause = false;
		while (getUBoot().isAlive() && getUBoot().isRunning()) {
			pause = getUBoot().isPause();
			getTerritoriumPanel().setChanged(false);
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!getTerritoriumPanel().isChanged() && !pause && !getUBoot().isPause()) {
				getUBoot().interrupt();
				System.out.println("fick dich");
				/*
				 * Ich wei� nicht so geil mit stop hier, aber ich wusste nicht
				 * wie ich besser in die Main methode von UBoot eingreifen kann
				 * weiterhin sind die Transaktionen hier egal, sodass auch
				 * mitten drin abgebrochen werden d�rfte..
				 */
				getUBoot().stop();
			}
		}
		disableAndEnable(false);
		setStartZustand();
	}

	/*
	 * Stellt sicher das alles disablet ist w�hrend der thread l�uft und wieder
	 * anclickbar wird nachdem der uboot thread angehalten ist
	 */
	private void disableAndEnable(boolean disable) {
		for (Button b : getButton()) {
			b.setDisable(disable);
		}
		for (MenuItem m : getMenu()) {
			m.setDisable(disable);
		}
	}

	public TerritoriumPanel getTerritoriumPanel() {
		return territoriumPanel;
	}

	public void setTerritoriumPanel(TerritoriumPanel territoriumPanel) {
		this.territoriumPanel = territoriumPanel;
	}

	public Roboter getUBoot() {
		return roboter;
	}

	public void setUBoot(Roboter roboter) {
		this.roboter = roboter;
	}

	public ArrayList<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(ArrayList<MenuItem> menu) {
		this.menu = menu;
	}

	public ArrayList<Button> getButton() {
		return button;
	}

	public void setButton(ArrayList<Button> button) {
		this.button = button;
	}

}

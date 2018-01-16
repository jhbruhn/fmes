package controlls;

import java.util.ArrayList;

import modell.Territorium;
import modell.Roboter;
import views.Oberflaeche;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;

public class RunCodeController {
	private Territorium territorium;
	private Slider slider;
	private Roboter runcode;
	private EndlosschleifenThread endlos;
	public static final int endlossAbbruchKriterium = 200;

	public RunCodeController(Territorium t, Slider s, Button start, Button stop, Button pause, MenuItem startMenuItem,
			MenuItem stopMenuItem, MenuItem pauseMenuItem, Oberflaeche o, ArrayList<Button> buttons,
			ArrayList<MenuItem> menuItems) {
		setTerritorium(t);
		setSlider(s);

		stopMenuItem.setDisable(true);
		stop.setDisable(true);
		pauseMenuItem.setDisable(true);
		pause.setDisable(true);

		stopMenuItem.setOnAction(e -> {
			if (runcode != null && runcode.isAlive()) {
				stopMenuItem.setDisable(true);
				stop.setDisable(true);
				pauseMenuItem.setDisable(true);
				pause.setDisable(true);
				startMenuItem.setDisable(true);
				start.setDisable(true);
				runcode.setStopped(true);
				if (runcode.isPause()) {
					runcode.setPause(false);
				}
			}
		});
		stop.setOnAction(e -> stopMenuItem.fire());

		startMenuItem.setOnAction(e -> {
			if (runcode == null || !runcode.isAlive()) {
				runcode = null;
				endlos = null;
				runcode = getTerritorium().getRoboter();
				endlos = new EndlosschleifenThread(o.territoriumPanel, runcode, menuItems, buttons, stopMenuItem, stop,
						pauseMenuItem, pause, startMenuItem, start);
				getTerritorium().getRoboter().getSpeed().bind(getSlider().valueProperty());
				runcode.setStopped(false);
				runcode.start();
				endlos.start();
				stopMenuItem.setDisable(false);
				stop.setDisable(false);
				Controller controller = new Controller(getTerritorium());
				controller.run();
			} else {
				runcode.setPause(false);
			}
			pauseMenuItem.setDisable(false);
			pause.setDisable(false);
			startMenuItem.setDisable(true);
			start.setDisable(true);
		});
		start.setOnAction(e -> startMenuItem.fire());

		pauseMenuItem.setOnAction(e -> {
			if (runcode != null && runcode.isAlive()) {
				try {
					runcode.setPause(true);
					pauseMenuItem.setDisable(true);
					pause.setDisable(true);
					startMenuItem.setDisable(false);
					start.setDisable(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		pause.setOnAction(e -> pauseMenuItem.fire());
	}

	public Territorium getTerritorium() {
		return territorium;
	}

	public void setTerritorium(Territorium territorium) {
		this.territorium = territorium;
	}

	public Slider getSlider() {
		return slider;
	}

	public void setSlider(Slider slider) {
		this.slider = slider;
	}
}

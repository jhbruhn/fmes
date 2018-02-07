package controlls;

import java.util.ArrayList;

import modell.Kind;
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
	public static final int endlossAbbruchKriterium = 200;
	Controller controller;
	public RunCodeController(Territorium t, Slider s, Button start, Button stop, Button pause, MenuItem startMenuItem,
			MenuItem stopMenuItem, MenuItem pauseMenuItem, Oberflaeche o) {
		setTerritorium(t);
		setSlider(s);

		stopMenuItem.setDisable(true);
		stop.setDisable(true);
		pauseMenuItem.setDisable(true);
		pause.setDisable(true);

		stopMenuItem.setOnAction(e -> {
			if (runcode != null /*&& runcode.isAlive()*/) {
				stopMenuItem.setDisable(true);
				stop.setDisable(true);
				pauseMenuItem.setDisable(true);
				pause.setDisable(true);
				startMenuItem.setDisable(false);
				start.setDisable(false);
				runcode.setStopped(true);
				if (runcode.isPause()) {
					runcode.setPause(false);
				}
				controller.setStopped(true);
			}
		});
		stop.setOnAction(e -> stopMenuItem.fire());

		startMenuItem.setOnAction(e -> {
			if (runcode == null || !runcode.isAlive()) {
				runcode = null;
				Roboter ralf = new Roboter(getTerritorium());
				getTerritorium().setRoboter(ralf);
				Kind hammerhuepfer = new Kind(getTerritorium());
				getTerritorium().setChild(hammerhuepfer);
				runcode = getTerritorium().getRoboter();
				getTerritorium().getRoboter().getSpeed().bind(getSlider().valueProperty());
				runcode.setStopped(false);
				runcode.start();
				runcode.setRunning(true);

				hammerhuepfer.start();
				hammerhuepfer.setStopped(false);

				//endlos.start();
				stopMenuItem.setDisable(false);
				stop.setDisable(false);
				controller = new Controller(runcode, getTerritorium());
				controller.start();
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

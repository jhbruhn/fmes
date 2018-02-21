package controlls;

import java.util.ArrayList;

import javafx.scene.control.*;
import modell.Kind;
import modell.Territorium;
import modell.Roboter;
import views.Oberflaeche;

import javax.swing.text.html.ImageView;

public class RunCodeController {
    private Territorium territorium;
    private Slider slider;
    private Roboter runcode;
    public static final int endlossAbbruchKriterium = 200;
    Controller controller;
    ComboBox<javafx.scene.image.ImageView> combo;
    private ArrayList<RadioMenuItem> radioMenus = new ArrayList<RadioMenuItem>();

    public RunCodeController(Territorium t, Slider s, Button start, Button stop, MenuItem startMenuItem,
                             MenuItem stopMenuItem, Oberflaeche o, ComboBox<javafx.scene.image.ImageView> c, RadioMenuItem r1, RadioMenuItem r2, RadioMenuItem r3,
                             RadioMenuItem r4, RadioMenuItem r5, RadioMenuItem r6) {
        setTerritorium(t);
        setSlider(s);

        radioMenus.add(r1);
        radioMenus.add(r2);
        radioMenus.add(r3);
        radioMenus.add(r4);
        radioMenus.add(r5);
        radioMenus.add(r6);
        combo = c;

        stopMenuItem.setDisable(true);
        stop.setDisable(true);

        stopMenuItem.setOnAction(e -> {
            if (runcode != null /*&& runcode.isAlive()*/) {
                stopMenuItem.setDisable(true);
                stop.setDisable(true);
                startMenuItem.setDisable(false);
                start.setDisable(false);
                runcode.setStopped(true);
                controller.setStopped(true);
            }
            enableOrDisablePlacing(false);
        });
        stop.setOnAction(e -> stopMenuItem.fire());

        startMenuItem.setOnAction(e -> {
            if (runcode == null || !runcode.isAlive()) {
                enableOrDisablePlacing(true);
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
            }
            startMenuItem.setDisable(true);
            start.setDisable(true);
        });
        start.setOnAction(e -> startMenuItem.fire());
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

    public void enableOrDisablePlacing(boolean disable){
        for(RadioMenuItem r : radioMenus){
            r.setDisable(disable);
        }
        combo.setDisable(disable);
    }
}

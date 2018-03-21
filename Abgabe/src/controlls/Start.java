package controlls;

import java.util.HashMap;

import modell.Territorium;
import modell.Roboter;
import views.Oberflaeche;
import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {

	public static final String defaultName = "Roboter";

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.application.Application#start(javafx.stage.Stage) start
	 * Methode, welche ganz zu Anfang des Programmes aufgerufen wird. Von diesem
	 * Object gehen dann alle Fenster aus und werden �berpr�ft.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Territorium territorium = new Territorium();
		Oberflaeche oberflaeche = new Oberflaeche(primaryStage, territorium);
		erstelleNeuesFenster(oberflaeche, primaryStage, territorium);

		oberflaeche.setTextChanged(false);

	}

	/*
	 * hier wird das neue Fenster erstellt, welches ganz am Anfang bei Start des
	 * Programmes wichtig ist, oder eben wenn ein weiteres Fenster ge�ffnet wird
	 * durch den aufruf der Buttons/MenuItems
	 */
	public void erstelleNeuesFenster(Oberflaeche oberflaeche, Stage primaryStage, Territorium territorium) {
		// UBoot uboot = new UBoot(territorium);
		// territorium.setUboot(uboot);
		oberflaeche.getSubmarineEvents().setStart(this);
		if (primaryStage.getTitle() == null || primaryStage.getTitle().equals("")) {
			primaryStage.setTitle(defaultName);
		}

		primaryStage.show();
	}
}

package controlls;

import java.util.HashMap;

import modell.Territorium;
import modell.Roboter;
import views.Oberflaeche;
import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {
	static HashMap<String, Oberflaeche> hashOberflaeche = new HashMap<String, Oberflaeche>();

//	public static void main(String[] args){
//		launch(args);
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.application.Application#start(javafx.stage.Stage) start
	 * Methode, welche ganz zu Anfang des Programmes aufgerufen wird. Von diesem
	 * Object gehen dann alle Fenster aus und werden �berpr�ft.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		LoadAndSaveCode.verzeichnissCheck();
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
			primaryStage.setTitle(LoadAndSaveCode.defaultName);
		}
		hashOberflaeche.put(primaryStage.getTitle(), oberflaeche);
		primaryStage.show();
	}

	/*
	 * Eine Liste welche immer aktuell gehalten wird, dar�ber welche Fenster
	 * schon offen sind
	 */
	public void replaceStringKey(String oldName, String newName) {
		Oberflaeche o = hashOberflaeche.get(oldName);
		hashOberflaeche.remove(oldName);
		hashOberflaeche.put(newName, o);
	}

	public boolean isOberflaecheVorhanden(String name){
		return hashOberflaeche.containsKey(name);
	}

	public Oberflaeche getOberflaecheByKey(String name){
		return hashOberflaeche.get(name);
	}

	/*
	 * schaut nach ob das geschlossene Fenster auch in der Liste war und l�scht
	 * es dann
	 */
	public void onClose(Oberflaeche o) {
		if (hashOberflaeche.containsKey(o.getPrimaryStage().getTitle())) {
			hashOberflaeche.remove(o.getPrimaryStage().getTitle());
		} else {
			// Fehler werfen oder sowas..
		}
	}
}

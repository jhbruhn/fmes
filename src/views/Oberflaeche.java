package views;

import java.util.ArrayList;

import controlls.AnimationController;
import controlls.LoadAndSaveCode;
import controlls.LoadAndSaveCode.Picture;
import controlls.RunCodeController;
import controlls.SubmarineEvents;
import controlls.SubmarineMouseHandler;
import controlls.TextAreaControls;
import modell.ImageCombo;
import modell.Internationalitaet;
import modell.ScAchse;
import modell.Territorium;
import modell.Territorium.FeldEigenschaft;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Oberflaeche {

	public final static int CANVAS_RAND_ABSTAND = 10;

	// ObermenuPunkte erstellen
	private Menu fileMenu;
	private Menu bearbeitenMenu;
	private Menu uBoot;
	private Menu simulationsMenu;
	private Menu exampleMenu;
	private Menu helpMenu;

	// MenuUnterpunkte im Menu "Datei"
	private MenuItem newProjektMenuItem;
	private MenuItem openProjectMenuItem;
	private MenuItem druckenCodeMenuItem;
	private Menu spracheMenuItem;
	private ToggleGroup toggleSprache;
	private RadioMenuItem englishSprache;
	private RadioMenuItem deutschSprache;
	private MenuItem quitMenuItem;

	// MenuUnterpunkte im Menu "Bearbeiten"
	private Menu subMenuSave;
	private MenuItem xmlSave;
	private MenuItem jaxbSave;
	private MenuItem serialisierenSave;

	private Menu subMenuLoad;
	private MenuItem xmlLoad;
	private MenuItem jaxbLoad;
	private MenuItem deserialisierenLoad;

	private Menu subMenuPicture;
	private MenuItem pngSaveAs;
	private MenuItem jpgSaveAs;

	private MenuItem druckenSpielMenuItem;
	private MenuItem groeßeAendernMenuItem;
	private MenuItem resizeableMenuItem;

	// ToggleGroup f�r die folgenden RadioMenuItems erstellen, welche die
	// Auswahl im Menu sp�ter managed
	private ToggleGroup toggleBearbeitenSpielfeld;
	private RadioMenuItem submarine;
	private RadioMenuItem felsen;
	private RadioMenuItem leuchtFelsen;
	private RadioMenuItem batterie;
	private RadioMenuItem hai;
	private RadioMenuItem zielFeld;
	private RadioMenuItem deleteBefehl;

	// MenuUnterpunkte im Menu "U-Boot"
	private MenuItem vorMenuItem;
	private MenuItem rueckMenuItem;
	private MenuItem linksMenuItem;
	private MenuItem rechtsMenuItem;

	// MenuUnterpunkte im Menupunkt "Simulation"
	private MenuItem startMenuItem;
	private MenuItem pauseMenuItem;
	private MenuItem stopMenuItem;

	// MenuUnterpunkte im MenuPunkt "Beispiele"
	private MenuItem speichernBeispiel;
	private MenuItem ladenBeispiel;

	// MenuUnterpunkte im Menupunkt "Hilfe"
	private MenuItem hinweisMenuItem;
	private MenuItem hilfeMenuItem;

	// Button deklarationen etc.
	// Button neues Dokument
	private Button buttonNeuesDokument;

	// Button �ffne Dokument
	private Button buttonOeffneDokument;

	// Button speichern
	private Button buttonSpeichernDokument;

	// Button Vor
	private Button buttonUp;

	// Button Links
	private Button buttonLeft;

	// Button Rueck
	private Button buttonDown;

	// Button Rechts
	private Button buttonRight;

	// Button Start
	private Button buttonStart;

	// Button pausieren
	private Button buttonPause;

	// Button stoppen
	private Button buttonStop;

	// Button Hinweis
	private Button buttonHinweis;

	// ComboBox f�r die sonst recht un�bersichtlichen und oft �berfl�ssigen
	// Buttons
	private ComboBox<ImageView> comboboxButtonBearbeitenAuswahl;

	// Slider f�r die Toolbar
	private Slider slider;

	// Label f�r unten
	private Label labelBottom;

	// die SplitPane in welcher nachher der Code-Bereich und das Spiel liegen
	private SplitPane split;

	// Unser Akteur, das dicke fette UBOOT,
	// "HEY, ich bin nicht irgend ein dickes fettes UBOOT! Ich bin DAS dicke
	// fette UBOOT!!"
	private Territorium territorium;

	// Code-Text-Feld
	private TextArea code;

	// Wave Dateien
	// Audioclip f�r die Hilfe-Leiste
	private AudioClip clip;

	// main Border, in welches alles gesteckt wird
	private BorderPane border;

	// Inneres Border, in welchem das Spiel und die Buttons sind
	private BorderPane innerborder;

	// Internationalit�t l�sst einen die Sprache w�hlen/wechseln
	private Internationalitaet international;

	// Eventh�ndler f�r alle Aktionen die gemacht werden
	private SubmarineEvents sbEvents;

	// ScrollPane f�r das Game
	public ScrollPane sc;

	// Achsen f�r die ScrollPane!
	public ScAchse scWidth;
	public ScAchse scHeigth;

	// TerritoriumPanel f�r das Game
	public TerritoriumPanel territoriumPanel;

	// ArrayList um alle RadioMenuItems zu deselecten wenn n�tig
	public ArrayList<RadioMenuItem> al;

	// F�r die Dragg and Drop Aktionen der Maus in sc
	public SubmarineMouseHandler scMousehandler;

	// Speicherte die Stage mit der das hier ge�ffnet wird, um sie beim
	// schlie�en aufrufen zu k�nnen
	private Stage primaryStage;

	// boolean welcher das Speichern vor dem schlie�en abpr�ft
	private boolean textChanged;

	// als Feld zum aufz�hlen der geschriebenen Codezeilen
	private TextFlow flow;

	// Kommen der Code und der TextFlow f�r den Linecount rein
	private BorderPane borderPaneForCodeField;

	// H�lle f�r den TextFlow zum Linecount
	private ScrollPane scrollPaneLineCount;

	// Ist nur f�r das sch�ne aussehen da..
	private StackPane root;

	// Um den Code zum laufen zu bringen
	private RunCodeController runcode;

	// Controlliert alle TextArea-Ereignisse
	private TextAreaControls textAreaControls;

	// Controller f�r die Aninamtionen wird hier selber nicht gebraucht in
	// Oberflaeche, ist nur zum Initialieseren hier
	private AnimationController animation;
	
	/*
	 * initialisiert alle Werte/Variablen/Atribute
	 */
	public void initialisieren() {
		international = new Internationalitaet(null);
		sbEvents = new SubmarineEvents(getTerritorium());
		fileMenu = new Menu();
		bearbeitenMenu = new Menu();
		uBoot = new Menu();
		exampleMenu = new Menu();
		newProjektMenuItem = new MenuItem();
		openProjectMenuItem = new MenuItem();
		druckenCodeMenuItem = new MenuItem();
		spracheMenuItem = new Menu();
		toggleSprache = new ToggleGroup();
		englishSprache = new RadioMenuItem();
		deutschSprache = new RadioMenuItem();
		quitMenuItem = new MenuItem();
		simulationsMenu = new Menu();
		helpMenu = new Menu();
		subMenuSave = new Menu();
		xmlSave = new MenuItem();
		jaxbSave = new MenuItem();
		serialisierenSave = new MenuItem();
		subMenuLoad = new Menu();
		xmlLoad = new MenuItem();
		jaxbLoad = new MenuItem();
		deserialisierenLoad = new MenuItem();
		subMenuPicture = new Menu();
		pngSaveAs = new MenuItem();
		jpgSaveAs = new MenuItem();
		druckenSpielMenuItem = new MenuItem();
		groeßeAendernMenuItem = new MenuItem();
		resizeableMenuItem = new MenuItem();
		toggleBearbeitenSpielfeld = new ToggleGroup();
		submarine = new RadioMenuItem();
		felsen = new RadioMenuItem();
		leuchtFelsen = new RadioMenuItem();
		batterie = new RadioMenuItem();
		hai = new RadioMenuItem();
		zielFeld = new RadioMenuItem();
		deleteBefehl = new RadioMenuItem();
		vorMenuItem = new MenuItem();
		rueckMenuItem = new MenuItem();
		linksMenuItem = new MenuItem();
		rechtsMenuItem = new MenuItem();
		startMenuItem = new MenuItem();
		pauseMenuItem = new MenuItem();
		stopMenuItem = new MenuItem();
		speichernBeispiel = new MenuItem();
		ladenBeispiel = new MenuItem();
		hinweisMenuItem = new MenuItem();
		hilfeMenuItem = new MenuItem();
		toggleBearbeitenSpielfeld = new ToggleGroup();
		buttonNeuesDokument = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/new_fileSmall.png"))));
		buttonOeffneDokument = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/openSmall.png"))));
		buttonSpeichernDokument = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/saveSmall.png"))));
		buttonUp = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsUpSmall.png"))));
		buttonLeft = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsLeftSmall.png"))));
		buttonDown = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsDownSmall.png"))));
		buttonRight = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsRightSmall.png"))));
		buttonStart = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		buttonPause = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/pauseSmall.png"))));
		buttonStop = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
		buttonHinweis = new Button("", new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/fragezeichenSmall.png"))));
		comboboxButtonBearbeitenAuswahl = new ComboBox<ImageView>();
		slider = new Slider();
		labelBottom = new Label();
		split = new SplitPane();
		code = new TextArea();
		clip = new AudioClip(
				getClass().getResource("../resourcesPicturesAndSoundsVidoes/BabyCryingSounds.wav").toString());
		border = new BorderPane();
		innerborder = new BorderPane();
		al = new ArrayList<RadioMenuItem>();
		al.add(submarine);
		al.add(felsen);
		al.add(leuchtFelsen);
		al.add(batterie);
		al.add(hai);
		al.add(zielFeld);
		al.add(deleteBefehl);
		scWidth = new ScAchse();
		scHeigth = new ScAchse();
		setTextChanged(false);
		flow = new TextFlow();
		scMousehandler = new SubmarineMouseHandler(getTerritorium(), sbEvents, international.getRb(),
				getPrimaryStage());
		RadioMenuItem[] m = new RadioMenuItem[7];
		m[0] = submarine;
		m[1] = felsen;
		m[2] = leuchtFelsen;
		m[3] = zielFeld;
		m[4] = deleteBefehl;
		m[5] = hai;
		m[6] = batterie;
		animation = new AnimationController(m, comboboxButtonBearbeitenAuswahl, labelBottom ,getInternationalitaet().getRb());
		territoriumPanel = new TerritoriumPanel(getTerritorium(), scWidth, scHeigth, animation);
		borderPaneForCodeField = new BorderPane();
		scrollPaneLineCount = new ScrollPane(flow);
		root = new StackPane();
		zuBeachtendeButtonsUndMenuItems();
		textAreaControls = new TextAreaControls(getTerritorium(), getTextArea(), this, flow);
	}

	/*
	 * Speichert die sp�ter zu beachtenden MenuItems und Buttons beim Thread
	 */
	private void zuBeachtendeButtonsUndMenuItems() {
		ArrayList<Button> b = new ArrayList<Button>();
		b.add(buttonOeffneDokument);
		ArrayList<MenuItem> m = new ArrayList<MenuItem>();
		m.add(ladenBeispiel);
		m.add(xmlLoad);
		m.add(jaxbLoad);
		m.add(deserialisierenLoad);
		m.add(pngSaveAs);
		m.add(jpgSaveAs);
		m.add(ladenBeispiel);
		m.add(openProjectMenuItem);
		setRuncode(new RunCodeController(territorium, slider, buttonStart, buttonStop, buttonPause, startMenuItem,
				stopMenuItem, pauseMenuItem, this, b, m));
	}

	/*
	 * Zusammenbau aller Einzelteile und �ffnen/sichtbar machen des Fensters
	 */
	public Oberflaeche(Stage stage, Territorium t) throws Exception {
		setPrimaryStage(stage);

		setTerritorium(t);
		initialisieren();

		sc = new ScrollPane(territoriumPanel.getScrollPane());

		borderPaneForCodeField.setCenter(code);
		borderPaneForCodeField.setRight(scrollPaneLineCount);
		// nervige Scrollbars ausblenden, da diese im sp�teren Verlauf nicht
		// mehr n�tig sind.
		scrollPaneLineCount.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPaneLineCount.setVbarPolicy(ScrollBarPolicy.NEVER);
		// Die folgenden Zahlen sind nur nach pers�nlichem Empfinden gesetzt
		borderPaneForCodeField.setMinWidth(175);
		borderPaneForCodeField.setMaxWidth(300);

		split.getItems().addAll(borderPaneForCodeField, sc);
		split.setDividerPosition(0, 0.3);

		// Hinzuf�gen der Komponenten des inneren Borders
		innerborder.setCenter(split);
		innerborder.setTop(addToolBar());

		// Hinzuf�gen des Menus, des Labels und des inneren Borders zum
		// main-Border
		border.setTop(addMenuBar());
		border.setBottom(addLabel());
		border.setCenter(innerborder);

		root.getChildren().add(border);

		// scrollPaneLineCount.Property
		// Hier noch mal die explizieten Sch�nheits�nderungen, welche nicht alle
		// gleich in der "CSS"-Datei sind
		territoriumPanel.getScrollPane().setStyle("-fx-background: #6680e6; -fx-background-color: #6680e6;");
		root.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #4d66cc, #001a80)");

		// Hat keinen gr�en Effekt au�er, dass sie nicht transparent wird.
		// Sollte man sich jedoch entscheiden, den "Disable" raus zu nehmen, ist
		// die Farbe wieder "sch�n" gew�hlt.
		scrollPaneLineCount.setStyle("-fx-background: #ffff99;");
		scrollPaneLineCount.setDisable(true);
		// scrollPaneLineCount.

		Scene scene = new Scene(root, 1250, 750);
		scene.getStylesheets().add(Oberflaeche.class.getResource("../resources/Style.txt").toExternalForm());
		primaryStage.setScene(scene);
	}

	/*
	 * Erstellt die ToolBar, in welche alle Buttons zur Bedienung kommen Mit der
	 * ToolBar wird die Bedienung des Spiels und die generelle Benutzung
	 * vereinfacht
	 * 
	 * @return toolBar, gibt die vorgegebene ToolBar zur�ck
	 */
	private ToolBar addToolBar() {

		ToolBar toolBar = new ToolBar();
		toolBar.setPadding(new Insets(10, 10, 10, 10));
		// toolBar.setSpacing(10); // Gap between nodes
		toolBar.setStyle("-fx-background-color: transparent");

		// Gr��en festlegen, der Buttons
		buttonStart.setPrefSize(Territorium.OBJ_WIDTH, Territorium.OBJ_WIDTH);
		buttonPause.setPrefSize(Territorium.OBJ_WIDTH, Territorium.OBJ_WIDTH);
		buttonStop.setPrefSize(Territorium.OBJ_WIDTH, Territorium.OBJ_WIDTH);
		buttonSpeichernDokument.setPrefSize(Territorium.OBJ_WIDTH, Territorium.OBJ_WIDTH);
		buttonNeuesDokument.setPrefSize(Territorium.OBJ_WIDTH, Territorium.OBJ_WIDTH);

		// Erstellt eine ComboBox f�r nicht so h�ufig ben�tigte Buttons
		bearbeiteComboBoxFuerDieButtons();

		// f�gt den Buttons Tooltips hinzu, welche erkl�ren was sie machen
		setTooltipAndTexts();

		// toolbar.getChildren().addAll(buttonSubmarine, buttonFelsen,
		// buttonBatterie, buttonExitFeld, buttonDelete);
		toolBar.getItems().addAll(buttonNeuesDokument, buttonOeffneDokument, buttonSpeichernDokument, new Separator(),
				comboboxButtonBearbeitenAuswahl, new Separator(), buttonUp, buttonDown, buttonLeft,
				buttonRight, new Separator(), buttonStart, buttonPause, buttonStop,
				new Separator(), slider, new Separator(), buttonHinweis);

		return toolBar;
	}

	/*
	 * erstellt die ComboBox voller Buttons, diese Methode soll sp�ter nach
	 * Auswahl eines Button wieder aufgerufen werden, damit das Bild
	 * Aktualiesiert werden kann und die verschiedenen Auswahlen angepasst
	 * werden k�nnen. Es ist zu beachten, dass nur ein U-Boot vorhanden sein
	 * darf und auch nur ein Ziel-Feld.
	 */
	private void bearbeiteComboBoxFuerDieButtons() {

		// Je nachdem ob ein Element schon da ist, welches nur einmal vorhanden
		// sein darf, wird es hier wieder raus geworfen.

		comboboxButtonBearbeitenAuswahl.getItems().addAll(
				new ImageView(new Image(
						getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/submarineSmall.png"))),
				new ImageView(new Image(
						getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/felsenSmall.png"))),
				new ImageView(new Image(
						getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/leuchtFelsenSmall.png"))),
				new ImageView(new Image(
						getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/batterieSmall.png"))),
				new ImageView(
						new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/haiSmall.png"))),
				new ImageView(new Image(
						getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/Exit-FeldSmall.png"))),
				new ImageView(new Image(
						getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/deleteSmall.png"))));

		comboboxButtonBearbeitenAuswahl.setCellFactory(new ImageCombo());

		comboboxButtonBearbeitenAuswahl.setMinWidth(Territorium.OBJ_WIDTH * 2.5);
		comboboxButtonBearbeitenAuswahl.setPrefSize(Territorium.OBJ_WIDTH * 2.5, Territorium.OBJ_WIDTH * 1.5);
	}

	/*
	 * Erstellt die MenuBar des Spiels dabei werden die folgenden Punkte
	 * eingebunden: Datei, Bearbeiten, U-Boot, Simulation und Hilfe Die
	 * Untermen�s k�nnen teilwesie mit Shortcuts aufgerufen werden insgesamt
	 * dient das Menu zu m�glichen Steuerung des Spiels
	 * 
	 * @return MenuBar, gibt die MenuBar des Spiels zur�ck
	 */
	private MenuBar addMenuBar() {

		// MenuBar erstellen
		MenuBar menu = new MenuBar();

		// Gibt den Buttons/MenuItems ihre F�higkeiten etc.
		placeControlsOnActions();

		// Images an die MenuItems heften
		setImagesOnMenuItems();

		// toggleGroup zuweisen
		submarine.setToggleGroup(toggleBearbeitenSpielfeld);
		felsen.setToggleGroup(toggleBearbeitenSpielfeld);
		leuchtFelsen.setToggleGroup(toggleBearbeitenSpielfeld);
		batterie.setToggleGroup(toggleBearbeitenSpielfeld);
		hai.setToggleGroup(toggleBearbeitenSpielfeld);
		zielFeld.setToggleGroup(toggleBearbeitenSpielfeld);
		deleteBefehl.setToggleGroup(toggleBearbeitenSpielfeld);

		deutschSprache.setToggleGroup(toggleSprache);
		englishSprache.setToggleGroup(toggleSprache);
		if (international.getLanguage().equals("de")) {
			toggleSprache.selectToggle(deutschSprache);
		} else {
			toggleSprache.selectToggle(englishSprache);
		}

		// SubMenus zusammenstellen
		spracheMenuItem.getItems().addAll(englishSprache, deutschSprache);
		subMenuSave.getItems().addAll(xmlSave, jaxbSave, serialisierenSave);
		subMenuLoad.getItems().addAll(xmlLoad, jaxbLoad, deserialisierenLoad);
		subMenuPicture.getItems().addAll(pngSaveAs, jpgSaveAs);

		// Hinzuf�gen der MenuUnterpunkte zum MenuPunkt
		fileMenu.getItems().addAll(newProjektMenuItem, openProjectMenuItem, new SeparatorMenuItem(),
				druckenCodeMenuItem, spracheMenuItem, new SeparatorMenuItem(), quitMenuItem);
		bearbeitenMenu.getItems().addAll(subMenuSave, subMenuLoad, subMenuPicture, druckenSpielMenuItem,
				groeßeAendernMenuItem, new SeparatorMenuItem(), resizeableMenuItem, new SeparatorMenuItem(), submarine,
				felsen, leuchtFelsen, batterie, hai, zielFeld, deleteBefehl, new SeparatorMenuItem());
		uBoot.getItems().addAll(vorMenuItem, rueckMenuItem, linksMenuItem, rechtsMenuItem);
		simulationsMenu.getItems().addAll(startMenuItem, pauseMenuItem, stopMenuItem);
		exampleMenu.getItems().addAll(speichernBeispiel, ladenBeispiel);
		helpMenu.getItems().addAll(hinweisMenuItem, hilfeMenuItem);

		// Hinzuf�gen der MenuPunkte zur MenuBar
		menu.getMenus().addAll(fileMenu, bearbeitenMenu, uBoot, simulationsMenu, exampleMenu, helpMenu);

		return menu;
	}

	/*
	 * Eine einigerma�en zuf�llige Nachricht im Label
	 * 
	 * @return label, gibt momentane Dankes-Gedanken des Autors wieder
	 */
	private Label addLabel() {
		String s = "Ich danke ";
		int i = (int) (Math.random() * 100);
		// Schwere Auswahl eines Dankestextes, deswegen w�hle ich zuf�llig
		if (i < 10) {
			s += "meiner Mutter";
		} else if (i < 20) {
			s += "meinem Vater";
		} else if (i < 30) {
			s += "meiner Awesomeness";
		} else if (i < 40) {
			s += "meinen Omas";
		} else if (i < 50) {
			s = "Don*t go Trump";
		} else if (i < 60) {
			s += "dem Weihnachtsmann";
		} else if (i < 70) {
			s += "meinen Freunden, the real MVP";
		} else if (i < 80) {
			s += "Keksen, wie ich sie liebe";
		} else if (i < 90) {
			s += "daf�r das ich nicht schwanger werden kann, thx Jesus";
		} else if (i < 98) {
			s += "das es Animes gibt";
		} else {
			s += "the *appening, daf�r meine Inspiration f�r alles zu sein";
		}
		labelBottom.setText(s);
		labelBottom.setTextFill(Color.WHITE);
		return labelBottom;
	}

	/*
	 * Setzt alle Aktions die passieren k�nnen wenn ein Button oder ein MenuItem
	 * gedr�ckt wird. Dabei werden zus�tzlich noch Short-Cuts vergeben.
	 */
	private void placeControlsOnActions() {
		// holy moly es funktioniert. diese Rechnung wird nicht immer klappen
		// und ist fest an die Schriftbreite gebunden, jedoch werden auf weitere
		// Binds verzichtet was die Schriftg��e angeht, da es hie rnicht zu
		// kompliziert werden soll und ich das ganze vielleicht noch mal
		// erkl�ren mus..
		// Hier ist 17 der Wert f�r die SChriftgr��e (14) + Zeilenabst�nde
		code.scrollTopProperty().addListener(l -> {
			if (code.scrollTopProperty().getValue() > 0) {
				String s[] = getTextArea().getText().split("\n");
				double berechnung = s.length * 17 - (code.heightProperty().get() - (2 * 17));
				scrollPaneLineCount.vvalueProperty().setValue(code.scrollTopProperty().getValue() / berechnung);
			} else {
				scrollPaneLineCount.vvalueProperty().setValue(0);
			}
		});

		// Erm�glicht durch das binding, dass das Spiel immer in die Mitte
		// gesetzt wird.
		scWidth.valueProperty().bind(sc.widthProperty());
		scHeigth.valueProperty().bind(sc.heightProperty());

		scWidth.valueProperty().addListener(c -> getTerritorium().checkIfChanged());
		scHeigth.valueProperty().addListener(c -> getTerritorium().checkIfChanged());

		/*
		 * Macht das ich keinen Gehirnschaden mehr beim Coden, folglich werden
		 * auf jede ge�ffnete Klammer auch eine geschlossene gesetzt. ( -> ) {
		 * -> } " -> " Erg�nzungen durchs Programm, welche einem vieles
		 * erleichtern.. Sollte dieses einmal nicht hinhauen, befindet sich dann
		 * ein # im Code
		 * 
		 * Weiterhin werden alle Controlls gesetzt, welche einfach alles
		 * vereinfachen..
		 * 
		 * Siehe textAreaControls f�r mehr Infos
		 */
		textAreaControls.setzeEventsZurTextArea();

		// MouseHandler f�r das Draggen und Dropen hinzuf�gen
		scMousehandler.handlerSetzen(territoriumPanel.getCanvas());

		// Aktionen im Men� "Datei"
		// Unterpunkt "Neu"
		newProjektMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		sbEvents.neuesFensterOeffnen(newProjektMenuItem, buttonNeuesDokument, getInternationalitaet().getRb());

		// Unterpunkt "�ffnen"
		openProjectMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openProjectMenuItem.setOnAction(e -> {
			sbEvents.ladeCode(code, getPrimaryStage());
		});
		buttonOeffneDokument.setOnAction(e -> openProjectMenuItem.fire());

		// Button Speichern
		sbEvents.speichernEvent(buttonSpeichernDokument, code, getPrimaryStage(), international.getRb(),
				getTerritorium().getUboot());

		// Unterpunkt "Drucken"
		druckenCodeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		druckenCodeMenuItem.setOnAction(e -> sbEvents.print(getTextArea()));

		// Untermenu Sprache:
		// Unterpunkt Deutsch:
		deutschSprache.setOnAction(e -> {
			if (sbEvents.spracheAendern("de", international)) {
				setTooltipAndTexts();
			}
		});

		// Unterpunkt Englisch
		englishSprache.setOnAction(e -> {
			if (sbEvents.spracheAendern("en", international)) {
				setTooltipAndTexts();
			}
		});

		// Unterpunkt "Schlie�en"
		// Achtung: der Thread wird in jedem Fall geschlossen, schlicht aus
		// Vorsicht. Man k�nnte hier Argummentieren, dass dieses nicht notwendig
		// ist, aber wahrscheinlich hast du auch noch keinen Server abrauchen
		// lassen, wenn du das sagst.. SAFTY FIRST, nicht ohne Gummie!
		quitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		quitMenuItem.setOnAction(e -> {
			stopMenuItem.fire();
			sbEvents.closeEvent(getPrimaryStage(), getTextArea(), international.getRb(), this);
		});
		getPrimaryStage().setOnCloseRequest(event -> {
			stopMenuItem.fire();
			sbEvents.closeEvent(getPrimaryStage(), getTextArea(), international.getRb(), this);
			event.consume();
		});

		// Aktionen im Menu "Bearbeiten"
		// Unterpunkt "Speichern"
		// Submenu "XML"
		xmlSave.setOnAction(e -> LoadAndSaveCode.xmlSpeichern(getTerritorium(), getPrimaryStage().getTitle()));

		// Submenu "JAXB"
		jaxbSave.setOnAction(e -> LoadAndSaveCode.jaxbSpeichern(getTerritorium(), getPrimaryStage().getTitle()));

		// Submenu "Serialisieren"
		serialisierenSave
				.setOnAction(e -> LoadAndSaveCode.serialisieren(getTerritorium(), getPrimaryStage().getTitle()));

		// Unterpunkt "Laden"
		// Submenu "XML"
		xmlLoad.setOnAction(e -> LoadAndSaveCode.xmlLaden(getTerritorium()));

		// Submenu "JAXB"
		jaxbLoad.setOnAction(e -> LoadAndSaveCode.ladeJaxbDatei(getTerritorium(), this));

		// Submenu "Deserialisieren"
		deserialisierenLoad.setOnAction(e -> LoadAndSaveCode.deserialisieren(getTerritorium(), this));

		// Unterpunkt "Als Bild Speichern"
		// Submenu "PNG"
		sbEvents.speichereAlsBild(pngSaveAs, territoriumPanel, getPrimaryStage().getTitle(), Picture.png);

		// Submenu "JPG"
		sbEvents.speichereAlsBild(jpgSaveAs, territoriumPanel, getPrimaryStage().getTitle(), Picture.jpg);

		// Unterpunkt "Spiel drucken"
		druckenSpielMenuItem.setOnAction(
				e -> sbEvents.print(getTerritorium(), scWidth, scHeigth, territoriumPanel.getCanvas(), animation));

		// Unterpunkt "Gr��e �ndern"
		groeßeAendernMenuItem.setOnAction(e -> sbEvents.changeSizeEventHandler(international.getRb()));

		// Unterpunkt "Gr��e Anpassen an/aus"
		resizeableMenuItem.setOnAction(e -> {
			getTerritorium().setResizeable(!getTerritorium().isResizeable());
			rezisableImageCheck();
		});

		/*
		 * ComboBox �ber PropertyListener mit den RadioMenuItems verbunden.
		 */
		comboboxButtonBearbeitenAuswahl.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					switch (comboboxButtonBearbeitenAuswahl.getSelectionModel().getSelectedIndex()) {
					case 0:
						submarine.setSelected(true);
						submarine.fire();
						break;
					case 1:
						felsen.setSelected(true);
						felsen.fire();
						break;
					case 2:
						leuchtFelsen.setSelected(true);
						leuchtFelsen.fire();
						break;
					case 3:
						batterie.setSelected(true);
						batterie.fire();
						break;
					case 4:
						hai.setSelected(true);
						hai.fire();
						break;
					case 5:
						zielFeld.setSelected(true);
						zielFeld.fire();
						break;
					case 6:
						deleteBefehl.setSelected(true);
						deleteBefehl.fire();
						break;
					default:
						comboboxButtonBearbeitenAuswahl.getSelectionModel().select(-1);
						break;
					}
					comboboxButtonBearbeitenAuswahl.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (event.getClickCount() == 2) {
								switch (comboboxButtonBearbeitenAuswahl.getSelectionModel().getSelectedIndex()) {
								case 0:
									submarine.setSelected(false);
									break;
								case 1:
									felsen.setSelected(false);
									break;
								case 2:
									leuchtFelsen.setSelected(false);
									break;
								case 3:
									batterie.setSelected(false);
									break;
								case 4:
									hai.setSelected(false);
									break;
								case 5:
									zielFeld.setSelected(false);
									break;
								case 6:
									deleteBefehl.setSelected(false);
									break;
								default:
									break;
								}
								comboboxButtonBearbeitenAuswahl.getSelectionModel().select(-1);
							}
						}
					});
				});

		// Unterpunkt "Submarine"
		sbEvents.setzeObjektEventCode(null, territoriumPanel, submarine, 0, comboboxButtonBearbeitenAuswahl);

		// Unterpunkt "Felsen"
		sbEvents.setzeObjektEventCode(FeldEigenschaft.Felsen, territoriumPanel, felsen, 1,
				comboboxButtonBearbeitenAuswahl);

		// Unterpunkt "Leucht-Felsen"
		sbEvents.setzeObjektEventCode(FeldEigenschaft.LeuchtFelsen, territoriumPanel, leuchtFelsen, 2,
				comboboxButtonBearbeitenAuswahl);

		// Unterpunkt "Batterie"
		sbEvents.setzeObjektEventCode(FeldEigenschaft.Batterie, territoriumPanel, batterie, 3,
				comboboxButtonBearbeitenAuswahl);

		// Unterpunkt "Hai-Fisch"
		sbEvents.setzeObjektEventCode(FeldEigenschaft.Hai, territoriumPanel, hai, 4, comboboxButtonBearbeitenAuswahl);

		// Unterpunkt "Zielfeld"
		sbEvents.setzeObjektEventCode(FeldEigenschaft.ZielFeld, territoriumPanel, zielFeld, 5,
				comboboxButtonBearbeitenAuswahl);

		// Unterpunkt "Delete"
		sbEvents.setzeObjektEventCode(FeldEigenschaft.Leer, territoriumPanel, deleteBefehl, 6,
				comboboxButtonBearbeitenAuswahl);

		// Aktionen im Menu "U-Boot"
		// Unterpunkt "Vor"
		vorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
		sbEvents.menuItemsButtonsFuerSubmarineAktions(vorMenuItem, buttonUp, 'v');

		// Unterpunkt "Rueck"
		rueckMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		sbEvents.menuItemsButtonsFuerSubmarineAktions(rueckMenuItem, buttonDown, 'b');

		// Unterpunkt "Links"
		linksMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
		sbEvents.menuItemsButtonsFuerSubmarineAktions(linksMenuItem, buttonLeft, 'l');

		// Unterpunkt "Rechts"
		rechtsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
		sbEvents.menuItemsButtonsFuerSubmarineAktions(rechtsMenuItem, buttonRight, 'r');

		// Aktionen im Menu "Simulation"
		// Unterpunkt "start"
		startMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11, KeyCombination.CONTROL_DOWN));

		// Unterpunkt "stop"
		stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12, KeyCombination.CONTROL_DOWN));

		// Unterpunkt "Pause"
		/*
		 * Pause, Start und Stop werden in RunCodeController selbst bearbeitet
		 */

		// Aktionen im Menu "Beispiele"
		// Unterpunkt "speichernBeispiel"
		speichernBeispiel.setOnAction(e -> sbEvents.saveExample(getInternationalitaet().getRb(), getTerritorium(),
				getTextArea(), getPrimaryStage().getTitle()));

		// Unterpunkt "ladenBeispiel"
		ladenBeispiel.setOnAction(e -> sbEvents.loadExample(getInternationalitaet().getRb(), getTextArea()));

		// Aktionen im Menu "Hilfe"
		// Unterpunkt "Hinweis"
		hinweisMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
		hinweisMenuItem.setOnAction(e -> sbEvents.oeffneHilfeFenster(international.getRb()));
		buttonHinweis.setOnAction(e -> hinweisMenuItem.fire());

		// Unterpunkt "Hilfe"
		hilfeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
		hilfeMenuItem.setOnAction(e -> clip.play(1.0));
	}

	/*
	 * F�gt dem MenuItems die zutreffende Images hinzu
	 */
	private void setImagesOnMenuItems() {
		// Vorher noch eben das Logo xD
		getPrimaryStage().getIcons().add(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/submarineSmall.png")));

		// Images in die MenuLeiste einf�gen

		// Datei-Images
		newProjektMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/new_fileVerySmall.png"))));
		openProjectMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/openVerySmall.png"))));
		druckenCodeMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/printerVerySmall.png"))));
		spracheMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/languageVerySmall.png"))));
		deutschSprache.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/germanVerySmall.png"))));
		englishSprache.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/englishVerySmall.png"))));
		quitMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/cancel.png"))));

		// Bearbeiten-Images
		subMenuSave.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/saveVerySmall.png"))));
		xmlSave.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/xmlVerySmall.png"))));
		jaxbSave.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/jaxbVerySmall.png"))));
		serialisierenSave.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/serialVerySmall.png"))));
		subMenuLoad.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/openVerySmall.png"))));
		xmlLoad.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/xmlVerySmall.png"))));
		jaxbLoad.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/jaxbVerySmall.png"))));
		deserialisierenLoad.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/deserialVerySmall.png"))));
		subMenuPicture.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/pictureVerySmall.png"))));
		pngSaveAs.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/pngVerySmall.png"))));
		jpgSaveAs.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/jpgVerySmall.png"))));
		druckenSpielMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/printerVerySmall.png"))));
		groeßeAendernMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/sizeVerySmall.png"))));
		rezisableImageCheck();
		submarine.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/submarineVerySmall.png"))));
		felsen.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/felsenVerySmall.png"))));
		leuchtFelsen.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/leuchtFelsenVerySmall.png"))));
		batterie.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/batterieVerySmall.png"))));
		hai.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/haiVerySmall.png"))));
		zielFeld.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/Exit-FeldVerySmall.png"))));
		deleteBefehl.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/deleteVerySmall.png"))));

		// U-Boot-Images
		vorMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsUpVerySmall.png"))));
		linksMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsLeftVerySmall.png"))));
		rechtsMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsDownVerySmall.png"))));
		rueckMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/arrowsRightVerySmall.png"))));

		// Simulation-Images
		startMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startVerySmall.png"))));
		pauseMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/pauseVerySmall.png"))));
		stopMenuItem.setGraphic(new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopVerySmall.png"))));

		// Hilfe-Images
		hinweisMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/fragezeichenVerySmall.png"))));
		hilfeMenuItem.setGraphic(new ImageView(new Image(
				getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/ausrufezeichenVerySmall.png"))));
	}

	/*
	 * Gibt den Button Tooltips, welche erkl�ren was ein Dr�cken des Button
	 * bewirkt.
	 */
	private void setTooltipAndTexts() {
		fileMenu.setText(international.getRb().getString("fileMenu"));
		bearbeitenMenu.setText(international.getRb().getString("bearbeitenMenu"));
		uBoot.setText(international.getRb().getString("uBoot"));
		newProjektMenuItem.setText(international.getRb().getString("newProjektMenuItem"));
		openProjectMenuItem.setText(international.getRb().getString("openProjectMenuItem"));
		druckenCodeMenuItem.setText(international.getRb().getString("druckenCodeMenuItem"));
		spracheMenuItem.setText(international.getRb().getString("spracheMenuItem"));
		englishSprache.setText(international.getRb().getString("englishSprache"));
		deutschSprache.setText(international.getRb().getString("deutschSprache"));
		quitMenuItem.setText(international.getRb().getString("quitMenuItem"));
		simulationsMenu.setText(international.getRb().getString("simulationsMenu"));
		exampleMenu.setText(international.getRb().getString("exampleMenu"));
		helpMenu.setText(international.getRb().getString("helpMenu"));
		subMenuSave.setText(international.getRb().getString("subMenuSave"));
		xmlSave.setText(international.getRb().getString("xmlSave"));
		jaxbSave.setText(international.getRb().getString("jaxbSave"));
		serialisierenSave.setText(international.getRb().getString("serialisierenSave"));
		subMenuLoad.setText(international.getRb().getString("subMenuLoad"));
		xmlLoad.setText(international.getRb().getString("xmlLoad"));
		jaxbLoad.setText(international.getRb().getString("jaxbLoad"));
		deserialisierenLoad.setText(international.getRb().getString("deserialisierenLoad"));
		subMenuPicture.setText(international.getRb().getString("subMenuPicture"));
		pngSaveAs.setText(international.getRb().getString("pngSaveAs"));
		jpgSaveAs.setText(international.getRb().getString("jpgSaveAs"));
		druckenSpielMenuItem.setText(international.getRb().getString("druckenSpielMenuItem"));
		groeßeAendernMenuItem.setText(international.getRb().getString("groeßeAendernMenuItem"));
		resizeableMenuItem.setText(international.getRb().getString("resizeableMenuItem"));
		submarine.setText(international.getRb().getString("submarine"));
		felsen.setText(international.getRb().getString("felsen"));
		leuchtFelsen.setText(international.getRb().getString("leuchtFelsen"));
		batterie.setText(international.getRb().getString("batterie"));
		hai.setText(international.getRb().getString("hai"));
		zielFeld.setText(international.getRb().getString("zielFeld"));
		deleteBefehl.setText(international.getRb().getString("deleteBefehl"));
		vorMenuItem.setText(international.getRb().getString("vorMenuItem"));
		rueckMenuItem.setText(international.getRb().getString("rueckMenuItem"));
		linksMenuItem.setText(international.getRb().getString("linksMenuItem"));
		rechtsMenuItem.setText(international.getRb().getString("rechtsMenuItem"));
		startMenuItem.setText(international.getRb().getString("startMenuItem"));
		pauseMenuItem.setText(international.getRb().getString("pauseMenuItem"));
		stopMenuItem.setText(international.getRb().getString("stopMenuItem"));
		speichernBeispiel.setText(international.getRb().getString("exampleSave"));
		ladenBeispiel.setText(international.getRb().getString("exampleLoad"));
		hinweisMenuItem.setText(international.getRb().getString("hinweisMenuItem"));
		hilfeMenuItem.setText(international.getRb().getString("hilfeMenuItem"));
		buttonNeuesDokument.setTooltip(new Tooltip(international.getRb().getString("buttonNeuesDokument")));
		buttonOeffneDokument.setTooltip(new Tooltip(international.getRb().getString("buttonOeffneDokument")));
		buttonSpeichernDokument.setTooltip(new Tooltip(international.getRb().getString("buttonSpeichernDokument")));

		buttonUp.setTooltip(new Tooltip(international.getRb().getString("buttonUp")));
		buttonRight.setTooltip(new Tooltip(international.getRb().getString("buttonRight")));
		buttonLeft.setTooltip(new Tooltip(international.getRb().getString("buttonLeft")));
		buttonDown.setTooltip(new Tooltip(international.getRb().getString("buttonDown")));

		buttonStart.setTooltip(new Tooltip(international.getRb().getString("buttonStart")));
		buttonStop.setTooltip(new Tooltip(international.getRb().getString("buttonStop")));
		buttonPause.setTooltip(new Tooltip(international.getRb().getString("buttonPause")));

		buttonHinweis.setTooltip(new Tooltip(international.getRb().getString("buttonHinweis")));
	}

	public SubmarineEvents getSubmarineEvents() {
		return sbEvents;
	}

	public void setSubmarineEvents(SubmarineEvents s) {
		sbEvents = s;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage s) {
		primaryStage = s;
	}

	public Internationalitaet getInternationalitaet() {
		return international;
	}

	public void setInternationalitaet(Internationalitaet s) {
		international = s;
	}

	public TextArea getTextArea() {
		return code;
	}

	public void setTextArea(TextArea a) {
		code = a;
	}

	public boolean isTextChanged() {
		return textChanged;
	}

	public void setTextChanged(boolean textChanged) {
		this.textChanged = textChanged;
	}

	public Territorium getTerritorium() {
		return territorium;
	}

	public void setTerritorium(Territorium t) {
		territorium = t;
	}

	public void rezisableImageCheck() {
		if (getTerritorium().isResizeable()) {
			resizeableMenuItem.setGraphic(new ImageView(new Image(
					getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startVerySmall.png"))));
		} else {
			resizeableMenuItem.setGraphic(new ImageView(new Image(
					getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopVerySmall.png"))));
		}
	}

	public RunCodeController getRuncode() {
		return runcode;
	}

	public void setRuncode(RunCodeController runcode) {
		this.runcode = runcode;
	}
}
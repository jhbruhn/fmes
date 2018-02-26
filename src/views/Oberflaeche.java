package views;

import java.util.ArrayList;

import controlls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import modell.*;
import modell.Territorium.FeldEigenschaft;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Oberflaeche {

    public final static int CANVAS_RAND_ABSTAND = 10;

    // ObermenuPunkte erstellen
    private Menu fileMenu;
    private Menu bearbeitenMenu;
    private Menu simulationsMenu;
    private Menu helpMenu;

    // MenuUnterpunkte im Menu "Datei"
    private Menu spracheMenuItem;
    private ToggleGroup toggleSprache;
    private RadioMenuItem englishSprache;
    private RadioMenuItem deutschSprache;
    private MenuItem quitMenuItem;

    private MenuItem groeßeAendernMenuItem;
    private MenuItem resizeableMenuItem;
    private MenuItem tankStelleBenoetigtMenuItem;

    // ToggleGroup f�r die folgenden RadioMenuItems erstellen, welche die
    // Auswahl im Menu sp�ter managed
    private ToggleGroup toggleBearbeitenSpielfeld;
    private RadioMenuItem roboterMenuItem;
    private RadioMenuItem felsen;
    private RadioMenuItem child;
    private RadioMenuItem batterie;
    private RadioMenuItem location;
    private RadioMenuItem deleteBefehl;

    // MenuUnterpunkte im Menupunkt "Simulation"
    private MenuItem startMenuItem;
    private MenuItem stopMenuItem;

    // MenuUnterpunkte im Menupunkt "Hilfe"
    private MenuItem hinweisMenuItem;
    private Button buttonHinweis;

    // Button deklarationen etc.
    // Button Start
    private Button buttonStart;


    // Button stoppen
    private Button buttonStop;

    // ComboBox f�r die sonst recht un�bersichtlichen und oft �berfl�ssigen
    // Buttons
    private ComboBox<ImageView> comboboxButtonBearbeitenAuswahl;

    // Slider f�r die Toolbar
    private Slider slider;

    // die SplitPane in welcher nachher der Code-Bereich und das Spiel liegen
    private SplitPane split;

    // Unser Akteur, das dicke fette UBOOT,
    // "HEY, ich bin nicht irgend ein dickes fettes UBOOT! Ich bin DAS dicke
    // fette UBOOT!!"
    private Territorium territorium;

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
    private RobotEvents robotEvents;

    // Label zum Anzeigen der Systemnachrichten
    private Label labelBottom;

    // ScrollPane fuer das Game
    public ScrollPane sc;

    // Achsen fuer die ScrollPane!
    public ScAchse scWidth;
    public ScAchse scHeigth;

    // TerritoriumPanel fuer das Game
    public TerritoriumPanel territoriumPanel;

    // ArrayList um alle RadioMenuItems zu deselecten wenn noetig
    public ArrayList<RadioMenuItem> al;

    // Fuer die Dragg and Drop Aktionen der Maus in sc
    public SubmarineMouseHandler scMousehandler;

    // Speicherte die Stage mit der das hier geoeffnet wird, um sie beim
    // schliessen aufrufen zu koennen
    private Stage primaryStage;

    // boolean welcher das Speichern vor dem schliessen abprueft
    private boolean textChanged;

    // als Feld zum aufzaehlen der geschriebenen Codezeilen
    private TextFlow flow;

    // Kommen der Code und der TextFlow fuer den Linecount rein
    private BorderPane borderPaneForCodeField;

    // Ist nur fuer das schoene aussehen da..
    private StackPane root;

    // Um den Code zum laufen zu bringen
    private RunCodeController runcode;

    // Controlliert alle TextArea-Ereignisse
    private TextAreaControls textAreaControls;


    private MovementInputController movement;
    private TextField robotInput;
    private TextField childInput;
    private TextField startTank;
    private CheckBox endlessLoop;

    /*
     * initialisiert alle Werte/Variablen/Atribute
     */
    public void initialisieren() {
        international = new Internationalitaet(null);
        robotEvents = new RobotEvents(getTerritorium());
        fileMenu = new Menu();
        bearbeitenMenu = new Menu();
        spracheMenuItem = new Menu();
        toggleSprache = new ToggleGroup();
        englishSprache = new RadioMenuItem();
        deutschSprache = new RadioMenuItem();
        quitMenuItem = new MenuItem();
        simulationsMenu = new Menu();
        helpMenu = new Menu();
        groeßeAendernMenuItem = new MenuItem();
        resizeableMenuItem = new MenuItem();
        tankStelleBenoetigtMenuItem = new MenuItem();
        toggleBearbeitenSpielfeld = new ToggleGroup();
        roboterMenuItem = new RadioMenuItem();
        felsen = new RadioMenuItem();
        child = new RadioMenuItem();
        batterie = new RadioMenuItem();
        location = new RadioMenuItem();
        deleteBefehl = new RadioMenuItem();
        startMenuItem = new MenuItem();
        stopMenuItem = new MenuItem();
        hinweisMenuItem = new MenuItem();
        buttonHinweis = new Button();
        toggleBearbeitenSpielfeld = new ToggleGroup();
        buttonStart = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
       buttonStop = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
        comboboxButtonBearbeitenAuswahl = new ComboBox<ImageView>();
        slider = new Slider();
        labelBottom = new Label();
        split = new SplitPane();
        clip = new AudioClip(
                getClass().getResource("../resourcesPicturesAndSoundsVidoes/BabyCryingSounds.wav").toString());
        border = new BorderPane();
        innerborder = new BorderPane();
        al = new ArrayList<RadioMenuItem>();
        al.add(roboterMenuItem);
        al.add(felsen);
        al.add(child);
        al.add(batterie);
        al.add(location);
        al.add(deleteBefehl);
        scWidth = new ScAchse();
        scHeigth = new ScAchse();
        setTextChanged(false);
        flow = new TextFlow();
        scMousehandler = new SubmarineMouseHandler(getTerritorium(), robotEvents, international.getRb(),
                getPrimaryStage());
        RadioMenuItem[] m = new RadioMenuItem[7];
        m[0] = roboterMenuItem;
        m[1] = child;
        m[2] = felsen;
        m[3] = batterie;
        m[4] = location;
        m[5] = deleteBefehl;
        territoriumPanel = new TerritoriumPanel(getTerritorium(), scWidth, scHeigth);
        borderPaneForCodeField = new BorderPane();
        root = new StackPane();

        robotInput = new TextField();
        robotInput.setText("lruu,e,rru");
        childInput = new TextField();
        childInput.setText("ldru,lrlr,ld,rd");
        movement = new MovementInputController(territorium, robotInput, childInput);

        startTank = new TextField("25");
        startTank.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                startTank.setText(newValue.replaceAll("[^\\d]", ""));
            }
            int val = Integer.valueOf(startTank.getText());
            getTerritorium().setStartTankfuellung(val);
        });

        endlessLoop = new CheckBox();
        endlessLoop.setText("Endless Loop (Büchi)");
        endlessLoop.selectedProperty().addListener((observable, oldValue, newValue) -> {
            getTerritorium().setEndlessLoop(newValue);
        });
        endlessLoop.setSelected(true);

        zuBeachtendeButtonsUndMenuItems();
    }

    /*
     * Speichert die sp�ter zu beachtenden MenuItems und Buttons beim Thread
     */
    private void zuBeachtendeButtonsUndMenuItems() {
        setRuncode(new RunCodeController(territorium, slider, buttonStart, buttonStop, startMenuItem,
                stopMenuItem, this, comboboxButtonBearbeitenAuswahl, child, roboterMenuItem, batterie, felsen, deleteBefehl, location));
    }

    /*
     * Zusammenbau aller Einzelteile und �ffnen/sichtbar machen des Fensters
     */
    public Oberflaeche(Stage stage, Territorium t) throws Exception {
        setPrimaryStage(stage);

        setTerritorium(t);
        initialisieren();

        sc = new ScrollPane(territoriumPanel.getScrollPane());

        //TODO: ADD input controls for robot and child here.
        VBox box = new VBox();
        box.getChildren().add(new Label("Robot Moves:"));
        box.getChildren().add(this.robotInput);

        box.getChildren().add(new Label("Child Moves:"));
        box.getChildren().add(this.childInput);
        box.getChildren().add(new Label("Start Tank:"));
        box.getChildren().add(this.startTank);
        box.getChildren().add(this.endlessLoop);
        borderPaneForCodeField.setCenter(box);
        //borderPaneForCodeField.setRight();
        // Die folgenden Zahlen sind nur nach pers�nlichem Empfinden gesetzt
        borderPaneForCodeField.setMinWidth(175);
        borderPaneForCodeField.setMaxWidth(300);

        split.getItems().addAll(borderPaneForCodeField, sc);
        split.setDividerPosition(0, 0.3);

        // Hinzufuegen der Komponenten des inneren Borders
        innerborder.setCenter(split);
        innerborder.setTop(addToolBar());

        // Hinzufuegen des Menus, des Labels und des inneren Borders zum
        // main-Border
        border.setTop(addMenuBar());
        border.setBottom(addLabel());
        border.setCenter(innerborder);

        root.getChildren().add(border);

        // Hier noch mal die explizieten Sch�nheits�nderungen, welche nicht alle
        // gleich in der "CSS"-Datei sind
        territoriumPanel.getScrollPane().setStyle("-fx-background: #6680e6; -fx-background-color: #6680e6;");
        root.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #4d66cc, #001a80)");

        // Hat keinen gr�en Effekt au�er, dass sie nicht transparent wird.
        // Sollte man sich jedoch entscheiden, den "Disable" raus zu nehmen, ist
        // die Farbe wieder "sch�n" gew�hlt.

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
        buttonStop.setPrefSize(Territorium.OBJ_WIDTH, Territorium.OBJ_WIDTH);

        // Erstellt eine ComboBox f�r nicht so h�ufig ben�tigte Buttons
        bearbeiteComboBoxFuerDieButtons();

        // f�gt den Buttons Tooltips hinzu, welche erkl�ren was sie machen
        setTooltipAndTexts();

        // toolbar.getChildren().addAll(buttonSubmarine, buttonFelsen,
        // buttonBatterie, buttonExitFeld, buttonDelete);
        toolBar.getItems().addAll(comboboxButtonBearbeitenAuswahl, new Separator(), buttonStart, buttonStop,
                new Separator(), slider);

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
                        getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/roboter.png"))),
                new ImageView(new Image(
                        getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/kind.png"))),
                new ImageView(new Image(
                        getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/felsenSmall.png"))),
                new ImageView(new Image(
                        getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/batterieSmall.png"))),
                new ImageView(
                        new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/location.png"))),
                new ImageView(new Image(
                        getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/deleteSmall.png"))));

        comboboxButtonBearbeitenAuswahl.setCellFactory(new ImageCombo());

        comboboxButtonBearbeitenAuswahl.setMinWidth(Territorium.OBJ_WIDTH * 2.5);
        comboboxButtonBearbeitenAuswahl.setPrefSize(Territorium.OBJ_WIDTH * 2.5, Territorium.OBJ_WIDTH * 1.5);
    }

    public Label addLabel(){
        labelBottom.setText("Roboter-Programm");
        return labelBottom;
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
        roboterMenuItem.setToggleGroup(toggleBearbeitenSpielfeld);
        felsen.setToggleGroup(toggleBearbeitenSpielfeld);
        child.setToggleGroup(toggleBearbeitenSpielfeld);
        batterie.setToggleGroup(toggleBearbeitenSpielfeld);
        location.setToggleGroup(toggleBearbeitenSpielfeld);
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

        // Hinzuf�gen der MenuUnterpunkte zum MenuPunkt
        fileMenu.getItems().addAll(spracheMenuItem, new SeparatorMenuItem(), quitMenuItem);
        bearbeitenMenu.getItems().addAll(groeßeAendernMenuItem, new SeparatorMenuItem(),
                resizeableMenuItem, tankStelleBenoetigtMenuItem, new SeparatorMenuItem(), roboterMenuItem,
                child, felsen, batterie, location, deleteBefehl);
        simulationsMenu.getItems().addAll(startMenuItem, stopMenuItem);
        helpMenu.getItems().addAll(hinweisMenuItem);

        // Hinzuf�gen der MenuPunkte zur MenuBar
        menu.getMenus().addAll(fileMenu, bearbeitenMenu, simulationsMenu, helpMenu);

        return menu;
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

        // Erm�glicht durch das binding, dass das Spiel immer in die Mitte
        // gesetzt wird.
        scWidth.valueProperty().bind(sc.widthProperty());
        scHeigth.valueProperty().bind(sc.heightProperty());

        scWidth.valueProperty().addListener(c -> getTerritorium().checkIfChanged());
        scHeigth.valueProperty().addListener(c -> getTerritorium().checkIfChanged());

        // MouseHandler f�r das Draggen und Dropen hinzuf�gen
        scMousehandler.handlerSetzen(territoriumPanel.getCanvas());

        // Untermenu Sprache:
        // Unterpunkt Deutsch:
        deutschSprache.setOnAction(e -> {
            if (robotEvents.spracheAendern("de", international)) {
                setTooltipAndTexts();
            }
        });

        // Unterpunkt Englisch
        englishSprache.setOnAction(e -> {
            if (robotEvents.spracheAendern("en", international)) {
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
            getPrimaryStage().close();
        });
        getPrimaryStage().setOnCloseRequest(event -> {
            stopMenuItem.fire();
            getPrimaryStage().close();
        });

        // Unterpunkt "Gr��e �ndern"
        groeßeAendernMenuItem.setOnAction(e -> robotEvents.changeSizeEventHandler(international.getRb()));

        // Unterpunkt "Gr��e Anpassen an/aus"
        resizeableMenuItem.setOnAction(e -> {
            getTerritorium().setResizeable(!getTerritorium().isResizeable());
            rezisableImageCheck();
        });

        tankStelleBenoetigtMenuItem.setOnAction(e -> {
            getTerritorium().setTrankfuellungBeachten(!getTerritorium().isTrankfuellungBeachten());
            tankstellenImageCheck();
            getTerritorium().checkIfChanged();
        });

		/*
         * ComboBox �ber PropertyListener mit den RadioMenuItems verbunden.
		 */
        comboboxButtonBearbeitenAuswahl.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    switch (comboboxButtonBearbeitenAuswahl.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            roboterMenuItem.setSelected(true);
                            roboterMenuItem.fire();
                            break;
                        case 1:
                            child.setSelected(true);
                            child.fire();
                            break;
                        case 2:
                            felsen.setSelected(true);
                            felsen.fire();
                            break;
                        case 3:
                            batterie.setSelected(true);
                            batterie.fire();
                            break;
                        case 4:
                            location.setSelected(true);
                            location.fire();
                            break;
                        case 5:
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
                                        roboterMenuItem.setSelected(false);
                                        break;
                                    case 1:
                                        child.setSelected(false);
                                        break;
                                    case 2:
                                        felsen.setSelected(false);
                                        break;
                                    case 3:
                                        batterie.setSelected(false);
                                        break;
                                    case 4:
                                        location.setSelected(false);
                                        break;
                                    case 5:
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
        robotEvents.setzeObjektEventCode(null, territoriumPanel, roboterMenuItem, 0,
                comboboxButtonBearbeitenAuswahl, false);

        // Unterpunkt "Leucht-Felsen"
        robotEvents.setzeObjektEventCode(null, territoriumPanel, child, 1,
                comboboxButtonBearbeitenAuswahl, true);

        // Unterpunkt "Felsen"
        robotEvents.setzeObjektEventCode(FeldEigenschaft.Felsen, territoriumPanel, felsen, 2,
                comboboxButtonBearbeitenAuswahl, false);

        // Unterpunkt "Batterie"
        robotEvents.setzeObjektEventCode(FeldEigenschaft.Batterie, territoriumPanel, batterie, 3,
                comboboxButtonBearbeitenAuswahl, false);

        // Unterpunkt "Hai-Fisch"
        robotEvents.setzeObjektEventCode(FeldEigenschaft.Location, territoriumPanel, location, 4,
                comboboxButtonBearbeitenAuswahl, false);

        // Unterpunkt "Delete"
        robotEvents.setzeObjektEventCode(FeldEigenschaft.Leer, territoriumPanel, deleteBefehl, 5,
                comboboxButtonBearbeitenAuswahl, false);

        // Aktionen im Menu "Simulation"
        // Unterpunkt "start"
        startMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11, KeyCombination.CONTROL_DOWN));

        // Unterpunkt "stop"
        stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12, KeyCombination.CONTROL_DOWN));


        // Aktionen im Menu "Hilfe"
        // Unterpunkt "Hinweis"
        hinweisMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        hinweisMenuItem.setOnAction(e -> robotEvents.oeffneHilfeFenster(international.getRb()));
        buttonHinweis.setOnAction(e -> hinweisMenuItem.fire());
    }

    /*
     * F�gt dem MenuItems die zutreffende Images hinzu
     */
    private void setImagesOnMenuItems() {
        // Vorher noch eben das Logo xD
        getPrimaryStage().getIcons().add(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/roboter.png")));

        // Images in die MenuLeiste einf�gen

        // Datei-Images
        spracheMenuItem.setGraphic(new ImageView(new Image(
                getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/languageVerySmall.png"))));
        deutschSprache.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/germanVerySmall.png"))));
        englishSprache.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/englishVerySmall.png"))));
        quitMenuItem.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/cancel.png"))));

        groeßeAendernMenuItem.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/sizeVerySmall.png"))));
        rezisableImageCheck();
        tankstellenImageCheck();
        roboterMenuItem.setGraphic(new ImageView(new Image(
                getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/roboterSmall.png"))));
        felsen.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/felsenVerySmall.png"))));
        child.setGraphic(new ImageView(new Image(
                getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/kindSmall.png"))));
        batterie.setGraphic(new ImageView(new Image(
                getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/batterieVerySmall.png"))));
        location.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/locationSmall.png"))));
        deleteBefehl.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/deleteVerySmall.png"))));

        // Simulation-Images
        startMenuItem.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startVerySmall.png"))));
        stopMenuItem.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopVerySmall.png"))));

        // Hilfe-Images
        hinweisMenuItem.setGraphic(new ImageView(new Image(
                getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/fragezeichenVerySmall.png"))));
    }

    /*
     * Gibt den Button Tooltips, welche erkl�ren was ein Dr�cken des Button
     * bewirkt.
     */
    private void setTooltipAndTexts() {
        fileMenu.setText(international.getRb().getString("fileMenu"));
        bearbeitenMenu.setText(international.getRb().getString("bearbeitenMenu"));
        spracheMenuItem.setText(international.getRb().getString("spracheMenuItem"));
        englishSprache.setText(international.getRb().getString("englishSprache"));
        deutschSprache.setText(international.getRb().getString("deutschSprache"));
        quitMenuItem.setText(international.getRb().getString("quitMenuItem"));
        simulationsMenu.setText(international.getRb().getString("simulationsMenu"));
        helpMenu.setText(international.getRb().getString("helpMenu"));
        groeßeAendernMenuItem.setText(international.getRb().getString("groeßeAendernMenuItem"));
        resizeableMenuItem.setText(international.getRb().getString("resizeableMenuItem"));
        tankStelleBenoetigtMenuItem.setText(international.getRb().getString("tankfuellung"));
        roboterMenuItem.setText(international.getRb().getString("roboter"));
        felsen.setText(international.getRb().getString("felsen"));
        child.setText(international.getRb().getString("child"));
        batterie.setText(international.getRb().getString("batterie"));
        location.setText(international.getRb().getString("location"));
        deleteBefehl.setText(international.getRb().getString("deleteBefehl"));
        startMenuItem.setText(international.getRb().getString("startMenuItem"));
        stopMenuItem.setText(international.getRb().getString("stopMenuItem"));
        hinweisMenuItem.setText(international.getRb().getString("hinweisMenuItem"));

        buttonStart.setTooltip(new Tooltip(international.getRb().getString("buttonStart")));
        buttonStop.setTooltip(new Tooltip(international.getRb().getString("buttonStop")));


        buttonHinweis.setTooltip(new Tooltip(international.getRb().getString("buttonHinweis")));
    }

    public RobotEvents getSubmarineEvents() {
        return robotEvents;
    }

    public void setSubmarineEvents(RobotEvents s) {
        robotEvents = s;
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

    public void tankstellenImageCheck(){
        if (getTerritorium().isTrankfuellungBeachten()) {
            tankStelleBenoetigtMenuItem.setGraphic(new ImageView(new Image(
                    getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/tankstelleSmall.png"))));
            tankStelleBenoetigtMenuItem.setText(international.getRb().getString("tankfuellung"));
        } else {
            tankStelleBenoetigtMenuItem.setGraphic(new ImageView(new Image(
                    getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopVerySmall.png"))));
            tankStelleBenoetigtMenuItem.setText(international.getRb().getString("kTankfuellung"));
        }
    }

    public RunCodeController getRuncode() {
        return runcode;
    }

    public void setRuncode(RunCodeController runcode) {
        this.runcode = runcode;
    }
}
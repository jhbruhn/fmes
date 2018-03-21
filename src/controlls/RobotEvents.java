package controlls;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import modell.Internationalitaet;
import modell.NumberTextField;
import modell.ScAchse;
import modell.Territorium;
import modell.Territorium.FeldEigenschaft;
import modell.Roboter;
import views.Oberflaeche;
import views.TerritoriumPanel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class RobotEvents {
    private Territorium territorium;
    private Start start;
    private final static String fehlerText = "999Fehler%&%Abbruch";

    /*
     * normaler Konstruktor
     */
    public RobotEvents(Territorium t) {
        setTerritorium(t);
    }

    /*
     * f�hrt die Aktionen aus, welche das UBoot auf dem Spielfeld machen kann,
     * dabei gibt der Char c an, welches Event gew�hlt wird
     */
    public void submarineAktionen(char c) {
        Territorium t = getTerritorium();
        switch (c) {
            case 'v':
                t.bewege(Territorium.Richtung.UP, true);
                break;
            case 'b':
                t.bewege(Territorium.Richtung.DOWN, true);
                break;
            case 'l':
                t.bewege(Territorium.Richtung.LEFT, true);
                break;
            case 'r':
                t.bewege(Territorium.Richtung.RIGHT, true);
                break;
            default:
                break;
        }
        setTerritorium(t);
    }

    /*
     * stellt sicher, dass immer wenn ein FeldItem ausgew�hlt ist, auch in der
     * ComboBox das selbe Item zu sehen ist / ausgew�hlt ist
     */
    public void setzeObjektEventCode(FeldEigenschaft f, TerritoriumPanel tp, RadioMenuItem rm, int select,
                                     ComboBox<ImageView> c, boolean child) {
        rm.setOnAction(e -> {
            scSetMouseClickedEvent(f, tp, rm, child);
            c.getSelectionModel().select(select);
        });
    }

    /*
     * Vor Methode um abzupr�fen wo das Event des hinclickens passiert ist. Mit
     * den neuen X und Y koordinaten wird dann das Objekt platziert
     */
    public void scSetMouseClickedEvent(FeldEigenschaft f, TerritoriumPanel tp, RadioMenuItem m, boolean child) {
        if (m.isSelected()) {
            tp.getCanvas().setOnMouseClicked(
                    event -> setObjectOnTheFieldEvent(f, (int) (event.getX()), (int) (event.getY()), m, child));
        }
    }

    /*
     * Regelt die Aktionen des Submarines, wobei der Char c bestimtm welche
     * Aktion ausgef�hrt wird. Diese Methode wird auf jeden Button/MenuItem
     * angewandt, welcher mit den direkten Aktionen des Submarines zu tun hat
     */
    public void menuItemsButtonsFuerSubmarineAktions(MenuItem m, Button b, char c) {
        m.setOnAction(e -> submarineAktionen(c));
        b.setOnAction(e -> m.fire());
    }

    /*
     * �ndert die Sprache auf die ausgew�hlte Sprache
     */
    public boolean spracheAendern(String s, Internationalitaet international) {
        if (!international.getLanguage().equals(s)) {
            international = new Internationalitaet(s);
            return true;
        }
        return false;
    }

    /*
     * setzt die Radoimen�items zur�ck, da diese von der ComboBox betroffen sein
     * k�nnen und somit sonst fehlerhafte "select" werte h�tten
     */
    public void deselectRadioMenuItems(ArrayList<RadioMenuItem> al, RadioMenuItem a) {
        for (RadioMenuItem r : al) {
            if (!a.equals(r)) {
                r.setSelected(false);
            }
        }
    }

    /*
     * Setzt das Ausgew�hlte Objekt auf das Feld, welches ausgew�hlt wurde.
     * Dabei sind die X und Y Koordinate die �bergebenen Werte, welche vom
     * Eventhandler generiert wurden
     */
    public void setObjectOnTheFieldEvent(FeldEigenschaft f, int x, int y, RadioMenuItem m, boolean child) {
        if (m.isSelected()) {
            Territorium t = getTerritorium();
            if (t.getFeldBreite() > (x - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungBreiteDesSpielFeldes())
                    / t.getObjectResizedWidth()
                    && 0 <= (x - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungBreiteDesSpielFeldes())
                    / t.getObjectResizedWidth()
                    && t.getFeldHoehe() > (y - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungHoeheDesSpielFeldes())
                    / t.getObjectResizedHeigth()
                    && 0 <= (y - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungHoeheDesSpielFeldes())
                    / t.getObjectResizedHeigth()
                    && x > Oberflaeche.CANVAS_RAND_ABSTAND + t.getEntfernungBreiteDesSpielFeldes()
                    && y > Oberflaeche.CANVAS_RAND_ABSTAND + t.getEntfernungHoeheDesSpielFeldes()) {
                if (f == null && !child) {
                    t.setzeUBootAufsFeld(
                            (int) ((y - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungHoeheDesSpielFeldes())
                                    / t.getObjectResizedHeigth()),
                            (int) ((x - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungBreiteDesSpielFeldes())
                                    / t.getObjectResizedWidth()));
                } else {
                    t.setzeObjektAufsFeld(
                            (int) ((y - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungHoeheDesSpielFeldes())
                                    / t.getObjectResizedHeigth()),
                            (int) ((x - Oberflaeche.CANVAS_RAND_ABSTAND - t.getEntfernungBreiteDesSpielFeldes())
                                    / t.getObjectResizedWidth()),
                            f, child);
                }
                setTerritorium(t);
            }
        }
    }

    /*
     * stellt sicher, dass der Inhalt des TextAreas auch abspeicherbar ist.
     * !AUFBAU NICHT �NDERN! dabei ist der sufix und pr�fix genau so
     * einzuhalten, damit das auslesen uns speichern an allen stellen klappt
     */
    public String erstelleSpeicherbarenCode(String stageTitle, TextArea text) {
        String s = text.getText();
        String code = "import modell.UBoot;" + "\n" + "\n" + "public class " + stageTitle
                + " extends UBoot {" + "public " + "\n" + s + "\n" + "}";
        return code;
    }

    boolean buttonHoehe;
    boolean buttonBreite;
    int neueBreite;
    int neueHoehe;

    /*
     * �ndert die Gr��e des Feldes
     */
    public void changeSizeEventHandler(ResourceBundle rb) {
        buttonHoehe = false;
        buttonBreite = false;
        neueBreite = 0;
        neueHoehe = 0;

        Stage s = new Stage();
        GridPane grid = new GridPane();
        Button buttonOK = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("/resourcesPicturesAndSoundsVidoes/startSmall.png"))));
        buttonOK.setTooltip(new Tooltip(rb.getString("buttonOK")));
        Button buttonAbbrechen = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("/resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
        buttonAbbrechen.setTooltip(new Tooltip(rb.getString("buttonAbbrechen")));
        buttonOK.setDisable(true);
        NumberTextField heigthZahlentextfeld = new NumberTextField(grid, rb);
        NumberTextField widthZahlentextfeld = new NumberTextField(grid, rb);
        Label labelHoeheInChangeSize = new Label(rb.getString("labelHoeheInChangeSize") + " " + Territorium.MIN_HEIGTH
                + " " + rb.getString("InChangeSizeUndMitte") + " " + Territorium.MAX_HEIGTH
                + rb.getString("InChangeSizeKlammerEnde"));
        Label labelBreiteInChangeSize = new Label(rb.getString("labelBreiteInChangeSize") + " " + Territorium.MIN_WIDTH
                + " " + rb.getString("InChangeSizeUndMitte") + " " + Territorium.MAX_WIDTH
                + rb.getString("InChangeSizeKlammerEnde"));
        heigthZahlentextfeld.textProperty().addListener((observable, oldValue, newValue) -> {
            buttonHoehe = false;
            try {
                neueHoehe = Integer.parseInt(newValue);
            } catch (Exception e) {
                if (newValue == null || newValue.equals("")) {
                    neueHoehe = 0;
                }
            }
            if (neueHoehe >= Territorium.MIN_HEIGTH && neueHoehe <= Territorium.MAX_HEIGTH) {
                buttonHoehe = true;
                if (buttonBreite) {
                    buttonOK.setDisable(false);
                } else {
                    buttonOK.setDisable(true);
                }
            } else {
                buttonHoehe = false;
                buttonOK.setDisable(true);
            }

        });
        widthZahlentextfeld.textProperty().addListener((observable1, oldValue2, newValue2) -> {
            buttonBreite = false;
            try {
                neueBreite = Integer.parseInt(newValue2);
            } catch (Exception e) {
                if (newValue2 == null || newValue2.equals("")) {
                    neueBreite = 0;
                }
            }
            if (neueBreite >= Territorium.MIN_WIDTH && neueBreite <= Territorium.MAX_WIDTH) {
                buttonBreite = true;
                if (buttonHoehe) {
                    buttonOK.setDisable(false);
                } else {
                    buttonOK.setDisable(true);
                }
            } else {
                buttonBreite = false;
                buttonOK.setDisable(true);
            }
        });

        buttonOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getTerritorium().changeSize(neueHoehe, neueBreite);
                neueHoehe = 0;
                neueBreite = 0;
                buttonHoehe = false;
                buttonBreite = false;
                s.close();
            }
        });

        buttonAbbrechen.setOnAction(e -> s.close());
        grid.add(labelHoeheInChangeSize, 1, 1);
        grid.add(labelBreiteInChangeSize, 1, 2);
        grid.add(heigthZahlentextfeld, 2, 1);
        grid.add(widthZahlentextfeld, 2, 2);
        grid.add(buttonOK, 1, 4);
        grid.add(buttonAbbrechen, 2, 4);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setAlignment(Pos.CENTER);
        s.setScene(new Scene(grid, 450, 150));
        s.show();
    }

    /*
     * gibt ein Alert-Fenster aus, welches die Methoden des Spiels wiedergibt
     * und kurz erkl�rt
     */
    public void oeffneHilfeFenster(ResourceBundle rb) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(rb.getString("oeffneHilfeFensterTitle"));
        alert.setHeaderText(rb.getString("oeffneHilfeFensterHeader"));
        alert.setContentText(rb.getString("oeffneHilfeFensterContent"));
        alert.showAndWait();
    }

    // TODO das hier abdecken, damit ich auch mit der Entertaste den ButtonOK

    public Territorium getTerritorium() {
        return territorium;
    }

    public void setTerritorium(Territorium territorium) {
        this.territorium = territorium;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    private void tagsEingeben(PrimaryStageName p, String create, ResourceBundle rb) {
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        TextField textName = new TextField();
        Button buttonName = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("/resourcesPicturesAndSoundsVidoes/startSmall.png"))));
        Button buttonAbbrechenName = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("/resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
        buttonAbbrechenName.setOnAction(e -> {
            p.setName(fehlerText);
            stage.close();
        });
        buttonName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.setName(textName.getText());
                stage.close();
            }
        });

        buttonName.setDisable(true);
        textName.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.matches("([A-Z]*[a-z]*[0-9]*[ ]*)*")) {
                    buttonName.setDisable(false);
                } else {
                    buttonName.setDisable(true);
                }
            } catch (Exception e) {
                buttonName.setDisable(true);
            }
        });
        Label tags = new Label(rb.getString("tags"));
        grid.add(tags, 1, 0);
        grid.add(textName, 1, 1);
        grid.add(buttonName, 1, 2);
        grid.add(buttonAbbrechenName, 2, 2);
        grid.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(grid, 350, 150));
        stage.setOnCloseRequest(e -> {
            p.setName(fehlerText);
            stage.close();
        });
        stage.setTitle(create);
        stage.show();
    }

    String zielnummer = "";

    /*
     * Anders als bei Anderen Hamstermodellen gibt es beim UBoot noch die
     * Speziellen Finishevents, welche beachtet werden sollen udn hier zum
     * tragen kommen k�nnen
     */
    private void tagsEingebenSpecialNumber(PrimaryStageName p, String create, ResourceBundle rb) {
        zielnummer = "0";
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        TextField textName = new TextField();
        NumberTextField textSpecial = new NumberTextField(grid, rb);
        Button buttonName = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("/resourcesPicturesAndSoundsVidoes/startSmall.png"))));
        Button buttonAbbrechenName = new Button("", new ImageView(
                new Image(getClass().getResourceAsStream("/resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
        buttonAbbrechenName.setOnAction(e -> {
            p.setName(fehlerText);
            stage.close();
        });
        buttonName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (textSpecial.getText() != null && !textSpecial.getText().trim().equals("")
                            && Integer.parseInt(textSpecial.getText()) > 0) {
                        p.setName(textName.getText() + "||<>" + textSpecial.getText() + "<>||" + zielnummer);
                    } else {
                        p.setName(textName.getText() + "||<>" + "-1" + "<>||" + zielnummer);
                    }
                } catch (Exception e) {
                    p.setName(textName.getText() + "||<>" + "-1" + "<>||" + "0");
                }
                stage.close();
            }
        });

        buttonName.setDisable(true);
        textName.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.matches("([A-Z]*[a-z]*[0-9]*[ ]*)*") && newValue.length() > 2) {
                    buttonName.setDisable(false);
                } else {
                    buttonName.setDisable(true);
                }
            } catch (Exception e) {
                buttonName.setDisable(true);
            }
        });
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("0");
        options.add("1");
        options.add("2");
        options.add("9000");
        options.add("9001");
        options.add("9999");
        ComboBox<String> comboLaden = new ComboBox<String>(options);
        comboLaden.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                zielnummer = newValue;
            }
        });

        Label tags = new Label(rb.getString("tags"));
        Label special = new Label(rb.getString("special"));
        Label finish = new Label(rb.getString("finish"));
        grid.add(tags, 1, 1);
        grid.add(textName, 1, 2);
        grid.add(special, 0, 3);
        grid.add(textSpecial, 1, 3);
        grid.add(finish, 0, 4);
        grid.add(comboLaden, 1, 4);
        grid.add(buttonName, 1, 5);
        grid.add(buttonAbbrechenName, 2, 5);
        grid.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(grid, 500, 150));
        stage.setOnCloseRequest(e -> {
            p.setName(fehlerText);
            stage.close();
        });
        stage.setTitle(create);
        stage.show();
    }
}

/*
 * ist nur dazu da, die Namen richtig zu w�hlen
 */
class PrimaryStageName {
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name", "");

    public String getValue() {
        return name.get();
    }

    public void setName(String val) {
        name.set(val);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
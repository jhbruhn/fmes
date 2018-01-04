package controlls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import modell.Roboter;
import modell.Territorium;
import resources.Invisible;
import views.Oberflaeche;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class TextAreaControls {

    private TextArea textarea;
    private Territorium territorium;
    private Oberflaeche oberflaeche;
    private TextFlow flow;
    // Variable die wirklich nur f�r den Check benutzt wird, ob die Klammer
    // schon gesetzt wurde
    private boolean gescheifteKlammerGesetzt = false;
    int zaehler = 0;

    public TextAreaControls(Territorium t, TextArea text, Oberflaeche o, TextFlow f) {
        territorium = t;
        textarea = text;
        oberflaeche = o;
        flow = f;
    }

    /*
     * Wenn man eine Klammer auf setzt, so wird auch eine Klammer zu automatisch
     * gesetzt. Ebenfalls f�r " , bei { ist eine �nderung drin, damit es wie in
     * Java nur beim EnterTeste dr�cken passiert und diese dann eine Zeile nach
     * unten bewegt wird
     */
    public void verbessertesKlammernsetzen() {
        getTextArea().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCharacter().equals("(")) {
                    getTextArea().insertText(getTextArea().getAnchor(), ")");
                    getTextArea().backward();
                }
                if (event.getCharacter().equals("\"")) {
                    getTextArea().insertText(getTextArea().getAnchor(), "\"");
                    getTextArea().backward();
                }
                if (event.getCharacter().equals("{")) {
                    gescheifteKlammerGesetzt = true;
                    getTextArea().setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event2) {
                            if (event2.getCode().equals(KeyCode.ENTER) && gescheifteKlammerGesetzt) {
                                gescheifteKlammerGesetzt = false;
                                getTextArea().insertText(getTextArea().getAnchor(), "\n" + "}");
                                getTextArea().backward();
                                getTextArea().backward();
                            }
                        }
                    });
                }
            }
        });
    }

    /*
     * Magie auf hohem Niveau (ja ich habe extra gegoogelt, wie man das
     * schreibt! so wichtig ist es mir). Es wird geschaut per normaler Eclipse
     * autofill von MethodenNamen usw. ob es den Text, welcher gerade da steht
     * so als Methodennamen schon gibt, wenn ja wird der Text
     * autovervollst�ndigt. Sollte es noch eine kleine Auswahl an m�glichen
     * Methoden geben, so wird ein PopUpMenu ausgegeben, in welchem man sich
     * dann entscheiden kann. Hohe Magie ikr
     */
    public void autoFillMethodenNamen() {
        getTextArea().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.SPACE) && event.isControlDown()) {
                    zaehler = 0;
                    while (getTextArea()
                            .getText(getTextArea().getAnchor() - zaehler - 1, getTextArea().getAnchor() - zaehler)
                            .matches("[A-Za-z0-9]")) {
                        zaehler = zaehler + 1;
                    }
                    final ContextMenu contextMenu = new ContextMenu();
                    TextField textField = new TextField();
                    int count = 0;
                    for (java.lang.reflect.Method m : territorium.getUboot().getClass().getMethods()) {
                        boolean hinzufuegen = true;
                        if (!m.getName().startsWith(getTextArea().getText(getTextArea().getAnchor() - zaehler,
                                getTextArea().getAnchor()))) {
                            hinzufuegen = false;
                        }
                        Invisible inv = m.getAnnotation(Invisible.class);
                        if (inv != null) {
                            hinzufuegen = false;
                        }
                        if (Modifier.isStatic(m.getModifiers()) || Modifier.isAbstract(m.getModifiers())) {
                            hinzufuegen = false;
                        }
                        for (java.lang.reflect.Method m2 : (new Roboter(territorium)).getClass().getSuperclass()
                                .getMethods()) {
                            if (m.getName().equals(m2.getName())) {
                                hinzufuegen = false;
                            }
                        }
                        if (hinzufuegen) {
                            String parameterString = " ";
                            for (int i = 0; i < m.getParameters().length; i++) {
                                Parameter p = m.getParameters()[i];
                                parameterString = parameterString + p.getParameterizedType().toString();
                                if (i < m.getParameters().length - 1) {
                                    parameterString = parameterString + ", ";
                                } else {
                                    parameterString = parameterString + " ";
                                }
                            }
                            MenuItem item = new MenuItem(
                                    m.getReturnType().toString() + " " + m.getName() + "(" + parameterString + ")");
                            item.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    String ps = "";
                                    for (int i = 0; i < m.getParameters().length; i++) {
                                        Parameter p = m.getParameters()[i];
                                        ps = ps + " " + p.getName() + " ";
                                        if (i < m.getParameters().length - 1) {
                                            ps = ps + ",";
                                        }
                                    }
                                    getTextArea().replaceText(getTextArea().getAnchor() - zaehler,
                                            getTextArea().getAnchor(), m.getName() + "(" + ps + ");");
                                    try {
                                        if (getTextArea().getText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1)
                                                .equals(")")) {
                                            getTextArea().replaceText(getTextArea().getAnchor() - 1, getTextArea().getAnchor(), "");
                                        }
                                    } catch (IndexOutOfBoundsException e) {
                                        //Dann halt nicht
                                    }
                                }
                            });
                            count = count + 1;
                            contextMenu.getItems().add(item);
                        }
                    }
                    textField.setContextMenu(contextMenu);
                    if (count < 5) {
                        if (count == 1) {
                            contextMenu.getItems().get(0).fire();
                        } else {
                            TextInputControl control = (TextInputControl) event.getSource();
                            Point2D pos = control.getInputMethodRequests().getTextLocation(0);
                            contextMenu.show(getTextArea(), pos.getX(), pos.getY());
                            contextMenu.requestFocus();
                            contextMenu.setStyle("-fx-background-color: e6994d; -fx-background: e6994d;");
                        }
                    }
                }
            }

        });
    }

    /*
     * Sollte beim entfernen einer LEFT_BRACE gleich rechts daneben die
     * RIGTH_BRACE sein, so wird sie mit entfernt, selbiges f�r " und {
     */
    public void verbessertesKlammernEntfernen() {
        getTextArea().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                    /*
					 * Folgender Code w�rde ein verbessertes L�schen der
					 * Klammern erwirken, ist jedoch zu Fehleranf�llig was
					 * Exceptions angeht und wird deshalb gemieden
					 */

                    // try {
                    // if (getTextArea().getText(getTextArea().getAnchor() - 1,
                    // getTextArea().getAnchor())
                    // .equals("{")) {
                    // int i = 0;
                    // while (true) {
                    // if (getTextArea()
                    // .getText(getTextArea().getAnchor() + i,
                    // getTextArea().getAnchor() + i + 1)
                    // .equals("}")) {
                    // getTextArea().replaceText(getTextArea().getAnchor(),
                    // getTextArea().getAnchor() + i + 1, "");
                    // break;
                    // }
                    // if (getTextArea()
                    // .getText(getTextArea().getAnchor() + i,
                    // getTextArea().getAnchor() + i + 1)
                    // .equals(" ")
                    // || getTextArea().getText(getTextArea().getAnchor() + i,
                    // getTextArea().getAnchor() + i + 1).equals("\n")) {
                    // i = i + 1;
                    // } else {
                    // break;
                    // }
                    // if (i > 10) {
                    // break;
                    // }
                    // }
                    // }
                    // } catch (IndexOutOfBoundsException e) {
                    // // do nothing cuz the Anchor sucks
                    // }
                    try {
                        if (getTextArea().getText(getTextArea().getAnchor() - 1, getTextArea().getAnchor()).equals("{")
                                && getTextArea().getText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1)
                                .equals("}")) {
                            getTextArea().replaceText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1, "");
                        }
                        if (getTextArea().getText(getTextArea().getAnchor() - 1, getTextArea().getAnchor()).equals("\"")
                                && getTextArea().getText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1)
                                .equals("\"")) {
                            getTextArea().replaceText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1, "");
                        }
                        if (getTextArea().getText(getTextArea().getAnchor() - 1, getTextArea().getAnchor()).equals("(")
                                && getTextArea().getText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1)
                                .equals(")")) {
                            getTextArea().replaceText(getTextArea().getAnchor(), getTextArea().getAnchor() + 1, "");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        //Dann halt nich
                    }
                }
            }
        });
    }

    /*
     * Das TextFlow Feld wird mit Zeilennummern versehen, welche gleich viele
     * sind, wie im TextArea selbst gerade vorhanden sind. dann wird gepr�ft ob
     * gerade Fehler im Code dest TextAreas sind, wenn ja werden die
     * Zahlennummern des TextFlows in der jewalligen Zeile rot eingef�rbt, sonst
     * bleiben sie gr�n
     */
    public void textFlowMitFehlerMeldungFuellen() {
        // Textarea mit dem Zeilenfeld versehen (um die Zeilen zu z�hlen)
        getTextArea().textProperty().addListener(e -> {
            oberflaeche.setTextChanged(true);
            while (!flow.getChildren().isEmpty()) {
                flow.getChildren().remove(0);
            }
            if (getTextArea() != null || !getTextArea().getText().equals("")) {
                String s[] = getTextArea().getText().split("\n");

                //TODO das hier noch zum hochzaehlen bringen
                int compileArray = 20;


                for (int i = 0; i < s.length; i++) {
                    Text t = new Text();
                    t.setFont(Font.font("Verdana", 14));
                    boolean fehler = false;
                    for (int j = 0; j < compileArray; j++) {
                      //  if (i == compileArray.get(j)) {
                      //      fehler = true;
                      //  }
                    }
                    // hier wird mit den Angaben des shortCompilers ausgewertet
                    // ob in der Zeile ein Fehler vorliegt. Wenn Ja wird diese
                    // makiert
                    if (!fehler) {
                        t.setStyle("-fx-fill: #4F8A10;-fx-font-weight:bold;");
                    } else {
                        t.setStyle("-fx-fill: RED;-fx-font-weight:normal;");
                    }
                    t.setText(i + "\n");
                    flow.getChildren().add(t);
                }
            }
        });
    }

    /*
     * macht das unm�gliche m�glich. F�r genauere Details bitte unterMethoden
     * anschauen
     */
    public void setzeEventsZurTextArea() {
        verbessertesKlammernsetzen();

        autoFillMethodenNamen();

        verbessertesKlammernEntfernen();

        textFlowMitFehlerMeldungFuellen();
    }

    public TextArea getTextArea() {
        return textarea;
    }

    public void setTextarea(TextArea textarea) {
        this.textarea = textarea;
    }

}

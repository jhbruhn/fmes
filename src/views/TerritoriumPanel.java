package views;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JComponent;

import controlls.AnimationController;
import javafx.scene.text.Font;
import modell.ScAchse;
import modell.Territorium;
import modell.Territorium.FeldEigenschaft;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TerritoriumPanel extends Region implements Observer {

    private final Image felsenSmall = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/felsenSmall.png"));

    private final Image ort1 = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/ort1.jpg"));

    private final Image batterieSmall = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/batterieSmall.png"));

    private final Image ort2 = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/ort2.jpg"));

    private final Image ort3 = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/ort3.jpg"));

    private final Image deleteSmall = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/deleteSmall.png"));

    private final Image submarineBugNordSmall = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/Roboter.png"));

    private final Image submarineHeckNordSmall = new Image(
            getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/Kind.png"));

    Canvas canvas = new Canvas();
    Group root = new Group();
    GraphicsContext gc;
    ScAchse scWidth;
    ScAchse scHeigth;
    Territorium territorium;
    ScrollPane scrollPane;
    private boolean changed = false;
    public static final double timeDeath = 2000;
    public static final double timeWave = 1000;

    static Properties prop;
    static ResourceBundle rb;
    static Locale locale;

    Image imageBackground = null;
    String backgroundString = "";

    Pane pane;
    Rectangle rectangleForDeath;

    private int x, y;
    private double objectwidth;
    private double objectheigth;
    private double objectwidthpercent;
    private double objectheigthpercent;
    private double groeßeX;
    private double groeßeY;

    private AnimationController animation;

    /*
     * Gibt dem UBoot beim Tod durch den Spieler eine TodesAnimation
     */
    private void deathAnimation(Territorium t) {
        pane = new Pane();

        rectangleForDeath = new Rectangle(0, 0, canvas.getWidth(), canvas.getHeight());
        rectangleForDeath.setFill(Color.BLACK);

        FadeTransition fade = new FadeTransition(Duration.millis(timeDeath), rectangleForDeath);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setAutoReverse(true);
        fade.setCycleCount(2);

        pane.getChildren().add(rectangleForDeath);

        fade.play();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(timeDeath), ae -> {
            getTerritorium().setDeath(false);
            updateCanvas();
        }));
        timeline.play();

        getAnimation().paneControll(timeDeath, pane, root);

        root.getChildren().add(pane);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    /*
     * Manche gr��en des Spielfeldes geben Bilder als Hintergrund vor, welche
     * hier vorgeladen werden, falls ein solches Bild da ist
     */
    public void setPictureBundle() {
        TerritoriumPanel.prop = new Properties();
        TerritoriumPanel.locale = Locale.getDefault();
        Locale.setDefault(TerritoriumPanel.locale);
        JComponent.setDefaultLocale(TerritoriumPanel.locale);
        TerritoriumPanel.rb = ResourceBundle.getBundle("resources.PictureBundle",
                TerritoriumPanel.locale);
    }

    public TerritoriumPanel(Territorium t, ScAchse sx, ScAchse sy, AnimationController a) {
        setTerritorium(t);
        setPictureBundle();
        drawField(t);
        setRoot();
        scrollPane = new ScrollPane(root);
        scWidth = sx;
        scHeigth = sy;
        setAnimation(a);
    }

    /*
     * Diese Methode zeichnet das Feld, wobei je nach Einstellungen und Gr��en
     * andere Zeichenarten/gr��en gew�hlt werden. Weiterhin werden hier auch
     * weitere gr��en gesetzt, welche die Feldbreiten/hoehen angeben
     *
     * F�r weitere DetailInfos in der Methode nachschauen
     */
    public void drawField(Territorium t) {
        objectwidth = Territorium.OBJ_WIDTH;
        objectheigth = Territorium.OBJ_WIDTH;
        objectwidthpercent = 1;
        objectheigthpercent = 1;
        groeßeX = Territorium.OBJ_WIDTH * t.getFeldBreite() + 2 * Oberflaeche.CANVAS_RAND_ABSTAND;
        groeßeY = Territorium.OBJ_WIDTH * t.getFeldHoehe() + 2 * Oberflaeche.CANVAS_RAND_ABSTAND;
        /*
         * erster if-block macht das canvas "resizable", folglich wird das Feld
		 * kleiner gezeichnet als sonst, wenn es nicht in die Scrollpane passt
		 * der zweite if-block befasst sich mit der m�glichkeit, dass das canvas
		 * kleiner ist als der gegebene Bereich. Sie bewirkt, dass das Canvas
		 * immer in der Mitte bleibt der Dritte Block ist die Fallback l�sung,
		 * falls das Canvas z.B. nicht verkleinert werden soll (also als nicht
		 * "resizable" angesehen werden soll
		 */
        if (getTerritorium().isResizeable() && getScWidth() != null && getScHeigth() != null
                && (getScWidth().getValue() < groeßeX || getScHeigth().getValue() < groeßeY)) {
            t.setEntfernungBreiteDesSpielFeldes(0);
            t.setEntfernungHoeheDesSpielFeldes(0);
            if (getScWidth().getValue() > groeßeX && getScHeigth().getValue() > groeßeY) {
                x = (int) (Oberflaeche.CANVAS_RAND_ABSTAND
                        + ((getScWidth().getValue() / 2.0) - (Territorium.OBJ_WIDTH * t.getFeldBreite() / 2.0)));
                y = (int) (Oberflaeche.CANVAS_RAND_ABSTAND
                        + ((getScHeigth().getValue() / 2.0) - (Territorium.OBJ_WIDTH * t.getFeldHoehe() / 2.0)));
            } else {
                x = Oberflaeche.CANVAS_RAND_ABSTAND;
                y = Oberflaeche.CANVAS_RAND_ABSTAND;
            }
            objectwidth = ((getScWidth().getValue() - Oberflaeche.CANVAS_RAND_ABSTAND) / groeßeX)
                    * Territorium.OBJ_WIDTH;
            objectheigth = ((getScHeigth().getValue() - Oberflaeche.CANVAS_RAND_ABSTAND) / groeßeY)
                    * Territorium.OBJ_WIDTH;
            if (objectwidth < Territorium.OBJ_MIN_WIDTH) {
                objectwidth = Territorium.OBJ_MIN_WIDTH;
            }
            if (objectheigth < Territorium.OBJ_MIN_WIDTH) {
                objectheigth = Territorium.OBJ_MIN_WIDTH;
            }
            if (objectwidth > Territorium.OBJ_WIDTH) {
                objectwidth = Territorium.OBJ_WIDTH;
            }
            if (objectheigth > Territorium.OBJ_WIDTH) {
                objectheigth = Territorium.OBJ_WIDTH;
            }
            objectwidthpercent = objectwidth / Territorium.OBJ_WIDTH;
            objectheigthpercent = objectheigth / Territorium.OBJ_WIDTH;
            canvas.setWidth(t.getFeldBreite() * objectwidth + Oberflaeche.CANVAS_RAND_ABSTAND + x);
            canvas.setHeight(t.getFeldHoehe() * objectheigth + Oberflaeche.CANVAS_RAND_ABSTAND + y);
        } else if (getScWidth() != null && getScHeigth() != null && getScWidth().getValue() > groeßeX
                && getScHeigth().getValue() > groeßeY) {
            x = (int) (Oberflaeche.CANVAS_RAND_ABSTAND
                    + ((getScWidth().getValue() / 2.0) - (Territorium.OBJ_WIDTH * t.getFeldBreite() / 2.0)));
            y = (int) (Oberflaeche.CANVAS_RAND_ABSTAND
                    + ((getScHeigth().getValue() / 2.0) - (Territorium.OBJ_WIDTH * t.getFeldHoehe() / 2.0)));
            t.setEntfernungBreiteDesSpielFeldes(x - Oberflaeche.CANVAS_RAND_ABSTAND);
            t.setEntfernungHoeheDesSpielFeldes(y - Oberflaeche.CANVAS_RAND_ABSTAND);
            canvas.setWidth(getScWidth().getValue());
            canvas.setHeight(getScHeigth().getValue());
        } else {
            t.setEntfernungBreiteDesSpielFeldes(0);
            t.setEntfernungHoeheDesSpielFeldes(0);
            x = Oberflaeche.CANVAS_RAND_ABSTAND;
            y = Oberflaeche.CANVAS_RAND_ABSTAND;
            canvas.setWidth(Territorium.OBJ_WIDTH * t.getFeldBreite() + Oberflaeche.CANVAS_RAND_ABSTAND + x);
            canvas.setHeight(Territorium.OBJ_WIDTH * t.getFeldHoehe() + Oberflaeche.CANVAS_RAND_ABSTAND + y);
        }
        t.setObjectResizedWidth(objectwidth);
        t.setObjectResizedHeigth(objectheigth);
        int resetX = x;
        gc = canvas.getGraphicsContext2D();
        boolean picture = false;
        for (int i = 0; i < t.getFeldHoehe(); i++) {
            for (int j = 0; j < t.getFeldBreite(); j++) {
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokeRect(x, y, objectwidth, objectheigth);
                if (!picture) {
                    gc.setFill(Color.LIGHTBLUE);
                    gc.fillRect(x, y, objectwidth - 1, objectheigth - 1);
                }
                if (t.getFeld()[i][j] != FeldEigenschaft.Leer && t.getFeld()[i][j] != FeldEigenschaft.Location) {
                    Image image;
                    switch (t.getFeld()[i][j]) {
                        case Felsen:
                            image = felsenSmall;
                            break;
                        case Batterie:
                            image = batterieSmall;
                            break;
                        default:
                            image = deleteSmall;
                            break;
                    }
                    gc.drawImage(image, x, y, objectwidth, objectheigth);
                }
                for (int k = 0; k < territorium.getZielFelder().size(); k++) {
                    if (territorium.getZielFelder().get(k).getReihe() == i
                            && territorium.getZielFelder().get(k).getSpalte() == j) {
                        Integer h = k + 1;
                        gc.setFont(new Font(objectwidth));
                        gc.setFill(Color.BLACK);
                        double addX = objectwidth * 0.2;
                        double addY = (objectheigth * -0.1) + objectheigth;
                        if(h > 9){
                            addX = objectwidth * -0.1;
                        }
                        gc.fillText(h.toString(), x + addX, y + addY);

                        if(territorium.getNextGoalField().getReihe() == i &&
                                territorium.getNextGoalField().getSpalte() == j){
                            gc.setStroke(Color.LIGHTGREEN);
                            gc.setLineWidth(objectwidth * 0.1);
                            gc.strokeRect( x + objectwidth * 0.1, y + objectheigth * 0.1,
                                    objectwidth - objectwidth * 0.2, objectheigth - objectheigth * 0.2);
                        }
                    }
                }
                if (i == t.feldReiheRoboter && j == t.feldSpalteRoboter) {
                    Image image = submarineBugNordSmall;
                    gc.drawImage(image, x, y, objectwidth, objectheigth);
                } else if (i == t.feldReiheKind && j == t.feldSpalteKind) {
                    Image image = submarineHeckNordSmall;
                    gc.drawImage(image, x, y, objectwidth, objectheigth);
                }
                x = x + (int) objectwidth;
            }
            x = resetX;
            y = y + (int) objectheigth;
        }
    }

    public Group getRoot() {
        return root;
    }


    public void setRoot() {
        root.getChildren().addAll(canvas);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public Territorium getTerritorium() {
        return territorium;
    }

    public void setTerritorium(Territorium territorium) {
        this.territorium = territorium;
        this.territorium.addObserver(this);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        Platform.runLater(this::updateCanvas);
        setChanged(true);
    }

    private void updateCanvas() {
        if (getTerritorium().isDeath()) {
            deathAnimation(getTerritorium());
        } else {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.setHeight(0);
            canvas.setWidth(0);
            drawField(getTerritorium());
        }
    }

    public ScAchse getScWidth() {
        return scWidth;
    }

    public void setScWidth(ScAchse scWidth) {
        this.scWidth = scWidth;
    }

    public ScAchse getScHeigth() {
        return scHeigth;
    }

    public void setScHeigth(ScAchse scHeigth) {
        this.scHeigth = scHeigth;
    }

    public synchronized boolean isChanged() {
        return changed;
    }

    public synchronized void setChanged(boolean changed) {
        this.changed = changed;
    }

    public AnimationController getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationController animation) {
        this.animation = animation;
    }
}
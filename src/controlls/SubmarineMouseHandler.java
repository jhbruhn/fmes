package controlls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ResourceBundle;

import modell.Territorium;
import modell.Roboter;
import resources.Invisible;
import views.Oberflaeche;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SubmarineMouseHandler {
	private Territorium territorium;
	private SubmarineEvents compileSubmarineEvents;
	private Stage primaryStage;
	private ResourceBundle rb;

	/*
	 * Construktor welcher die Propertys mit dieser Klasse verbindet
	 */
	public SubmarineMouseHandler(Territorium t, SubmarineEvents comp, ResourceBundle rb,
			Stage stage) {
		setTerritorium(t);
		setCompileSubmarineEvents(comp);
		setRb(rb);
		setPrimaryStage(stage);
	}

	boolean submarineThereOnStartDrag = false;

	/*
	 * setzt den MouseHandler f�r das Canvas, damit drag and drop funktioniert
	 */
	public void handlerSetzen(Canvas canvas) {
		canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent mouseEvent) {
				if (submarineThereOnStartDrag) {
					versetzeSubmarine(mouseEvent.getX(), mouseEvent.getY(), getTerritorium());
				}
			}
		});
		canvas.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent mouseEvent) {
				submarineThereOnStartDrag = isTheSubmarineThere(mouseEvent.getX(), mouseEvent.getY(),
						getTerritorium());
			}
		});
		canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent mouseEvent) {
				submarineThereOnStartDrag = false;
			}
		});

		canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent mouseEvent) {
				if (mouseEvent.getButton().toString().equals("SECONDARY") && isTheSubmarineThere(mouseEvent.getX(),
						mouseEvent.getY(), getTerritorium())) {
					final ContextMenu contextMenu = new ContextMenu();
					// Ich packe das ganze in ein TextField, da ich weiter mit
					// javafx alles l�sen m�chte (wenn irgendwie m�glich)
					TextField textField = new TextField();
					
					for (java.lang.reflect.Method m : territorium.getRoboter().getClass().getMethods()) {
						
						boolean hinzufuegen = true;
						Invisible inv = m.getAnnotation(Invisible.class);
						if (inv != null) {
							hinzufuegen = false;
						}
						if(Modifier.isStatic(m.getModifiers()) || Modifier.isAbstract(m.getModifiers())){
							hinzufuegen = false;
						}
						for (java.lang.reflect.Method m2 : (new Roboter(getTerritorium())).getClass().getSuperclass()
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
							if (m.getParameterCount() > 0 || !m.getReturnType().equals(Void.TYPE)) {
								item.setDisable(true);
							}
							if(getTerritorium().getRoboter().isAlive() || getTerritorium().getRoboter().isRunning()){
								item.setDisable(true);
							}
							item.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {

									try {
										territorium.setNotify(false);
										m.invoke(territorium.getRoboter());
										territorium.setNotify(true);
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (InvocationTargetException e) {
										e.printStackTrace();
									}
								}
							});
							contextMenu.getItems().add(item);
						}
					}
					textField.setContextMenu(contextMenu);
					contextMenu.show(canvas, mouseEvent.getScreenX(), mouseEvent.getScreenY());
				}
			}
		});

	}

	/*
	 * setzt das Submarine auf die Angegebene/Angeklickte Reihe und Spalte
	 */
	public void versetzeSubmarine(double x, double y, Territorium t) {
		if (darfIchZiehen(x, y, t)) {
			t.setzeUBootAufsFeld(
					(int) ((y - Oberflaeche.CANVAS_RAND_ABSTAND - getTerritorium().getEntfernungHoeheDesSpielFeldes())
							/ t.getObjectResizedHeigth()),
					(int) ((x - Oberflaeche.CANVAS_RAND_ABSTAND - getTerritorium().getEntfernungBreiteDesSpielFeldes())
							/ t.getObjectResizedWidth()));
			setTerritorium(t);
		}
	}

	/*
	 * Gibt an, ob das Feld f�r das U-Boot erreichbar w�re
	 */
	public boolean darfIchZiehen(double x, double y, Territorium t) {
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
			return t.setzeUBootAufsFeldAbfrage(
					(int) ((y - Oberflaeche.CANVAS_RAND_ABSTAND - getTerritorium().getEntfernungHoeheDesSpielFeldes())
							/ t.getObjectResizedHeigth()),
					(int) ((x - Oberflaeche.CANVAS_RAND_ABSTAND - getTerritorium().getEntfernungBreiteDesSpielFeldes())
							/ t.getObjectResizedWidth()));
		} else {
			return false;
		}
	}

	/*
	 * fragt auf DragAnfang ab, ob das U-Boot auch auf dem Startfeld ist.
	 */
	public boolean isTheSubmarineThere(double x, double y, Territorium t) {
		if (t.feldReiheRoboter == ((int) ((y - Oberflaeche.CANVAS_RAND_ABSTAND
				- t.getEntfernungHoeheDesSpielFeldes()) / t.getObjectResizedHeigth()))
				&& t.feldSpalteRoboter == ((int) ((x - Oberflaeche.CANVAS_RAND_ABSTAND
						- t.getEntfernungBreiteDesSpielFeldes()) / t.getObjectResizedWidth()))) {
			return true;
		}
		return false;
	}

	public Territorium getTerritorium() {
		return territorium;
	}

	public void setTerritorium(Territorium territorium) {
		this.territorium = territorium;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public ResourceBundle getRb() {
		return rb;
	}

	public void setRb(ResourceBundle rb) {
		this.rb = rb;
	}

	public SubmarineEvents getCompileSubmarineEvents() {
		return compileSubmarineEvents;
	}

	public void setCompileSubmarineEvents(SubmarineEvents compileSubmarineEvents) {
		this.compileSubmarineEvents = compileSubmarineEvents;
	}

}
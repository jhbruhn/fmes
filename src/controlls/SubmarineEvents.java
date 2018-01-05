package controlls;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.lang.model.SourceVersion;

import controlls.LoadAndSaveCode.Picture;
import dbZugriffe.CreateDB;
import dbZugriffe.InsertStatements;
import dbZugriffe.SelectStatements;
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

public class SubmarineEvents {
	private Territorium territorium;
	private Start start;
	private final static String fehlerText = "999Fehler%&%Abbruch";

	/*
	 * normaler Konstruktor
	 */
	public SubmarineEvents(Territorium t) {
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
			t.vorBewegen();
			break;
		case 'b':
			t.rueckBewegen();
			break;
		case 'l':
			t.linksBewegen();
			break;
		case 'r':
			t.rechtsBewegen();
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
			ComboBox<ImageView> c) {
		rm.setOnAction(e -> {
			scSetMouseClickedEvent(f, tp, rm);
			c.getSelectionModel().select(select);
		});
	}

	/*
	 * Vor Methode um abzupr�fen wo das Event des hinclickens passiert ist. Mit
	 * den neuen X und Y koordinaten wird dann das Objekt platziert
	 */
	public void scSetMouseClickedEvent(FeldEigenschaft f, TerritoriumPanel tp, RadioMenuItem m) {
		if (m.isSelected()) {
			tp.getCanvas().setOnMouseClicked(
					event -> setObjectOnTheFieldEvent(f, (int) (event.getX()), (int) (event.getY()), m));
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
	 * Kontrolliert das schlie�en der Oberflaeche, sodass wenn ben�tigt
	 * gespeichert wird!
	 */
	public void closeEvent(Stage primary, TextArea text, ResourceBundle rb, Oberflaeche o) {
		SimpleBooleanProperty close = new SimpleBooleanProperty();
		close.addListener(e -> {
			getStart().onClose(o);
			primary.close();
		});
		if (o.isTextChanged()) {
			saveCode(primary, text, primary.getTitle(), rb, getTerritorium().getRoboter(), close);
		} else {
			close.set(true);
		}
	}

	/*
	 * Setzt das Ausgew�hlte Objekt auf das Feld, welches ausgew�hlt wurde.
	 * Dabei sind die X und Y Koordinate die �bergebenen Werte, welche vom
	 * Eventhandler generiert wurden
	 */
	public void setObjectOnTheFieldEvent(FeldEigenschaft f, int x, int y, RadioMenuItem m) {
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
				if (f == null) {
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
							f);
				}
				setTerritorium(t);
			}
		}
	}

	/*
	 * gibt dem Button das speichernEvent. Dabei hat die SimpleBooleanProperty
	 * hier keinen Effekt, sie wird nur an vielen anderen Stellen des aufrufes
	 * von saveCode ben�tigt
	 */
	public void speichernEvent(Button b, TextArea text, Stage stage, ResourceBundle rb, Roboter uboot) {
		b.setOnAction(e -> saveCode(stage, text, stage.getTitle(), rb, uboot, new SimpleBooleanProperty()));
	}

	/*
	 * speichert den Code, falls die SimpleBooleanProperty dabei verbunden ist
	 * mit einem Close-Event, wird dieses danach ausgef�hrt. In der Methode wird
	 * der Benutzer aufgefordert seinen Code mit neuem Namen zu speichern, falls
	 * der Name noch der defaultName ist. Sollte der Text nicht akzeptabel sind,
	 * wird der Vorgang wiederholt, bis der Code gespeichert werden kann
	 */
	public void saveCode(Stage stage, TextArea text, String stageTitle, ResourceBundle rb, Roboter uboot,
			SimpleBooleanProperty close) {
		if (stageTitle.equals(LoadAndSaveCode.defaultName)) {
			PrimaryStageName p = new PrimaryStageName();
			p.nameProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.equals(fehlerText)) {
						if (!getStart().isOberflaecheVorhanden(p.getValue())) {
							saveCode(stage, text, newValue, rb, uboot, close);
						} else {
							getStart().getOberflaecheByKey(newValue).getPrimaryStage().requestFocus();
							getStart().getOberflaecheByKey(newValue).getPrimaryStage().toFront();
							fehlerAlertFuerDoppelteNamen(rb);
						}
					} else {
						close.set(true);
					}
				}
			});
			waehleName(p);
		} else {
			String code = erstelleSpeicherbarenCode(stageTitle, text);
			LoadAndSaveCode.save(stageTitle, code);
			getStart().replaceStringKey(stage.getTitle(), stageTitle);
			stage.setTitle(stageTitle);
			close.set(true);
		}
	}

	/*
	 * verwei�t auf das Speichern als Bild in LoadAndSave und w�hlt durch p das
	 * Richtige aus, welches zu Speichern ist. Diese Methode existiert, da der
	 * Code an mehreren Stellen ben�tigt wird und ein direktes verweisen dadurch
	 * "unsch�n"
	 */
	public void speichereAlsBild(MenuItem m, TerritoriumPanel tp, String titel, Picture p) {
		m.setOnAction(e -> LoadAndSaveCode.saveAsPicture(tp.getCanvas(), titel, p));
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

	/*
	 * der Name ist Programme xD
	 */
	public void ladeCodeInTextArea(TextArea text, String pathname) {
		String code = LoadAndSaveCode.load(pathname);
		text.setText(code);
	}

	String pathName = "";

	/*
	 * l�d den Code, welcher vorher aus einem vorsortierten Fenster ausgew�hlt
	 * werden kann (mit Namen des Projekts)
	 */
	public void ladeCode(TextArea text, Stage prStage) {
		pathName = "";
		Stage laden = new Stage();
		List<String> list = LoadAndSaveCode.searchFile(null);
		ObservableList<String> options = FXCollections.observableArrayList();
		for (String s : list) {
			boolean b = true;
			for (String v : Start.hashOberflaeche.keySet()) {
				v = v + ".java";
				if (v.equals(s) || !s.endsWith(".java")) {
					b = false;
				}
			}
			if (b) {
				options.add(s);
			}
		}
		ComboBox<String> comboLaden = new ComboBox<String>(options);

		Button buttonLadenName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		Button buttonAbbrechenLaden = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
		buttonLadenName.setOnAction(e -> {
			ladeCodeInTextArea(text, pathName);
			String[] s = pathName.split(".java");
			getStart().replaceStringKey(prStage.getTitle(), s[0]);
			prStage.setTitle(s[0]);
			laden.close();
		});
		buttonAbbrechenLaden.setOnAction(e -> {
			laden.close();
		});
		buttonLadenName.setDisable(true);
		comboLaden.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				buttonLadenName.setDisable(false);
				pathName = newValue;
			}
		});

		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		comboLaden.setPrefWidth(150);
		grid.setPadding(new Insets(25, 30, 25, 30));
		grid.add(comboLaden, 0, 0, 3, 3);
		grid.add(buttonLadenName, 0, 3);
		grid.add(buttonAbbrechenLaden, 2, 3);

		laden.setScene(new Scene(grid, 300, 150));
		laden.show();
	}

	/*
	 * w�hlt den Namen f�r das neue Dokment und gibt beim Abrechen einen
	 * FehlerCode in das �bergebene Objekt ein
	 */
	public void waehleName(PrimaryStageName p) {
		Stage name = new Stage();
		GridPane grid = new GridPane();
		TextField textName = new TextField();
		Button buttonName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		Button buttonAbbrechenName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
		buttonAbbrechenName.setOnAction(e -> {
			p.setName(fehlerText);
			name.close();
		});
		buttonName.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				p.setName(textName.getText());
				name.close();
			}
		});

		buttonName.setDisable(true);
		textName.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (SourceVersion.isIdentifier(newValue) && !SourceVersion.isKeyword(newValue)
						&& !newValue.equals(LoadAndSaveCode.defaultName)) {
					buttonName.setDisable(false);
				} else {
					buttonName.setDisable(true);
				}
			} catch (Exception e) {
				buttonName.setDisable(true);
			}
		});
		grid.add(textName, 1, 1, 3, 1);
		grid.add(buttonName, 1, 2);
		buttonName.setAlignment(Pos.CENTER);
		grid.add(buttonAbbrechenName, 3, 2);
		buttonAbbrechenName.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(25, 25, 25, 25));
		ColumnConstraints column0 = new ColumnConstraints();
		column0.setPercentWidth(5);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(40);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(10);
		grid.getColumnConstraints().addAll(column0, column1, column2, column1, column0);
		name.setScene(new Scene(grid, 350, 150));
		name.setOnCloseRequest(e -> {
			p.setName(fehlerText);
			name.close();
		});
		name.show();
	}

	/*
	 * neues Dokument �ffnen
	 */
	public void neuesFensterOeffnen(MenuItem m, Button b, ResourceBundle rb) {
		m.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage primaryStage = new Stage();
				PrimaryStageName p = new PrimaryStageName();
				p.nameProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (!p.getValue().equals(fehlerText)) {
							if (!getStart().isOberflaecheVorhanden(p.getValue())) {
								primaryStage.setTitle(p.getValue());
								try {
									getStart().start(primaryStage);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								getStart().getOberflaecheByKey(newValue).getPrimaryStage().requestFocus();
								getStart().getOberflaecheByKey(newValue).getPrimaryStage().toFront();
								fehlerAlertFuerDoppelteNamen(rb);
							}
						}
					}
				});
				waehleName(p);
			}
		});
		b.setOnAction(e -> m.fire());
	}

	private void fehlerAlertFuerDoppelteNamen(ResourceBundle rb) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(rb.getString("doppelNamenTitel"));
		alert.setHeaderText(rb.getString("doppelNamenHeader"));
		alert.setContentText(rb.getString("doppelNamenContent"));
		alert.showAndWait();
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
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		buttonOK.setTooltip(new Tooltip(rb.getString("buttonOK")));
		Button buttonAbbrechen = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
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
	 * druckt das Canvas aus
	 * 
	 * Note: meine Aufl�sung am PC schafft nur bis kurz �ber K�stchen im Spiel,
	 * weswegen ein Spielfeld von der Gr��e von 50 etc. nicht mehr richtig
	 * gedruckt wird..
	 */
	public void print(Territorium t, ScAchse sx, ScAchse sy, Canvas canvas1, AnimationController an) {
		TerritoriumPanel c = new TerritoriumPanel(new Territorium(), sx, sy, an);
		Canvas canvas = c.getCanvas();
		Printer printer = Printer.getDefaultPrinter();
		PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
				Printer.MarginType.EQUAL_OPPOSITES);
		double scaleX = pageLayout.getPrintableWidth() / c.getCanvas().getBoundsInParent().getWidth();// (((pageLayout.getPaper().getWidth()
																										// -
																										// 0.5)
																										// *
																										// 25.4)
																										// /
																										// 72)
																										// /
																										// (canvas.getBoundsInParent().getWidth()
																										// *
																										// 3);
		double scaleY = pageLayout.getPrintableHeight() / c.getCanvas().getBoundsInParent().getHeight();
		// (((pageLayout.getPaper().getHeight() - 0.5) * 25.4) / 72) /
		// (canvas.getBoundsInParent().getHeight() * 3);
		canvas.getTransforms().add(new Scale(scaleX, scaleY));

		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null && job.showPrintDialog(canvas1.getScene().getWindow())) {
			boolean success = job.printPage(pageLayout, canvas);
			if (success) {
				job.endJob();
			}
		}
	}

	/*
	 * druckt das TextArea aus
	 */
	public void print(TextArea text) {
		Node node = text;
		Printer printer = Printer.getDefaultPrinter();
		PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
				Printer.MarginType.DEFAULT);
		double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
		double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
		node.getTransforms().add(new Scale(scaleX, scaleY));

		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			boolean success = job.printPage(node);
			if (success) {
				job.endJob();
			}
		}
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

	/*
	 * Speicher das Territorium und seinen nebenstehenden Code in die Datenbank
	 * unter zuhernahme von Tags, mit welchen die Daten sp�ter wieder gefunden
	 * werden k�nnen
	 */
	public void saveExample(ResourceBundle rb, Territorium t, TextArea text, String name) {
		String create = new String();
		if (new CreateDB().checkAndCreateDB()) {
			create = rb.getString("createDB");
		} else {
			create = rb.getString("dbWasThere");
		}
		Territorium copy = new Territorium();
		copy.copyTerritoriumIntoThis(t);
		PrimaryStageName p = new PrimaryStageName();
		p.nameProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(fehlerText)) {
					int i = p.getValue().indexOf("||<>");
					int j = p.getValue().indexOf("<>||");
					int nachfolgeLevel = -1;
					if (!p.getValue().substring(i + 4, j - 1).equals("-")) {
						nachfolgeLevel = Integer.parseInt(p.getValue().substring(i + 4, j));
					}
					int finish = Integer.parseInt(p.getValue().substring(j + 4, p.getValue().length()));
					String sub = p.getValue().substring(0, i);
					String[] s = sub.split(" ");
					copy.setTerritoriumSpecialFinishNummer(finish);
					InsertStatements.insertTag(s, copy, text, name);
				} else {
					// Hier nichts machen..
				}
			}
		});
		tagsEingebenSpecialNumber(p, create, rb);
	}

	/*
	 * L�d die gespeicherten Daten aus der Datenbank, unter selectierung der
	 * eingegebenen Tags
	 */
	public void loadExample(ResourceBundle rb, TextArea textArea) {
		String create = new String();
		if (new CreateDB().checkAndCreateDB()) {
			create = rb.getString("createDB");
		} else {
			create = rb.getString("dbWasThere");
		}
		// for(String s : SelectStatements.getTAllTags()){
		// System.out.println(s);
		// }

		PrimaryStageName p = new PrimaryStageName();
		p.nameProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.equals(fehlerText)) {
					PrimaryStageName p2 = new PrimaryStageName();
					p2.nameProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							if (!newValue.equals(fehlerText)) {
								int i = Integer.parseInt(newValue.split(" ")[0]);
								terriLaden(getTerritorium(), textArea, i);
							} else {
								// Hier nichts machen..
							}
						}
					});
					ArrayList<String> array = new ArrayList<String>();
					for (String tag : newValue.split(" ")) {
						array.add(tag);
					}
					ArrayList<String> namen = SelectStatements.getTerriDaten(array);
					for (int j = 0; j < namen.size(); j++) {
						String dublikat = namen.get(j);
						int count = 0;
						for (int i = 0; i < namen.size(); i++) {
							if (namen.get(i).equals(dublikat)) {
								count = count + 1;
								if (count > 1) {
									namen.remove(i);
								}
							}
						}
					}
					nameAuswaehlen(p2, rb, namen);
				} else {
					// Hier nichts machen..
				}
			}
		});
		// SelectStatements.gibAlleAus();
		tagsEingeben(p, create, rb);
	}

	private static void terriLaden(Territorium territorium, TextArea textArea, int id) {
		String result = SelectStatements.getDatenFromTerri(id);
		int i = result.indexOf("||<>");
		String quelltext = result.substring(i + 4, result.length());
		String terri = result.substring(0, i);
		textArea.setText(quelltext);
		LoadAndSaveCode.fromXmlMarshal(territorium, terri);
	}

	private void nameAuswaehlen(PrimaryStageName p, ResourceBundle rb, ArrayList<String> list) {
		pathName = "";
		Stage laden = new Stage();
		ObservableList<String> options = FXCollections.observableArrayList();
		for (String s : list) {
			options.add(s);
		}
		ComboBox<String> comboLaden = new ComboBox<String>(options);

		Button buttonLadenName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		Button buttonAbbrechenLaden = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
		buttonLadenName.setOnAction(e -> {
			p.setName(pathName);
			laden.close();
		});
		buttonAbbrechenLaden.setOnAction(e -> {
			p.setName(fehlerText);
			laden.close();
		});
		buttonLadenName.setDisable(true);
		comboLaden.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				buttonLadenName.setDisable(false);
				pathName = newValue;
			}
		});

		GridPane grid = new GridPane();
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.add(comboLaden, 0, 0, 3, 3);
		grid.add(buttonLadenName, 0, 3);
		grid.add(buttonAbbrechenLaden, 2, 3);

		laden.setScene(new Scene(grid, 300, 300));
		laden.setOnCloseRequest(e -> {
			p.setName(fehlerText);
			laden.close();
		});
		laden.show();

	}

	private void tagsEingeben(PrimaryStageName p, String create, ResourceBundle rb) {
		Stage stage = new Stage();
		GridPane grid = new GridPane();
		TextField textName = new TextField();
		Button buttonName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		Button buttonAbbrechenName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
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
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/startSmall.png"))));
		Button buttonAbbrechenName = new Button("", new ImageView(
				new Image(getClass().getResourceAsStream("../resourcesPicturesAndSoundsVidoes/stopSmall.png"))));
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
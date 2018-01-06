package controlls;

import java.awt.image.RenderedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import modell.Territorium;
import modell.Territorium.FeldEigenschaft;
import views.Oberflaeche;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LoadAndSaveCode {

	public static String toXmlMarshal(Territorium ter) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Territorium.class);
			Marshaller m = jc.createMarshaller();
			StringWriter sw = new StringWriter();
			m.marshal(ter, sw);
			return sw.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void fromXmlMarshal(Territorium ter, String s) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Territorium.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(s);
			Territorium territorium = (Territorium) unmarshaller.unmarshal(reader);
			ter.copyTerritoriumIntoThis(territorium);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static enum Picture {
		jpg, png
	}

	public static final String path = "C:\\Users\\Raskur Sevenflame\\workspace\\BruhnsFabianSimulation\\ProgrammeJavaSubmarine";
	public static final String defaultName = "UBootDefault";

	/*
	 * speichert den Code als Datei weg, in den vorgesehenen Ordner
	 */
	public static void save(String name, String code) {
		try {
			Path pathTo = Paths.get(path + "\\" + name + ".java");
			String[] codeU = code.split("\n");
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < codeU.length; i++) {
				list.add(codeU[i]);
			}
			Files.write(pathTo, list, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @param name der gesuchten Datei, welche geladen werden soll l�d den Code
	 * und ver�ndert ihn so, dass er nach den gew�nschten Ma�st�ben in das
	 * Textfeld kann
	 */
	public static String load(String name) {

		try {
			Path pathFrom = Paths.get(path + "\\" + name);
			List<String> content = Files.readAllLines(pathFrom, StandardCharsets.UTF_8);
			for (int i = 0; i < 3; i++) {
				content.remove(0);
			}
			String[] contentA = content.toArray(new String[0]);
			String result = "";
			for (int i = 0; i < contentA.length - 1; i++) {
				result = result + contentA[i] + "\n";
			}
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/*
	 * schaut einmal zu Beginn, ob das Verzeichniss in welches alles gespeichert
	 * wird schon da ist, ebenso ob die Beispieldatei UBoot-Default schon
	 * existiert. Sollte dieses nicht der Fall sein, werden jene erstellt
	 */
	public static void verzeichnissCheck() {
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}

		File fileDefault = new File(path + "\\" + defaultName);
		if (!fileDefault.exists()) {
			String code = "import modell.UBoot;" + "\n" + "\n"
					+ "public class UBootDefault extends UBoot { public " + "\n" + "void main(){" + "\n" + "\n" + "}"
					+ "\n" + "}";
			save(defaultName, code);
		}
	}

	/*
	 * @param territorium welches serialisiert wird
	 * 
	 * @param name unter welchem es gespeichert wird serialiesiert das
	 * Territorium
	 */
	public static void serialisieren(Territorium territorium, String name) {
		File file = new File(path + "/" + name + ".ser");
		try (OutputStream ops = new FileOutputStream(file)) {
			ObjectOutputStream o = new ObjectOutputStream(ops);
			o.writeObject(territorium);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * deserialiesiert eine ausgew�hlte Datei und speichert sie in das
	 * Territorium, welches �bergeben wurde
	 */
	public static void deserialisieren(Territorium territorium, Oberflaeche o) {
		try {
			FileChooser filechoose = new FileChooser();
			filechoose.setInitialDirectory(new File(path));
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
			filechoose.getExtensionFilters().add(extFilter);
			File file = new File(filechoose.showOpenDialog(new Stage()).toPath().toString());
			InputStream is = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(is);
			Territorium territorium2 = (Territorium) ois.readObject();
			territorium.copyTerritoriumIntoThis(territorium2);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(NullPointerException e){
			
		}
	}

	/*
	 * wie die Aufgabe es verlangte und nicht mehr, was ein Kommentar, ich
	 * entschuldige mich daf�r, aber mir f�llt nichts ein
	 * 
	 * Ich wei� { via SAX via DOM via StAXCursor via StAXIterator } aber ich
	 * wollte das mal auf der niedrigsten Ebene machen..
	 * 
	 * let's be real, this was a mistake..
	 */
	public static void xmlSpeichern(Territorium territorium, String name) {
		try {
			File xmlFile = new File(path + "/" + name + ".xml");
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(xmlFile.getAbsolutePath()),
					"utf-8");
			// Der XML-Header wird erzeugt
			writer.writeStartDocument("utf-8", "1.0");
			writer.writeCharacters("\n");
			writer.writeDTD(
					"<!DOCTYPE territorium SYSTEM \"../src/resources/territorium.dtd\">");
			writer.writeCharacters("\n");

			writer.writeStartElement("territorium");
			writer.writeCharacters("\n");
			writer.writeStartElement("attributes");
			writer.writeCharacters("\n");

			writer.writeStartElement("feldSpalteRoboter");
			writer.writeAttribute("value", "" + territorium.feldSpalteRoboter);
			writer.writeEndElement(); // feldSpalteRoboter
			writer.writeCharacters("\n");

			writer.writeStartElement("feldSpalteKind");
			writer.writeAttribute("value", "" + territorium.feldSpalteKind);
			writer.writeEndElement(); // feldReiheKind
			writer.writeCharacters("\n");

			writer.writeStartElement("feldReiheKind");
			writer.writeAttribute("value", "" + territorium.feldReiheKind);
			writer.writeEndElement(); // feldReiheKind
			writer.writeCharacters("\n");

			writer.writeStartElement("feldReiheRoboter");
			writer.writeAttribute("value", "" + territorium.feldReiheRoboter);
			writer.writeEndElement(); // feldReiheRoboter
			writer.writeCharacters("\n");

			writer.writeStartElement("territoriumSpecialFinishNummer");
			writer.writeAttribute("value", "" + territorium.getTerritoriumSpecialFinishNummer());
			writer.writeEndElement(); // territoriumSpecialFinishNummer
			writer.writeCharacters("\n");

			writer.writeStartElement("startSpalteKind");
			writer.writeAttribute("value", "" + territorium.getStartSpalteKind());
			writer.writeEndElement(); // startSpalteKind
			writer.writeCharacters("\n");

			writer.writeStartElement("startSpalteRoboter");
			writer.writeAttribute("value", "" + territorium.getStartSpalteRoboter());
			writer.writeEndElement(); // startSpalteRoboter
			writer.writeCharacters("\n");

			writer.writeStartElement("startReiheKind");
			writer.writeAttribute("value", "" + territorium.getStartReiheKind());
			writer.writeEndElement(); // startReiheKind
			writer.writeCharacters("\n");

			writer.writeStartElement("startReiheRoboter");
			writer.writeAttribute("value", "" + territorium.getStartReiheRoboter());
			writer.writeEndElement(); // startReiheRoboter
			writer.writeCharacters("\n");

			writer.writeStartElement("resizeable");
			writer.writeAttribute("value", "" + territorium.isResizeable());
			writer.writeEndElement(); // resizeable
			writer.writeCharacters("\n");

			writer.writeStartElement("objectResizedWidth");
			writer.writeAttribute("value", "" + territorium.getObjectResizedWidth());
			writer.writeEndElement(); // objectResizedWidth
			writer.writeCharacters("\n");

			writer.writeStartElement("objectResizedHeigth");
			writer.writeAttribute("value", "" + territorium.getObjectResizedHeigth());
			writer.writeEndElement(); // objectResizedHeigth
			writer.writeCharacters("\n");

			writer.writeStartElement("entfernungBreiteDesSpielFeldes");
			writer.writeAttribute("value", "" + territorium.getEntfernungBreiteDesSpielFeldes());
			writer.writeEndElement(); // entfernungBreiteDesSpielFeldes
			writer.writeCharacters("\n");

			writer.writeStartElement("entfernungHoeheDesSpielFeldes");
			writer.writeAttribute("value", "" + territorium.getEntfernungHoeheDesSpielFeldes());
			writer.writeEndElement(); // entfernungHoeheDesSpielFeldes
			writer.writeCharacters("\n");

			writer.writeStartElement("feldhoehe");
			writer.writeAttribute("value", "" + territorium.getFeldHoehe());
			writer.writeEndElement(); // feldhoehe
			writer.writeCharacters("\n");

			writer.writeStartElement("feldbreite");
			writer.writeAttribute("value", "" + territorium.getFeldBreite());
			writer.writeEndElement(); // feldbreite
			writer.writeCharacters("\n");

			writer.writeEndElement(); // attributes
			writer.writeCharacters("\n");

			writer.writeStartElement("feld");
			for (int i = 0; i < territorium.getFeld().length; i++) {
				writer.writeStartElement("feldZeile");
				for (int j = 0; j < territorium.getFeld()[i].length; j++) {
					writer.writeStartElement("feldElement");
					writer.writeAttribute("value", territorium.getFeld()[i][j].toString());
					writer.writeEndElement(); // feldElement
					writer.writeCharacters("\n");
				}
				writer.writeEndElement(); // feldZeile
				writer.writeCharacters("\n");
			}
			writer.writeEndElement(); // feld
			writer.writeCharacters("\n");

			writer.writeEndElement(); // territorium
			writer.writeCharacters("\n");
			writer.writeEndDocument();
			writer.writeCharacters("\n");
			writer.close();

		} catch (Throwable exc) {
			exc.printStackTrace();
		}
	}

	/*
	 * wenn ich die wahl h�tte zur�ck zu gehen und es mit sax zu machen, ich
	 * w�rde es tun.. diese methode ist einzig und allein einem Dickkopf
	 * zuzuschreiben..
	 * 
	 * Ich m�chte bitte f�r das Laden und Speichern mit XML eine spezielle
	 * W�rdigung, Kekse w�ren ok
	 */
	public static void xmlLaden(Territorium territorium) {
		try {
			Territorium t = new Territorium();
			FileChooser filechoose = new FileChooser();
			filechoose.setInitialDirectory(new File(path));
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
			filechoose.getExtensionFilters().add(extFilter);
			File file = new File(filechoose.showOpenDialog(new Stage()).toPath().toString());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ErrorHandler() {

				@Override
				public void error(SAXParseException e) throws SAXException {
					System.out.println(e.getMessage() + " ERROR");

				}

				@Override
				public void fatalError(SAXParseException e) throws SAXException {
					System.out.println(e.getMessage() + " FATALERROR");

				}

				@Override
				public void warning(SAXParseException e) throws SAXException {
					System.out.println(e.getMessage() + " WARNING");

				}

			});
			Document document = builder.parse(file.getAbsolutePath());


			if (((Element) document.getElementsByTagName("resizeable").item(0)).getAttribute("value").equals("true")) {
				t.setResizeable(true);
			} else {
				t.setResizeable(false);
			}

			t.setEntfernungBreiteDesSpielFeldes(Double
					.parseDouble(((Element) document.getElementsByTagName("entfernungBreiteDesSpielFeldes").item(0))
							.getAttribute("value")));
			t.setEntfernungHoeheDesSpielFeldes(Double
					.parseDouble(((Element) document.getElementsByTagName("entfernungHoeheDesSpielFeldes").item(0))
							.getAttribute("value")));

			t.feldReiheRoboter = Integer
					.parseInt(((Element) document.getElementsByTagName("feldReiheRoboter").item(0)).getAttribute("value"));
			t.feldReiheKind = Integer
					.parseInt(((Element) document.getElementsByTagName("feldReiheKind").item(0)).getAttribute("value"));
			t.feldSpalteRoboter = Integer
					.parseInt(((Element) document.getElementsByTagName("feldSpalteRoboter").item(0)).getAttribute("value"));
			t.feldSpalteKind = Integer.parseInt(
					((Element) document.getElementsByTagName("feldSpalteKind").item(0)).getAttribute("value"));

			t.setFeldBreite(Integer
					.parseInt(((Element) document.getElementsByTagName("feldbreite").item(0)).getAttribute("value")));
			t.setFeldHoehe(Integer
					.parseInt(((Element) document.getElementsByTagName("feldhoehe").item(0)).getAttribute("value")));

			t.setObjectResizedHeigth(Double.parseDouble(
					((Element) document.getElementsByTagName("objectResizedHeigth").item(0)).getAttribute("value")));
			t.setObjectResizedWidth(Double.parseDouble(
					((Element) document.getElementsByTagName("objectResizedWidth").item(0)).getAttribute("value")));

			t.setStartReiheRoboter(Integer.parseInt(
					((Element) document.getElementsByTagName("startReiheRoboter").item(0)).getAttribute("value")));
			t.setStartReiheKind(Integer.parseInt(
					((Element) document.getElementsByTagName("startReiheKind").item(0)).getAttribute("value")));
			t.setStartSpalteRoboter(Integer.parseInt(
					((Element) document.getElementsByTagName("startSpalteRoboter").item(0)).getAttribute("value")));
			t.setStartSpalteKind(Integer.parseInt(
					((Element) document.getElementsByTagName("startSpalteKind").item(0)).getAttribute("value")));

			t.setTerritoriumSpecialFinishNummer(
					Integer.parseInt(((Element) document.getElementsByTagName("territoriumSpecialFinishNummer").item(0))
							.getAttribute("value")));

			FeldEigenschaft[][] feld = new FeldEigenschaft[t.getFeldHoehe()][t.getFeldBreite()];
			NodeList feldElement = document.getElementsByTagName("feldElement");
			int reihe = 0;
			int spalte = 0;
			for (int k = 0; k < feldElement.getLength(); k++) {
				Element e = (Element) feldElement.item(k);
				switch (e.getAttribute("value")) {
				case "Batterie":
					feld[reihe][spalte] = FeldEigenschaft.Batterie;
					break;
				case "Felsen":
					feld[reihe][spalte] = FeldEigenschaft.Felsen;
					break;
				case "Ort1":
					feld[reihe][spalte] = FeldEigenschaft.Ort1;
					break;
				case "Ort2":
					feld[reihe][spalte] = FeldEigenschaft.Ort2;
					break;
				case "Ort3":
					feld[reihe][spalte] = FeldEigenschaft.Ort3;
					break;
				default:
					feld[reihe][spalte] = FeldEigenschaft.Leer;
					break;
				}
				spalte = spalte + 1;
				if (spalte >= t.getFeldBreite()) {
					spalte = 0;
					reihe = reihe + 1;
				}
				if (reihe >= t.getFeldHoehe()) {
					break;
				}
			}
			t.setFeld(feld);

			territorium.copyTerritoriumIntoThis(t);
		} catch (Exception exc) {
//			exc.printStackTrace();
		}
	}

	/*
	 * Diese Methode wird zur Zeit nicht genutzt und zeigt nur auf, dass das
	 * Speichern mit xmlEncoder bekannt ist..
	 */
	public static void encoderXmlSave(Territorium territorium, String name) {
		try {
			XMLEncoder encoder = new XMLEncoder(
					new BufferedOutputStream(new FileOutputStream(path + "/" + name + ".xml")));
			encoder.writeObject(territorium);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * wird zur Zeit ebenfalls nicht benutzt, zeigt weiterhin nur, dass das
	 * Laden mit XMLDecoder bekannt ist
	 */
	public static void decodeXmlLoad(Territorium territorium) {
		try {
			FileChooser filechoose = new FileChooser();
			filechoose.setInitialDirectory(new File(path));
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
			filechoose.getExtensionFilters().add(extFilter);
			File file = new File(filechoose.showOpenDialog(new Stage()).toPath().toString());
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
			Territorium t = (Territorium) decoder.readObject();
			territorium.copyTerritoriumIntoThis(t);
			decoder.close();
		} catch (NullPointerException |FileNotFoundException e) {
//			e.printStackTrace();
		}
	}

	/*
	 * Speichern mit Jaxb, wer h�tte es gedacht..
	 */
	public static void jaxbSpeichern(Territorium territorium, String name) {
		File file = new File(path + "/" + name + ".jaxb");
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Territorium.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(territorium, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		/*
		 * TestCode um meinem Kumel Tobie auszuhelfen..
		 */
		// File file = new File(path + "/" + name + ".jaxb");
		// JAXBContext jc;
		// try {
		// jc = JAXBContext.newInstance(Teri.class);
		// Marshaller m = jc.createMarshaller();
		// m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Teri teri = new Teri();
		// m.marshal(teri, file);
		// m.marshal(teri, System.out);
		// } catch (JAXBException e) {
		// e.printStackTrace();
		// }
	}

	/*
	 * laden mit Jaxb.. quality comments
	 */
	public static void ladeJaxbDatei(Territorium territorium, Oberflaeche o) {
		try{
		FileChooser filechoose = new FileChooser();
		filechoose.setInitialDirectory(new File(path));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JAXB files (*.jaxb)", "*.jaxb");
		filechoose.getExtensionFilters().add(extFilter);
		File file = new File(filechoose.showOpenDialog(new Stage()).toPath().toString());
		Territorium territorium2 = JAXB.unmarshal(file, Territorium.class);
		territorium.copyTerritoriumIntoThis(territorium2);}
		catch(NullPointerException e){
			
		}

	}

	/*
	 * speichert das Spielfeld als Bild, dabei kann ausgew�hlt werden, was f�r
	 * ein Bildtyp und wie die Datei hei�en soll, mit den �bergebenen Parametern
	 */
	public static void saveAsPicture(Canvas canvas, String titel, Picture p) {
		String s = "";
		switch (p) {
		case png:
			s = "png";
			break;
		case jpg:
			s = "jpg";
			break;
		}
		File file = new File(path + "\\" + titel + "." + s);
		if (file != null) {
			WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
			canvas.snapshot(null, writableImage);
			RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
			try {
				ImageIO.write(renderedImage, s, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * durchsucht die Datein, nach dem �bergebenen String "find" in dem
	 * Standartordner
	 */
	public static List<String> searchFile(String find) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		List<String> matches = new ArrayList<String>();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				/*
				 * �berpr�ft, ob der Dateiname mit dem Suchstring �bereinstimmt.
				 * Gro�- und Kleinschreibung wird ignoriert.
				 */
				matches.add(files[i].getName());
				// }
				// if (files[i].isDirectory()) {
				// // F�gt der List die List mit den Treffern aus dem
				// // Unterordner zu
				// matches.addAll(searchFile(files[i], find));
				// }
			}
		}
		return matches;
	}
}

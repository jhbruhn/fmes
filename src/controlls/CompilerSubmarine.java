package controlls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import modell.Territorium;
import modell.UBoot;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CompilerSubmarine {
	boolean startUp = true;

	/*
	 * Compiliert den Code im Textarea, und gibt eine Fehlermeldung aus, wenn
	 * dieser Fehler enth�lt. Sollte dieser Korrekt sein, so wird in das UBoot
	 * im Territorium ein neues gespeichert, welches aus der Class besteht,
	 * welche im Textarea entstanden ist. Die Fehlermeldung kommt nicht, wenn es
	 * beim startup passiert, dann wird nur das Default-UBoot gespeichert im
	 * UBoot
	 */
	@SuppressWarnings("resource")
	public synchronized boolean compilieren(Territorium territorium, ResourceBundle rb, String name) {
		try {

			File javaSrcFile = new File(LoadAndSaveCode.path + "\\" + name + ".java");

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

			StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null);
			Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(Arrays.asList(javaSrcFile));
			CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, units);
			boolean success = task.call();
			manager.close();
			if (startUp) {
				startUp = false;
				if (success) {
					try {
						File path = new File(LoadAndSaveCode.path);
						URL[] urls = new URL[] { path.toURI().toURL() };
						ClassLoader cl = new URLClassLoader(urls);
						Class<?> ubootClass = cl.loadClass(name);
						UBoot uboot = (UBoot) ubootClass.newInstance();
						uboot.setTerritorium(territorium);
						territorium.setUboot(uboot);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					return true;
				} else {
					UBoot uboot = new UBoot(territorium);
					territorium.setUboot(uboot);
					return true;
				}
			} else if (success) {
				try {
					File path = new File(LoadAndSaveCode.path);
					URL[] urls = new URL[] { path.toURI().toURL() };
					ClassLoader cl = new URLClassLoader(urls);
					Class<?> ubootClass = cl.loadClass(name);
					UBoot uboot = (UBoot) ubootClass.newInstance();
					uboot.setTerritorium(territorium);
					territorium.setUboot(uboot);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
					String fehler = new String();
					fehler = fehler + "Kind: " + diagnostic.getKind() + "\n";
					fehler = fehler + "Quelle: " + diagnostic.getSource() + "\n";
					fehler = fehler + "Code und Nachricht: " + diagnostic.getCode() + " " + diagnostic.getMessage(null)
							+ "\n";
					fehler = fehler + "Zeile: " + diagnostic.getLineNumber() + "\n";
					fehler = fehler + "Position/Spalte: " + diagnostic.getPosition() + " "
							+ diagnostic.getColumnNumber() + "\n";
					fehler = fehler + "Startpostion/Endposition: " + diagnostic.getStartPosition() + " "
							+ diagnostic.getEndPosition() + "\n";
					fehlerMeldungDesCompilers(fehler, rb);
				}
				return false;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
	}

	/*
	 * gibt die m�gliche Fehlermeldung des Compilers aus
	 */
	public void fehlerMeldungDesCompilers(String s, ResourceBundle rb) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(rb.getString("fehlerMeldungDesCompilersTitle"));
		alert.setHeaderText(rb.getString("fehlerMeldungDesCompilersHeader"));
		alert.setContentText(s);
		alert.showAndWait();
	}
}
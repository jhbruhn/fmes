package controlls;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class ShortCompiler {
	/*
	 * Mini-Compiler welcher bei Fehlern im Textarea eine Liste mit den
	 * Zeilennummern der Fehler zur�ck gibt. Diese Methode wird nur zur
	 * Berechnung f�r die Visuelle R�ckgabe von Fehlern ben�tigt. Dabei ist
	 * anzumerken, dass die Fehleranzeige bei zu vielen Fehlern nicht mehr
	 * akkurat ist (�hnlich wie in Eclispe selbst, wenn in jeder Zeile ein
	 * Fehler ist).
	 */
	public static ArrayList<Integer> shortCompile(String text, String name) {
		String src = text;
		StringJavaFileObject javaFile = new StringJavaFileObject(name, src);

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> units = Arrays.asList(javaFile);
		CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, units);
		boolean succes = task.call();
		if (succes) {
			javaFile.delete();
			new File(name + ".class").delete();
			return new ArrayList<Integer>();
		} else {
			ArrayList<Integer> fehler = new ArrayList<Integer>();
			for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
				fehler.add(((int) diagnostic.getLineNumber()) - 4);
			}
			javaFile.delete();
			new File(name + ".class").delete();
			return fehler;
		}
	}
}

/*
 * Hilfsklasse f�r den Compiler
 * 
 * @stolenBy Dibo
 */
class StringJavaFileObject extends SimpleJavaFileObject {
	private final CharSequence code;

	public StringJavaFileObject(String name, CharSequence code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}
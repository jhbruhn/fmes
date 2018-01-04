package modell;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

@SuppressWarnings("unused")
public class Internationalitaet {
	static Properties prop;
	static ResourceBundle rb;
	static Locale locale;
	static String language;

	public String getLanguage() {
		if(language == null){
			return "de";
		}
		return language;
	}

	public ResourceBundle getRb() {
		if (rb == null) {
			System.out.println("FEHLER: Internationalitaet keine Sprache gewaehlt!");
		}
		return rb;
	}

	public void setRb(ResourceBundle rb) {
		Internationalitaet.rb = rb;
	}

	public Internationalitaet(String s) {
		Internationalitaet.prop = new Properties();
		try {
			if (s == null) {
				Internationalitaet.prop.load(new FileInputStream("MessageBundle.properties"));
				language = Internationalitaet.prop.getProperty("language");
			} else {
				language = s;
			}
			if (language == null) {
				Internationalitaet.locale = Locale.getDefault();
			} else {
				Internationalitaet.locale = new Locale(language);
			}
		} catch (Throwable e) {
			Internationalitaet.locale = Locale.getDefault();
		}
		Locale.setDefault(Internationalitaet.locale);
		JComponent.setDefaultLocale(Internationalitaet.locale);
		Internationalitaet.rb = ResourceBundle.getBundle("resources.MessageBundle",
				Internationalitaet.locale);
	}

	static String readFile(String internalFile) {
		BufferedReader in = null;
		InputStream is = null;
		try {
			is = Internationalitaet.class.getClassLoader().getResourceAsStream(internalFile);
			in = new BufferedReader(new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String eingabe = null;
			while ((eingabe = in.readLine()) != null) {
				buffer.append(eingabe);
				buffer.append("\n");
			}
			return buffer.toString();
		} catch (Throwable exc) {
			exc.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException exc) {
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException exc) {
			}
		}
		return "";
	}

	static String getResourceString(String key) {
		try {
			return Internationalitaet.rb.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}

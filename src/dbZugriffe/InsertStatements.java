package dbZugriffe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controlls.LoadAndSaveCode;
import modell.Territorium;
import javafx.scene.control.TextArea;

public class InsertStatements {

	static String tagHinzufuegen = "INSERT INTO TagNamen (name) values(?)";
	static String terriHinzufuegen = "INSERT INTO TerritoriumDaten (name, territorium, quelltext) values(?, ?, ?)";
	static String zwischenHinzufuegen = "INSERT INTO ZwischenTabelleTerritoriumDatenTagNamen (fk_TerritoriumDaten, fk_TagNamen) values(?, ?)";

	public static void insertTag(String[] tags, Territorium territorium, TextArea textArea, String name) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return;
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (PreparedStatement stmtTerri = conn.prepareStatement(terriHinzufuegen,
				PreparedStatement.RETURN_GENERATED_KEYS);) {
			conn.setAutoCommit(false);
			stmtTerri.setString(1, name);
			stmtTerri.setString(2, LoadAndSaveCode.toXmlMarshal(territorium));
			stmtTerri.setString(3, textArea.getText());
			stmtTerri.executeUpdate();
			ResultSet resultSet = stmtTerri.getGeneratedKeys();
			int terriKey = 0;
			if (resultSet.next()) {
				terriKey = resultSet.getInt(1);
			}
			stmtTerri.close();
			for (String tag : tags) {
				if (!tag.trim().equals("")) {
					try (PreparedStatement stmtTag = conn.prepareStatement(tagHinzufuegen,
							PreparedStatement.RETURN_GENERATED_KEYS);
							PreparedStatement stmtZwischen = conn.prepareStatement(zwischenHinzufuegen,
									PreparedStatement.RETURN_GENERATED_KEYS);) {
						int idTag = 0;
						int i;
						if ((i = SelectStatements.getIDFromTag(tag)) != -1) {
							idTag = i;
						} else {
							stmtTag.setString(1, tag);
							stmtTag.executeUpdate();
							ResultSet resultSet2 = stmtTag.getGeneratedKeys();
							if (resultSet2.next()) {
								idTag = resultSet2.getInt(1);
							}
						}
						stmtTag.close();
						stmtZwischen.setInt(1, terriKey);
						stmtZwischen.setInt(2, idTag);
						stmtZwischen.executeUpdate();
						stmtZwischen.close();
					} catch (Exception e2) {
						e2.printStackTrace();
						try {
							conn.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
			conn.commit();
		} catch (Throwable th) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			th.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
//					DriverManager.getConnection("jdbc:derby:;shutdown=true");
					conn.close();
				}
			} catch (SQLException e) {
				if (e.getSQLState().equals("XJ015")) {
					// alles tuttie
				} else {
					e.printStackTrace();
				}
			}
		}
	}
}

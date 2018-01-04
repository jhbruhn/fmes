package dbZugriffe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controlls.LoadAndSaveCode;
import modell.Territorium;

public class SelectStatements {

	private static String Tag = "Select * From TagNamen";
	private static String highestTagID = "Select max(ID) From TagNamen";
	private static String highestcreateTerritoriumDatenTableID = "Select max(ID) From createTerritoriumDatenTable";
	private static String highestZwischenTabelleTerritoriumDatenTagID = "Select max(ID) From ZwischenTabelleTerritoriumDatenTagNamen";
	private static String getTagID = "Select ID From TagNamen Where name LIKE ?";
	private static String getTerriByID = "Select territorium, quelltext From TerritoriumDaten Where ID = ?";
	private static String getZwischenIDs = "Select fk_TerritoriumDaten From ZwischenTabelleTerritoriumDatenTagNamen Where fk_TagNamen = ?";
	private static String getNamenTerri = "Select ID, name From TerritoriumDaten Where ID = ?";
	private static String getAllTerri = "Select ID, name From TerritoriumDaten";
	private static String getAllZwischenDaten = "Select * from ZwischenTabelleTerritoriumDatenTagNamen";

	public static int highestTagID() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return -1;
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				Statement stmt = conn.createStatement();) {

			ResultSet rs = stmt.executeQuery(highestTagID);
			while (rs.next()) {
				return rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int getIDFromTag(String name) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return -1;
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				PreparedStatement stmt = conn.prepareStatement(getTagID);) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int getMaxIDFromTerritoriumDaten() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return -1;
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				Statement stmt = conn.createStatement();) {

			ResultSet rs = stmt.executeQuery(highestcreateTerritoriumDatenTableID);
			while (rs.next()) {
				return rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int getMaxIDFromZwischenDaten() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return -1;
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				Statement stmt = conn.createStatement();) {

			ResultSet rs = stmt.executeQuery(highestZwischenTabelleTerritoriumDatenTagID);
			while (rs.next()) {
				return rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static ArrayList<String> getTAllTags() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return null;
		}
		ArrayList<String> arrayTag = new ArrayList<String>();
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				Statement stmtTags = conn.createStatement();) {
			ResultSet rs = stmtTags.executeQuery(Tag);
			while (rs.next()) {
				arrayTag.add(rs.getString("ID") + " " + rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrayTag;
	}

	public static ArrayList<String> getTerriDaten(ArrayList<String> tags) {
		ArrayList<String> arrayName = new ArrayList<String>();
		for (String tag : tags) {
			int idTag = getIDFromTag(tag);
			if (idTag != -1) {
				try {
					Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
				} catch (Throwable th) {
					th.printStackTrace();
					return null;
				}
				try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
						PreparedStatement stmtZwischen = conn.prepareStatement(getZwischenIDs);) {
					stmtZwischen.setInt(1, idTag);
					ResultSet rs = stmtZwischen.executeQuery();
					while (rs.next()) {
						try(PreparedStatement stmtNamen = conn.prepareStatement(getNamenTerri);){
						stmtNamen.setInt(1, rs.getInt("fk_TerritoriumDaten"));
						ResultSet rs2 = stmtNamen.executeQuery();
						while (rs2.next()) {
							arrayName.add(rs2.getString("ID") + " " + rs2.getString("name"));
						}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return arrayName;
	}

	public static String getDatenFromTerri(int id) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
			return null;
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				PreparedStatement stmt = conn.prepareStatement(getTerriByID);) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getString("territorium") + "||<>" + rs.getString("quelltext");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void gibAlleAus(){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				Statement stmt = conn.createStatement();
				Statement stmt2 = conn.createStatement();) {
			ResultSet rs = stmt.executeQuery(getAllTerri);
			while (rs.next()) {
				System.out.println(rs.getString("ID") + " " + rs.getString("name"));
			}
			
			ResultSet rs2 = stmt2.executeQuery(getAllZwischenDaten);
			while (rs2.next()) {
				System.out.println(rs2.getString("ID") + " " + rs2.getString("fk_TerritoriumDaten") + " " + rs2.getString("fk_TagNamen"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getSpecialFinishTerritorium(int id, Territorium territorium) {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Throwable th) {
			th.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection("jdbc:derby:" + CreateDB.dbName + ";create=false");
				PreparedStatement stmt = conn.prepareStatement(getTerriByID);) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println("kekse");
				LoadAndSaveCode.fromXmlMarshal(territorium, rs.getString(1));
			} else {
				territorium.setLevelGeschaft(true);
				territorium.aufAnfang();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

package dbZugriffe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controlls.LoadAndSaveCode;
import modell.Territorium;

public class CreateDB {
	public static final String dbName = "submarineDB";
	private static String createTagNamenTable = "CREATE TABLE TagNamen (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, name VARCHAR(64))";
	private static String createTerritoriumDatenTable = "CREATE TABLE TerritoriumDaten (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, name VARCHAR(64), territorium LONG VARCHAR, quelltext LONG VARCHAR)";
	private static String createZwischenTabelle = "CREATE TABLE ZwischenTabelleTerritoriumDatenTagNamen (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, fk_TerritoriumDaten INTEGER NOT NULL REFERENCES TerritoriumDaten(ID), fk_TagNamen INTEGER NOT NULL REFERENCES TagNamen(ID))";
	private static String exampleTagNamen = "INSERT INTO TagNamen (name) values('Example')";
	private static String exampleTerriDaten = "INSERT INTO TerritoriumDaten (name, territorium, quelltext)  values('" + LoadAndSaveCode.defaultName + "', '" + LoadAndSaveCode.toXmlMarshal(new Territorium()) + "', '" + "void main(){ \n \n}" +"')";
	private static String exampleZwischenDaten = "INSERT INTO ZwischenTabelleTerritoriumDatenTagNamen (fk_TerritoriumDaten, fk_TagNamen) values(?, ?)";
	//PRIMARY KEY GENERATED ALWAYS AS IDENTITY

	public boolean checkAndCreateDB(){
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:" + dbName + ";create=true");
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			stmt.execute(createTagNamenTable);
			stmt.execute(createTerritoriumDatenTable);
			stmt.execute(createZwischenTabelle);
			stmt.close();
			
			stmt2 = conn.prepareStatement(exampleTagNamen,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt2.execute();
			ResultSet resultSet = stmt2.getGeneratedKeys();
			int tagKey = 0;
			if (resultSet.next()) {
				tagKey = resultSet.getInt(1);
			}
			stmt2.close();
			
			stmt2 = conn.prepareStatement(exampleTerriDaten,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt2.execute();
			ResultSet resultSet2 = stmt2.getGeneratedKeys();
			int terriKey = 0;
			if (resultSet2.next()) {
				terriKey = resultSet2.getInt(1);
			}
			stmt2.close();
			
			stmt2 = conn.prepareStatement(exampleZwischenDaten,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt2.setInt(1, terriKey);
			stmt2.setInt(2, tagKey);
			stmt2.execute();
			stmt2.close();
			
			conn.commit();
//			stmt.executeUpdate(exampleZwischenDaten);
		} catch (Exception e) {
//			e.printStackTrace();
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			return false;
		}finally{
			try {
				if(stmt != null){
					stmt.close();
				}
				if(stmt2 != null){
					stmt2.close();
				}
				if(conn != null){
					conn.setAutoCommit(true);
//					DriverManager.getConnection("jdbc:derby:;shutdown=true");
					conn.close();
				}
			} catch (SQLException e) {
				if(e.getSQLState().equals("XJ015")){
					//alles tuttie
				}else{
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}

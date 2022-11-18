package osu.cse3241;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class People {
    public void addPeople(String db, int id) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "INSERT INTO People VALUES(" + id + ")";
        
        try {
        	conn = initializeDB(db);
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlStmt);
		} catch (SQLException e) {
			System.out.println("Error: unable to connect to database");
			System.exit(1);
		} finally {
			if(stmt != null) { stmt.close(); }
			if(conn != null) { conn.close(); }
		}
    }
    
    public void listPeople(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM People";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				int creator = rSet.getInt("creator_id");
				System.out.println("Creator ID: " + creator);
			}
		} catch (Exception e) {
			System.out.println("Error: unable to connect to database");
			System.exit(1);
		} finally {
			if(rSet != null) { rSet.close(); }
			if(stmt != null) { stmt.close(); }
			if(conn != null) { conn.close(); }
		}
    }
    
    public static Connection initializeDB(String databaseFileName) {
    	/**
    	 * The "Connection String" or "Connection URL".
    	 * 
    	 * "jdbc:sqlite:" is the "subprotocol".
    	 * (If this were a SQL Server database it would be "jdbc:sqlserver:".)
    	 */
        String url = "jdbc:sqlite:" + databaseFileName;
        Connection conn = null; // If you create this variable inside the Try block it will be out of scope
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
        }
        return conn;
    }
}

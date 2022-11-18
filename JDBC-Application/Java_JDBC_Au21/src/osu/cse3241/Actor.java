package osu.cse3241;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Actor {

    public void addActor(String db, People people) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the actor name you want to add: ");
        String actorName = sc.nextLine();
        int creatorID = getSize(db, "People") + 1;
        
        String sqlStmt = "INSERT INTO Actor VALUES(" + "'" + actorName + "'" + ", " + creatorID + ")";
        
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
        
        people.addPeople(db, creatorID);
    }
    
    public void getActor(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the actor name you want to search: ");
        String actorName = sc.nextLine();
        
        String sqlStmt = "SELECT name, creator FROM Actor WHERE name = '" + actorName + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				String creator = rSet.getString("creator");
				System.out.println("Actor: " + name + " Creator ID: " + creator);
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
    
    public void addRole(String db, Director director) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.println("Is the actor in any movies? (y/n)");
    	String option = sc.nextLine();
    	
    	if (option.equals("y")) {
            System.out.print("Enter the actor name you want to add: ");
            String actorName = sc.nextLine();
            director.listMovies(db);
            System.out.print("Enter the movie content id: ");
            int contentID = sc.nextInt();
            System.out.print("Enter the role of the actor: ");
            String role = sc.nextLine();
            
            String sqlStmt = "INSERT INTO Hasrolein VALUES(" + "'" + actorName + "'" + ", " + contentID + ", " + "'" + role + "'" + ")";
            
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
    }
    
    public int getSize(String db, String table) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
    	int size = 0;
    	String sqlStmt = "SELECT count(*) FROM " + table;
    	
    	try {
    		conn = initializeDB(db);
    		stmt = conn.prepareStatement(sqlStmt);
    		rSet = stmt.executeQuery();
    		while(rSet.next()) {
    			size = rSet.getInt(1);
    		}
    	} catch (SQLException e) {
    		System.out.println("Error: unable to connect to database");
			System.exit(1);
    	} finally {
    		
    	}
    	return size;
    }
    
    public void listActors(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Actor";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				int creator = rSet.getInt("creator");
				System.out.println("Actor: " + name + " | " + "Creator ID: " + creator);
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
    
    public void editActor(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listActors(db);
    	System.out.println("Enter the creator id of the actor you want to edit");
    	int id = sc.nextInt();
    	System.out.println("Enter the new name of the actor: ");
    	String name = sc.nextLine();
    	
    	String sqlStmt = "UPDATE Actor SET name = " + "'" + name + "'" + " WHERE creator = " + id;
    	
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

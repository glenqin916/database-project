package osu.cse3241;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.Scanner;

public class Director {
	
    public void addMovie(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	int contentID = 0;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the movie you want to add: ");
        String movieName = sc.nextLine();
        String y = Year.now().toString();
        System.out.print("Enter the length (HH:MM:SS): ");
        String length = sc.nextLine();
        System.out.print("Enter the genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter the content rating: ");
        String rating = sc.nextLine();
        System.out.print("Enter the producer: ");
        String producer = sc.nextLine();
        System.out.print("Enter the director: ");
        String director = sc.nextLine();
        contentID = getSize(db, "Content");
        
        String sqlStmt = "INSERT INTO Movie1 VALUES(" + "'" + movieName + "'" + ", " + Integer.parseInt(y) + ", " +  "'" + length + "'" + ", " + "'" + genre + "'" + ", " + "'" + rating + "'" + "'" + producer + "'"+ "'" + director + "'"+ contentID + ")";
        System.out.println(sqlStmt);
        
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
        
        content.addContent(db, "movie", contentID);
    }

    public void addDirector(String db, People people) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the name you want to add: ");
        String directorName = sc.nextLine();
        int creatorID = getSize(db, "People") + 1;
        
        String sqlStmt = "INSERT INTO Director VALUES(" + "'" + directorName + "'" + ", " + creatorID + ")";
        
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
    
    public void getDirector(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the director name you want to search: ");
        String directorName = sc.nextLine();
        
        String sqlStmt = "SELECT name, creator FROM Director WHERE name = '" + directorName + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				String creator = rSet.getString("creator");
				System.out.println("Director: " + name + " Creator ID: " + creator);
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
    
    public void getMovie(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the movie name you want to search: ");
        String movieName = sc.nextLine();
        
        String sqlStmt = "SELECT title, year, length, genre, content_rating, producer, director, content FROM Movie1 WHERE title = '" + movieName;
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String title = rSet.getString("title");
				String year = rSet.getString("year");
				String length = rSet.getString("length");
				String genre = rSet.getString("genre");
				String rating = rSet.getString("rating");
				String producer = rSet.getString("producer");
				String director = rSet.getString("director");
				String content = rSet.getString("content");
				System.out.println("Title: " + title + " Year: " + year + " Length: " + length + " Genre: " + genre + " Rating: " + rating + " Producer: " + producer + " Director: " + director + " Content ID: " + content);
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
    
    public void listDirectors(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Director";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				int creator = rSet.getInt("creator");
				System.out.println("Director: " + name + " | " + "Creator ID: " + creator);
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
    
    public void listMovies(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Movie1";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String title = rSet.getString("title");
				int year = rSet.getInt("year");
				String length = rSet.getString("length");
				String genre = rSet.getString("genre");
				String rating = rSet.getString("content_rating");
				String producer = rSet.getString("producer");
				String director = rSet.getString("director");
				int content = rSet.getInt("content");
				System.out.println("Title: " + title + " | " + "Year: " + year + " | " + "Length: " + length + " | " + "Genre: " + genre + " | " + "Content Rating: " + rating + " | " + "Producer: " + producer + " | " + "Director: " + director + " | " + "Content ID: " + content);
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
    
    public void editDirector(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listDirectors(db);
    	System.out.println("Enter the creator id of the director you want to edit");
    	int id = sc.nextInt();
    	System.out.println("Enter the new name of the director: ");
    	String name = sc.nextLine();
    	
    	String sqlStmt = "UPDATE Director SET name = " + "'" + name + "'" + " WHERE creator = " + id;
    	
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
    
    public void editMovie(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listMovies(db);
    	System.out.println("Enter the content id of the movie you want to edit");
    	int id = sc.nextInt();
        System.out.print("Enter the movie you want to edit: ");
        String movieName = sc.nextLine();
        System.out.print("Enter the length (HH:MM:SS): ");
        String length = sc.nextLine();
        System.out.print("Enter the genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter the content rating: ");
        String rating = sc.nextLine();
        System.out.print("Enter the producer: ");
        String producer = sc.nextLine();
        System.out.print("Enter the director: ");
        String director = sc.nextLine();
    	
    	String sqlStmt = "UPDATE Movie1 SET title = " + "'" + movieName + "'" + ", " + "length = " + "'" + length + "'" + ", " + "genre = " + "'" + genre + "'" + ", " + "content_rating = " + "'" + rating + "'" + ", " + "producer = " + "'" + producer + "'" + ", " + "director = " + "'" + director + "'" + " WHERE content = " + id;
    	
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
    
    public void deleteMovie(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listMovies(db);
    	System.out.println("Enter the content id of the movie you want to delete");
    	int id = sc.nextInt();
    	
    	String sqlStmt = "DELETE FROM Movie1 WHERE content = " + id;
    	
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
        
        content.deleteContent(db, id);
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

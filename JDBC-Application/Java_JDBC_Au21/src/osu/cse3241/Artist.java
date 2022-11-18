package osu.cse3241;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;

public class Artist {
	
	String artistName;

    public void addArtist(String db, People people) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the name you want to add: ");
        artistName = sc.nextLine();
        int creatorID = getSize(db, "People") + 1;
        
        String sqlStmt = "INSERT INTO Artist VALUES(" + "'" + artistName + "'" + ", " + creatorID + ")";
        
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
    
    public void addAlbum(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	int contentID = 0;
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.print("Enter the artist who made the album: ");
        artistName = sc.nextLine();
        System.out.print("Enter the album you want to add: ");
        String albumName = sc.nextLine();
        String y = Year.now().toString();
        System.out.print("Enter the genre: ");
        String genre = sc.nextLine();
        contentID = getSize(db, "Content");
        
        String sqlStmt1 = "INSERT INTO ALBUM1 VALUES(" + "'" + albumName + "'" + ", " + Integer.parseInt(y) + ", " + "'" + genre + "'" + ", " + contentID + ")";
        String sqlStmt2 = "INSERT INTO Album2 VALUES(" + "'" + artistName + "'" + "," + contentID + ")";
        
        try {
        	conn = initializeDB(db);
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlStmt1);
			stmt.executeUpdate(sqlStmt2);
		} catch (SQLException e) {
			System.out.println("Error: unable to connect to database");
			System.exit(1);
		} finally {
			if(stmt != null) { stmt.close(); }
			if(conn != null) { conn.close(); }
		}
        
        content.addContent(db, "album", contentID);
    }
    
    public void addTrack(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	int albumID = 0;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the track you want to add: ");
        String trackName = sc.nextLine();
        System.out.print("Enter the length (HH:MM:SS): ");
        String trackLength = sc.nextLine();
        System.out.print("Enter the album id: ");
        albumID = getSize(db, "Content");
        
        String sqlStmt = "INSERT INTO Track VALUES(" + "'" + trackName + "'" + ", " + "'" + trackLength + "'" + "," + albumID + ")";
        
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
        
        content.addContent(db, "track", albumID);
    }
    
    public void getArtist(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the artist name you want to search: ");
        String artistName = sc.nextLine();
        
        String sqlStmt = "SELECT name, creator FROM Artist WHERE name = '" + artistName + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				String creator = rSet.getString("creator");
				System.out.println("Artist: " + name + " Creator ID: " + creator);
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
    
    public void getAlbum(String db) throws SQLException {
    	Connection conn = null;
    	PreparedStatement stmt1 = null;
    	ResultSet rSet1 = null;
    	PreparedStatement stmt2 = null;
    	ResultSet rSet2 = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the content id you want to search: ");
        int albumID = sc.nextInt();
        
        String sqlStmt1 = "SELECT album_name, year, genre, content FROM Album1 WHERE content = " + albumID;
        String sqlStmt2 = "SELECT artist, content FROM Album2 WHERE content = " + albumID;
        
		try {
			conn = initializeDB(db);
			stmt1 = conn.prepareStatement(sqlStmt1);
			rSet1 = stmt1.executeQuery();
			while (rSet1.next()) {
				String albumName = rSet1.getString("album_name");
				String year = rSet1.getString("year");
				String genre = rSet1.getString("genre");
				String content = rSet1.getString("content");
				System.out.println("Album: " + albumName + " Year: " + year + " Genre: " + genre + " Content ID: " + content);
			}
			
			stmt2 = conn.prepareStatement(sqlStmt2);
			rSet2 = stmt2.executeQuery();
			while (rSet2.next()) {
				String artistName = rSet2.getString("artist");
				String content = rSet2.getString("content");
				System.out.println("Artist: " + artistName + " Content ID: " + content);
			}
		} catch (Exception e) {
			System.out.println("Error: unable to connect to database");
			System.exit(1);
		} finally {
			if(rSet1 != null) { rSet1.close(); }
			if(stmt1 != null) { stmt1.close(); }
			if(rSet2 != null) { rSet1.close(); }
			if(stmt2 != null) { stmt1.close(); }
			if(conn != null) { conn.close(); }
		}
    }
    
    public void getTrack(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the track name you want to search: ");
        String trackName = sc.nextLine();
        
        String sqlStmt = "SELECT track_name, length, album FROM Track WHERE track_name = " + trackName;
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String track = rSet.getString("track_name");
				String length = rSet.getString("length");
				String contentID = rSet.getString("album");
				System.out.println("Track: " + track + " Length: " + length + " Content ID: " + contentID);
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
    
    public void listArtists(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Artist";
        
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
    
    public void listAlbums(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Album1";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String album = rSet.getString("album_name");
				int year = rSet.getInt("year");
				String genre = rSet.getString("genre");
				int content = rSet.getInt("content");
				System.out.println("Album: " + album + " | " + "Year: " + year + " | " + "Genre: " + genre + " | " + "Content ID: " + content);
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
    
    public void listTracks(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Book1";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String title = rSet.getString("book_title");
				String genre = rSet.getString("genre");
				String publisher = rSet.getString("publisher");
				int year = rSet.getInt("year");
				int chapters = rSet.getInt("chapters");
				int pages = rSet.getInt("pages");
				int content = rSet.getInt("content");
				System.out.println("Title: " + title + " | " + "Genre: " + genre + " | " + "Publisher: " + publisher + " | " + "Year: " + year + " | " + "Chapters: " + chapters + " | " + "Pages: " + pages + " | " + "Content ID: " + content);
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
    
    public void editArtist(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listArtists(db);
    	System.out.println("Enter the creator id of the artist you want to edit");
    	int id = sc.nextInt();
    	System.out.println("Enter the new name of the artist: ");
    	String name = sc.nextLine();
    	
    	String sqlStmt = "UPDATE Artist SET name = " + "'" + name + "'" + " WHERE creator = " + id;
    	
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
    
    public void editAlbum(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listAlbums(db);
    	System.out.println("Enter the content id of the album you want to edit");
    	int id = sc.nextInt();
    	System.out.print("Enter the artist who made the album: ");
        artistName = sc.nextLine();
        System.out.print("Enter the album name: ");
        String albumName = sc.nextLine();
        System.out.print("Enter the genre: ");
        String genre = sc.nextLine();
    	
        String sqlStmt1 = "UPDATE Album1 SET album_name = " + "'" + albumName + "'" + ", " + "genre = " + "'" + genre + "'" + " WHERE content = " + id;
        String sqlStmt2 = "UPDATE Album2 SET artist = " + "'" + artistName + "'" + " WHERE content = " + id;
        
        try {
        	conn = initializeDB(db);
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlStmt1);
			stmt.executeUpdate(sqlStmt2);
		} catch (SQLException e) {
			System.out.println("Error: unable to connect to database");
			System.exit(1);
		} finally {
			if(stmt != null) { stmt.close(); }
			if(conn != null) { conn.close(); }
		}
    }
    
    public void editTrack(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listTracks(db);
    	System.out.println("Enter the content id of the track you want to edit");
    	int id = sc.nextInt();
    	System.out.print("Enter the artist who made the album: ");
        artistName = sc.nextLine();
        System.out.print("Enter the track name: ");
        String trackName = sc.nextLine();
        System.out.print("Enter the track length: ");
        String length = sc.nextLine();
    	
        String sqlStmt = "UPDATE Track SET track_name = " + "'" + trackName + "'" + ", " + "length = " + "'" + length + "'" + " WHERE album = " + id;
        
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
    
    public void deleteAlbum(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listAlbums(db);
    	System.out.println("Enter the content id of the album you want to delete");
    	int id = sc.nextInt();
    	
    	String sqlStmt = "DELETE FROM Album1, Album2 WHERE Album1.content = " + id + " AND Album2.content = " + id;
    	
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
    
    public void deleteTrack(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listTracks(db);
    	System.out.println("Enter the content id of the track you want to delete");
    	int id = sc.nextInt();
    	
    	String sqlStmt = "DELETE FROM Track WHERE album = " + id;
    	
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
//                System.out.println("The driver name is " + meta.getDriverName());
//                System.out.println("The connection to the database was successful.");
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

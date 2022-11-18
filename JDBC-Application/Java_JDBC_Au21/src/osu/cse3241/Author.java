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

public class Author {
    public void addAuthor(String db, People people) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the name you want to add: ");
        String authorName = sc.nextLine();
        int creatorID = getSize(db, "People") + 1;
        
        String sqlStmt = "INSERT INTO Author VALUES(" + "'" + authorName + "'" + ", " + creatorID + ")";
        
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
    
    public void addBook(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	int contentID = 0;
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.print("Enter the author who wrote the book: ");
        String authorName = sc.nextLine();
        System.out.print("Enter the book you want to add: ");
        String bookName = sc.nextLine();
        System.out.print("Enter the genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter the publisher: ");
        String publisher = sc.nextLine();
        String y = Year.now().toString();
        System.out.print("Enter the number of chapters: ");
        int chapters = sc.nextInt();
        System.out.print("Enter the number of pages: ");
        int pages = sc.nextInt();
        contentID = getSize(db, "Content") + 1;
        
        String sqlStmt1 = "INSERT INTO Book1 VALUES(" + "'" + bookName + "'" + ", " + "'" + genre + "'" + ", " + "'" + publisher + "'" + ", " + Integer.parseInt(y) + ", " + chapters + ", " + pages + ", " + contentID + ")";
        String sqlStmt2 = "INSERT INTO Book2 VALUES(" + "'" + authorName + "'" + "," + contentID + ")";
        System.out.println(sqlStmt1);
        System.out.println(sqlStmt2);
        
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
        content.addContent(db, "book", contentID);
    }
    
    public void getAuthor(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the author name you want to search: ");
        String author = sc.nextLine();
        
        String sqlStmt = "SELECT name, creator FROM Author WHERE name = '" + author + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				String creator = rSet.getString("creator");
				System.out.println("Author: " + name + " Creator ID: " + creator);
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
    
    public void getBook(String db) throws SQLException {
    	Connection conn = null;
    	PreparedStatement stmt1 = null;
    	ResultSet rSet1 = null;
    	PreparedStatement stmt2 = null;
    	ResultSet rSet2 = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter the book title you want to search: ");
        String title = sc.nextLine();
        System.out.print("Enter the content id of the book: ");
        int bookID = sc.nextInt();
        
        String sqlStmt1 = "SELECT book_title, genre, publisher, year, chapters, pages, content FROM Book1 WHERE book_title = " + title;
        String sqlStmt2 = "SELECT author, content FROM Book2 WHERE content = " + bookID;
        
		try {
			conn = initializeDB(db);
			stmt1 = conn.prepareStatement(sqlStmt1);
			rSet1 = stmt1.executeQuery();
			while (rSet1.next()) {
				String bookName = rSet1.getString("book_title");
				String genre = rSet1.getString("genre");
				String publisher = rSet1.getString("publisher");
				String year = rSet1.getString("year");
				String chapters = rSet1.getString("chapters");
				String pages = rSet1.getString("pages");
				String content = rSet1.getString("content");
				System.out.println("Book: " + bookName + " Genre:" + genre + " Publisher: " + publisher + " Year: " + year + " Chapters: " + chapters + " Pages: " + pages + " Content ID: " + content);
			}
			
			stmt2 = conn.prepareStatement(sqlStmt2);
			rSet2 = stmt2.executeQuery();
			while (rSet2.next()) {
				String authorName = rSet2.getString("author");
				String content = rSet2.getString("content");
				System.out.println("Author: " + authorName + " Content ID: " + content);
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
    
    public void listAuthors(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Author";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String name = rSet.getString("name");
				int creator = rSet.getInt("creator");
				System.out.println("Author: " + name + " | " + "Creator ID: " + creator);
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
    
    public void listBooks(String db) throws SQLException {
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
    
    public void editAuthor(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listAuthors(db);
    	System.out.println("Enter the creator id of the author you want to edit");
    	int id = sc.nextInt();
    	System.out.println("Enter the new name of the author: ");
    	String name = sc.nextLine();
    	
    	String sqlStmt = "UPDATE Author SET name = " + "'" + name + "'" + " WHERE creator = " + id;
    	
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
    
    public void editBook(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listBooks(db);
    	System.out.println("Enter the content id of the book you want to edit");
    	int id = sc.nextInt();
        System.out.print("Enter the book you want to edit: ");
        String bookName = sc.nextLine();
        System.out.print("Enter the genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter the publisher: ");
        String publisher = sc.nextLine();
        System.out.print("Enter the number of chapters: ");
        String chapters = sc.nextLine();
        System.out.print("Enter the number of pages: ");
        String pages = sc.nextLine();
        System.out.print("Enter the author of the book: ");
        String author = sc.nextLine();
    	
    	String sqlStmt1 = "UPDATE Book1 SET book_title = " + "'" + bookName + "'" + ", " + "genre = " + "'" + genre + "'" + ", " + "publisher = " + "'" + publisher + "'" + ", " + "chapters = " + "'" + chapters + "'" + ", " + "pages = " + "'" + pages + "'" + " WHERE content = " + id;
    	String sqlStmt2 = "UPDATE Book2 SET author = " + "'" + author + "'" + " WHERE content = " + id;
    	
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
    
    public void deleteBook(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listBooks(db);
    	System.out.println("Enter the content id of the book you want to delete");
    	int id = sc.nextInt();
    	
    	String sqlStmt = "DELETE FROM Book1, Book2 WHERE Book1.content = " + id + " AND Book2.content = " + id;
    	
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

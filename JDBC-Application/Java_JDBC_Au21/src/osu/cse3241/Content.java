package osu.cse3241;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class Content {
    public void addContent(String db, String type, int id) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the location of the item: ");
        String location = sc.nextLine();
        System.out.print("Enter the number of copies: ");
        int numCopies = sc.nextInt();
        System.out.print("Enter the library the item is being held at: ");
        String library = sc.nextLine();
        
        String sqlStmt = "INSERT INTO Content VALUES(" + id + ", " + "'" + location + "'" + ", " + numCopies + ", " + "'" + type + "'" + ", " + "'" + library + "'" + ")";
        
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
    
    public int copiesLeft(String db, int id) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
    	int copies = 0;
    	
    	String sqlStmt = "SELECT num_copies FROM Content WHERE content_id = " + id;
    	
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				copies = rSet.getInt("num_copies");
			}
		} catch (Exception e) {
			System.out.println("Error: unable to connect to database");
			System.exit(1);
		} finally {
			if(rSet != null) { rSet.close(); }
			if(stmt != null) { stmt.close(); }
			if(conn != null) { conn.close(); }
		}
    	
    	return copies;
    }
    
    public void updateNumCopies(String db, int copies, int id, int option) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	if (option == 1) {
    		//checkout
    		copies--;
    	} else {
    		//return
    		copies++;
    	}
        
        String sqlStmt = "UPDATE Content SET num_copies = " + copies + " WHERE content_id = " + id;
        
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
    
    public void listContent(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Content";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				int id = rSet.getInt("content_id");
				String location = rSet.getString("location");
				String numCopies = rSet.getString("num_copies");
				String type = rSet.getString("content_type");
				String library = rSet.getString("library");
				System.out.println("Content ID: " + id + " | " + "Location: " + location + " | " + "Copies: " + numCopies + " | " + "Content Type: " + type + " | " + "Library: " + library);
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
    
    public void editContent(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listContent(db);
    	System.out.print("Enter the content id of the item you would like to edit: ");
    	int id = sc.nextInt();
    	System.out.print("Enter the location of the content: ");
    	String location = sc.nextLine();
    	System.out.print("Enter the number of copies: ");
    	int numCopies = sc.nextInt();
    	System.out.print("Enter the content type (book, movie, album, track): ");
    	String type = sc.nextLine();
    	System.out.print("Enter the library address: ");
    	String library = sc.nextLine();
    	
    	String sqlStmt = "UPDATE Content SET location = " + "'" + location + "'" + ", " + "num_copies = " + numCopies + ", " + "content_type = " + "'" + type + "'" + ", " + "library = " + "'" + library + "'";
    	
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
    
    public void deleteContent(String db, int id) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	String sqlStmt = "DELETE FROM Content WHERE content_id = " + id;
    	
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
    
    public void addOrder(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	int orderID = getSize(db, "Orders") + 1;
    	System.out.println("Enter the price of the item: ");
    	String price = sc.nextLine();
    	System.out.println("Enter the arrival date of the item (YYYY-MM-DD): ");
    	String arrivalDate = sc.nextLine();
    	System.out.println("Enter the type of the item (book , movie, album, track): ");
    	String type = sc.nextLine();
    	int contentID = getSize(db, "Content");
    	System.out.println("Enter the destination library address of the item: ");
    	String library = sc.nextLine();
    	
    	String sqlStmt1 = "INSERT INTO Orders VALUES(" + orderID + ", " + "'" +  price + "'" + ", " + "'" + arrivalDate + "'" + ", " + "'" + type + "'" + ", " + contentID + ", " + "'" + library + "'" + ")";
    	String sqlStmt2 = "INSERT INTO Contains VALUES(" + orderID + ", " + contentID + ")";
    	
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
    
    public void transferOrder(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	listOrder(db);
    	System.out.println("Enter the order id of the order that has been delivered: ");
    	int orderID = sc.nextInt();
    	System.out.println("Enter the type of the order that has been delivered: ");
    	String type = sc.nextLine();
    	System.out.println("Enter the content id of the order that has been delivered: ");
    	int contentID = sc.nextInt();
    	
    	String sqlStmt1 = "DELETE FROM Orders WHERE order_id = " + orderID;
    	String sqlStmt2 = "DELETE FROM Contains WHERE order_id = " + orderID;
    	
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
        
        content.addContent(db, type, contentID);
    }
    
    public void listOrder(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Orders";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				int oid = rSet.getInt("order_id");
				String price = rSet.getString("price");
				String arrivalDate = rSet.getString("arrival_date");
				String type = rSet.getString("type");
				int cid = rSet.getInt("cid");
				String library = rSet.getString("library");
				System.out.println("Order ID: " + oid + " | " + "Price: " + price + " | " + "Arrival Date: " + arrivalDate + " | " + "Content Type: " + type + " | " + "Content ID: " + cid + " | " + "Library: " + library);
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

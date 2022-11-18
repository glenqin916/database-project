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

public class Patron {
	
    public void addPatron(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter your first name: ");
        String fName = sc.nextLine();
        System.out.print("Enter your last name: ");
        String lName = sc.nextLine();
        System.out.print("Enter your address: ");
        String address = sc.nextLine();
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        System.out.print("Enter the library address you are registering the card with: ");
        String library = sc.nextLine();
        
        String sqlStmt = "INSERT INTO Patron VALUES(" + "'" + fName + "'" + ", " + "'" + lName + "'" + " ," + "true" + " ," + "'" + address + "'" + " ," + "'" + email + "'" + " ," + "'" + library + "')";
        
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
    
    public void expiredCard(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        
        String sqlStmt = "UPDATE Patron SET library_card = false WHERE email = " + "'" + email + "'";
        
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
    
    public void checkout(String db, Author author, Artist artist, Director director, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	int id = 0;
    	int size = 0;
    	int copies = 0;
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.println("What would you like to checkout?");
    	System.out.println("1. Book\n2. Album\n3. Movie\n4. Back");
    	int option = sc.nextInt();
    	
    	switch(option) {
    		case 1: 
    			//book
    			author.listBooks(db);
    			System.out.println("Enter the book id you would like to check out: ");
    			id = sc.nextInt();
    			break;
    		case 2:
    			//album
    			artist.listAlbums(db);
    			System.out.println("Enter the album id you would like to check out: ");
    			id = sc.nextInt();
    			break;
    		case 3:
    			//movie
    			director.listMovies(db);
    			System.out.println("Enter the movie id you would like to check out: ");
    			id = sc.nextInt();
    			break;
    		default:
    			break;
    	}
    	
    	copies = content.copiesLeft(db, id);
    	
    	if (copies > 0) {
        	//nextInt() does not consume \n token
        	//needed here so that the \n token is consumed and won't affect next call of nextLine()
        	sc.nextLine();
       
        	size = getSize(db, "Checkouts") + 1;
        	System.out.print("Enter your name: ");
        	String name = sc.nextLine();
        	
        	String sqlStmt = "INSERT INTO Checkouts VALUES(" + "'" + LocalDate.now().plusDays(14) + "'" + ", " + size + ", " + "NULL" + ", " + id + ", " + "'" + name + "')";
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
            
            //1 is to determine if number of copies available should go up or down (should go down)
            content.updateNumCopies(db, copies, id, 1);
    	} else {
    		System.out.println("Current item is out of stock");
    	}
    }
    
    public void returnItem(String db, Content content) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	int copies = 0;
    	Scanner sc = new Scanner(System.in);
    	
        System.out.print("Enter the content id of the item being returned: ");
        int id = sc.nextInt();
        
        String sqlStmt = "UPDATE Checkouts SET return_date = '" + LocalDate.now() + "' WHERE cid = " + id;
        
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
        
        //add item back to inventory
        copies = content.copiesLeft(db, id);
        //0 is to determine if number of copies available should go up or down (should go up)
        content.updateNumCopies(db, copies, id, 0);
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
    
    public void listCheckouts(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        
        String sqlStmt = "SELECT * FROM Checkouts, Patron WHERE Checkouts.patron = '" + email + "' AND Patron.email = '" + email + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String dueDate = rSet.getString("due_date");
				String transaction = rSet.getString("transaction_id");
				String returnDate = rSet.getString("return_date");
				int cid = rSet.getInt("cid");
				String patron = rSet.getString("patron");
				System.out.println("Due Date: " + dueDate + " | " + "Transaction ID: " + transaction + " | " + "Return Date: " + returnDate + " | " + "Content ID: " + cid + " | " + "Patron: " + patron);
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
    
    public void listPatrons(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        String sqlStmt = "SELECT * FROM Patron";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String fName = rSet.getString("fName");
				String lName = rSet.getString("lName");
				String hasCard = rSet.getString("library_card");
				String address = rSet.getString("address");
				String email = rSet.getString("email");
				String library = rSet.getString("library");
				System.out.println("First Name: " + fName + " | " + "Last Name: " + lName+ " | " + "Library Card: " + hasCard + " | " + "Address: " + address + " | " + "Email: " + email + " | " + "Library: " + library);
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
    
    public void editPatrons(String db) throws SQLException {
    	Statement stmt = null;
    	Connection conn = null;
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.println("Enter your email address: ");
    	String email = sc.nextLine();
        System.out.print("Enter your first name: ");
        String fName = sc.nextLine();
        System.out.print("Enter your last name: ");
        String lName = sc.nextLine();
        System.out.print("Enter your address: ");
        String address = sc.nextLine();
        
        String sqlStmt = "UPDATE Patron SET fname = " + "'" + fName + "'" + ", " + "lName = " + "'" + lName + "'" + ", " + "address = " + "'" + address + "'" + " WHERE email = " + "'" + email + "'";
        
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
    
    public void overdue(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        
        String sqlStmt = "SELECT * FROM Checkouts WHERE patron = " + "'" + email + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String dueDate = rSet.getString("due_date");
				LocalDate date = LocalDate.parse(dueDate);
				String transaction = rSet.getString("transaction_id");
				String returnDate = rSet.getString("return_date");
				int cid = rSet.getInt("cid");
				String patron = rSet.getString("patron");
				if (LocalDate.now().compareTo(date) > 0) {
					System.out.println("OVERDUE:");
					System.out.println("Due Date: " + dueDate + " | " + "Transaction ID: " + transaction + " | " + "Return Date: " + returnDate + " | " + "Content ID: " + cid + " | " + "Patron: " + patron);
				}
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
    
    public void listReturn(String db) throws SQLException {
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rSet = null;
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        
        String sqlStmt = "SELECT * FROM Checkouts WHERE patron = " + "'" + email + "'";
        
		try {
			conn = initializeDB(db);
			stmt = conn.prepareStatement(sqlStmt);
			rSet = stmt.executeQuery();
			while (rSet.next()) {
				String dueDate = rSet.getString("due_date");
				String transaction = rSet.getString("transaction_id");
				String returnDate = rSet.getString("return_date");
				int cid = rSet.getInt("cid");
				String patron = rSet.getString("patron");
				if (returnDate != null) {
					System.out.println("RETURNED:");
					System.out.println("Due Date: " + dueDate + " | " + "Transaction ID: " + transaction + " | " + "Return Date: " + returnDate + " | " + "Content ID: " + cid + " | " + "Patron: " + patron);
				}
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

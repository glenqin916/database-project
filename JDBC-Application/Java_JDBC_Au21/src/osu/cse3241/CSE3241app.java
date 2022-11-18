package osu.cse3241;

import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

/**
 * <h1>CSE3241 Introduction to Database Systems - Sample Java application.</h1>
 * 
 * <p>Sample app to be used as guidance and a foundation for students of 
 * CSE3241 Introduction to Database Systems at 
 * The Ohio State University.</p>
 * 
 * <h2>!!! - Vulnerable to SQL injection - !!!</h2>
 * <p>Correct the code so that it is not 
 * vulnerable to a SQL injection attack. ("Parameter substitution" is the usual way to do this.)</p>
 * 
 * <p>Class is written in Java SE 8 and in a procedural style. Implement a constructor if you build this app out in OOP style.</p>
 * <p>Modify and extend this app as necessary for your project.</p>
 *
 * <h2>Language Documentation:</h2>
 * <ul>
 * <li><a href="https://docs.oracle.com/javase/8/docs/">Java SE 8</a></li>
 * <li><a href="https://docs.oracle.com/javase/8/docs/api/">Java SE 8 API</a></li>
 * <li><a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/">Java JDBC API</a></li>
 * <li><a href="https://www.sqlite.org/docs.html">SQLite</a></li>
 * <li><a href="http://www.sqlitetutorial.net/sqlite-java/">SQLite Java Tutorial</a></li>
 * </ul>
 *
 * <h2>MIT License</h2>
 *
 * <em>Copyright (c) 2019 Leon J. Madrid, Jeff Hachtel</em>
 * 
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.</p>
 *
 * 
 * @author Glen Qin (qin.444)
 * 
 */

public class CSE3241app {
    
	/**
	 *  The database file name.
	 *  
	 *  Make sure the database file is in the root folder of the project if you only provide the name and extension.
	 *  
	 *  Otherwise, you will need to provide an absolute path from your C: drive or a relative path from the folder this class is in.
	 */
	private static String DATABASE = "Library_Database.db";
	
	
	/**
	 *  The query statement to be executed.
	 *  
	 *  Remember to include the semicolon at the end of the statement string.
	 *  (Not all programming languages and/or packages require the semicolon (e.g., Python's SQLite3 library))
	 */
	private static String sqlStatement1 = "INSERT INTO Checkouts VALUES('2022-11-20', 4, '2021-11-21', 4, 'glenqingms@gmail.com')";
	private static String sqlStatement2 = "SELECT * FROM Checkouts WHERE patron = 'glenqingms@gmail.com'";
	private static String sqlStatement3 = "SELECT * FROM Author";
	//SELECT creator FROM Actor
	//INSERT INTO Artist VALUES(fdsa, 34)
	//SELECT * FROM People
	
	/**
	 * Database connection
	 */
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
    /**
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
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
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("The connection to the database was successful.");
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
    
    /**
     * Queries the database and prints the results.
     * 
     * @param conn a connection object
     * @param sql a SQL statement that returns rows
     * This query is written with the Statement class, tipically 
     * used for static SQL SELECT statements
     */
    public static void sqlQuery(Connection conn, String sql){
        try {
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(sql);
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
        	for (int i = 1; i <= columnCount; i++) {
        		String value = rsmd.getColumnName(i);
        		System.out.print(value);
        		if (i < columnCount) System.out.print(",  ");
        	}
			System.out.print("\n");
        	while (rs.next()) {
        		for (int i = 1; i <= columnCount; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < columnCount) System.out.print(",  ");
        		}
    			System.out.print("\n");
        	}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    public static void main(String[] args) throws SQLException {
    	PreparedStatement stmt = null;
    	Scanner s = new Scanner(System.in);
    	System.out.print("Enter 1 for the previous query, 2 for a new query: ");
    	int option = s.nextInt();
    	System.out.println("This is a new run");
    	Connection conn = null;
    	ResultSet rSet = null;
    	if (option == 1) {
    		conn = initializeDB(DATABASE);
    		sqlQuery(conn, sqlStatement1);
    	} else if (option == 2) {
    		try {
    			conn = initializeDB(DATABASE);
    			stmt = conn.prepareStatement(sqlStatement3);
    			rSet = stmt.executeQuery();
    			while (rSet.next()) {
    				String name = rSet.getString("name");
    				String creatorID = rSet.getString("creator");
    				System.out.println("Name: " + name + " | " + "Creator ID: " + creatorID);
    			}
    		} catch (Exception e) {
    			System.out.println("Error: unable to connect to database");
    			System.exit(1);
    		} finally {
    			if(rSet != null) { rSet.close(); }
    			if(stmt != null) { stmt.close(); }
    			if(conn != null) { conn.close(); }
    		}
    	} else if (option == 3) {
    		try {
    			Statement st = null;
    			conn = initializeDB(DATABASE);
    			st = conn.createStatement();
    			st.executeUpdate(sqlStatement1);
    		} catch (Exception e) {
    			
    		} finally {
    			if(stmt != null) { stmt.close(); }
    			if(conn != null) { conn.close(); }
    		}
    	}
    	
    	s.close();
    	
    	
    	
    	/* finally best approach
		finally{
		   
			/* From JSE7 onwards the try-with-resources statement is introduced. 
			 * The resources in the try block will be closed automatically after the use,
			 * at the end of the try block
			 *  close JDBC objects
			 * If not, use the following block:
		   try {
		      if(rs!=null) rs.close();
		   } catch (SQLException se) {
		      se.printStackTrace();
		   }
		   try {
		      if(stmt !=null) st.close();
		   } catch (SQLException se) {
		      se.printStackTrace();
		   }
		   try {
		      if(conn !=null) con.close();
		   } catch (SQLException se) {
		      se.printStackTrace();
		   }
		}*/
    }
}

//STEP 1. Import required packages
import java.sql.*;
import java.io.*;

public class Test {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
   static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";

   //  Database credentials
   static final String USERNAME = "irisxu";
   static final String PASSWORD = "9893025";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
      System.out.println("Connected database successfully...");
      
      //Create data table for Customers and Accounts
      System.out.println("Creating tables...");
      stmt = conn.createStatement();
      
      stmt.executeQuery("Drop table Owners");
      stmt.executeQuery("Drop table Accounts");
      stmt.executeQuery("Drop table Customers");

      String accountTable = 	"CREATE TABLE Accounts(	aid INTEGER," + 
			  	"interest FLOAT," +
			 	"balance FLOAT," +  
				"PRIMARY KEY (aid))";
      
      String customerTable = 	"CREATE TABLE Customers( taxID CHAR(15)," + 
			  	"PIN INTEGER," +
			 	"address CHAR(50)," +
				"name CHAR(20)," +
				"PRIMARY KEY (taxID))";
      
      String primOwner = "CREATE TABLE Owners( taxID CHAR(15)," + 
			  	"aid INTEGER," +
				"PRIMARY KEY (taxID, aid)," +
			  	"FOREIGN KEY (taxID) REFERENCES Customers)";
      
      System.out.println("Creating accounts...");
      stmt.executeQuery(accountTable);
      System.out.println("Creating customers...");
      stmt.executeQuery(customerTable);
      System.out.println("Creating owners...");
      stmt.executeQuery(primOwner);
      
      //Insert some stub data
      System.out.println("Adding data into Accounts and Customers table...");
      String data = "INSERT INTO Accounts(aid, interest, balance) VALUES (11111, 0.1, 1000.0)";
      stmt.executeQuery(data);
      data = "INSERT INTO Accounts(aid, interest, balance) VALUES (22222, 0.2, 2000.0)";
      stmt.executeQuery(data);
      data = "INSERT INTO Accounts(aid, interest, balance) VALUES (33333, 0.3, 3000.0)";
      stmt.executeQuery(data);
      data = "INSERT INTO Customers(taxID, PIN, address, name) VALUES ('abc', 1234, 'SB', 'John Doe')";
      stmt.executeQuery(data);
      data = "INSERT INTO Customers(taxID, PIN, address, name) VALUES ('def', 5678, 'IV', 'Jane Doe')";
      stmt.executeQuery(data);
      data = "INSERT INTO Owners(taxID, aid) VALUES ('abc', 11111)";
      stmt.executeQuery(data);
      data = "INSERT INTO Owners(taxID, aid) VALUES ('abc', 33333)";
      stmt.executeQuery(data);
      data = "INSERT INTO Owners(taxID, aid) VALUES ('def', 22222)";
      stmt.executeQuery(data);
      System.out.println("Done with setup...");
      
      // Query the user
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Welcome! Enter your PIN: ");
      String pin = input.readLine();
      
      String qry = "SELECT c.taxID from Customers c where c.PIN = '" + pin + "'";
      ResultSet rs = stmt.executeQuery(qry);
      String id = null;
      if (rs.next()) {
    	  id = rs.getString("taxID");
    	  qry = "SELECT * from Accounts a, Owners o where o.taxID = '" + id + "' AND a.aid = o.aid";
    	  ResultSet accts = stmt.executeQuery(qry);
    	  while(accts.next()){
    	         //Retrieve by column name
    	         int aid  = accts.getInt("aid");
    	         id = accts.getString("taxID");

    	         //Display values
    	         System.out.print("aid: " + aid);
    	         System.out.println(", taxID: " + id);
    	  }
    	  accts.close();
      }
      rs.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end JDBCExample
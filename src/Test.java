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
      
      //Setup data tables
      SetUpTables su = new SetUpTables(conn);
      su.destroy();
      su.create();
      su.initData();
      
      // Query the user
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Welcome! Enter your PIN: ");
      String pin = input.readLine();
      
      String qry = "SELECT c.taxID from Customers c where c.PIN = '" + pin + "'";
      stmt = conn.createStatement();
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
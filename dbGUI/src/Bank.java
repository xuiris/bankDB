//STEP 1. Import required packages
import java.sql.*;
import java.io.*;

public class Bank {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
   static final String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";

   //  Database credentials
   static final String USERNAME = "irisxu";
   static final String PASSWORD = "9893025"; 
   Connection conn = null;
   Statement stmt = null;
   
  public Bank (){
   try{
      //STEP 2: Register JDBC driver
      Class.forName(JDBC_DRIVER);

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
      System.out.println("Connected database successfully...");
      
      //Setup data tables
      //UNCOMMENT BELOW IF TABLES HAVE NOT BEEN INITIALIZED, OR YOU NEED TO REINITIALIZE
      SetUpTables su = new SetUpTables(conn);
      su.destroy();
      su.create();
      su.initData();
      
      // Query the user
      /*BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      
      System.out.println("Welcome!");
      System.out.println("Are you a customer? [y/n]");
      String userType = input.readLine();
      
      if (userType.equals("y")) {
          new customerInterface(conn);
      }
      else if (userType.equals("n")) {
          System.out.println("Welcome to the Bank Teller Interface!");
      }*/
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
      /*try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try*/
   }//end try
   System.out.println("Goodbye!");
}//end main
  
   public Connection getConnection(){
            return conn;
        }
}//end JDBCExample
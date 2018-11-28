//STEP 1. Import required packages
import java.sql.*;

public class SetUpTables {
	
	private Connection conn;

	SetUpTables(Connection c) throws SQLException
	{
		conn = c;
	}
	
	public void create() 
	{
		try {
			System.out.println("Creating tables...");
		      Statement stmt = conn.createStatement();

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
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
	public void destroy() 
	{
		try {
			System.out.println("Destroying tables...");
		      Statement stmt = conn.createStatement();
		      
		      stmt.executeQuery("Drop table Owners");
		      stmt.executeQuery("Drop table Accounts");
		      stmt.executeQuery("Drop table Customers");
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
	public void initData()
	{
		try {
			System.out.println("Adding data into Accounts and Customers table...");
			Statement stmt = conn.createStatement();
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
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		}
	}
	
}

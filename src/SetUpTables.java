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
			System.out.println("Initializing Banking System tables");
			
			//Account table 
			Statement st = conn.createStatement();
			String createTable =  "CREATE TABLE Account( aid INTEGER," +
									"Interest FLOAT," +
									"Balance FLOAT," +
									"PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Account table created");
			
			//Saving_Account 
			createTable =  "CREATE TABLE Saving_Account( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Saving Account table created");
		
			//Checking Account
			createTable =  "CREATE TABLE Checking_Account( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Checking Account table created");
			
			//Pocket Account
			createTable =  "CREATE TABLE Pocket_Account( pid INTEGER," + "aid INTEGER," + "flat_rate FLOAT," + "PRIMARY KEY (pid)," + "FOREIGN KEY (aid) REFERENCES Account ON DELETE CASCADE)";
			st.executeQuery(createTable);
			System.out.println("Pocket Account table created");
			
			//Student Account 
			createTable =  "CREATE TABLE Student_Account( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Student Account table created");
			
			//InterestChecking Account
			createTable =  "CREATE TABLE InterestChecking_Account( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Interest Checking Account table created");
			
			//Customer Table
			createTable = "CREATE TABLE Customer ( taxID CHAR(9)," + 
						   							"PIN INTEGER," + 
						   							"Address CHAR(40)," +
						   							"Name CHAR(20)," +
						   							"PRIMARY KEY(taxID))";
			st.executeQuery(createTable);
			System.out.println("Customer table created");
			
			//Primary_Ownwer
			createTable = "CREATE TABLE Primary_Owner( taxID CHAR(9)," +
							"aid INTEGER," + 
							"PRIMARY KEY( taxID, aid)," + 
							"FOREIGN KEY (taxID) REFERENCES Customer ON DELETE CASCADE," +
							"FOREIGN KEY(aid) REFERENCES Account)";
			st.executeQuery(createTable);
			System.out.println("Primary Owner table created");
			
			//CoOwner table
			
			createTable = "CREATE TABLE Co_Owner( taxID CHAR(9)," + 
												"aid INTEGER," +
												"PRIMARY KEY (taxID, aid)," +
												"FOREIGN KEY (taxID) REFERENCES Customer ON DELETE CASCADE," +
												"FOREIGN KEY (aid) REFERENCES Account)";
			
			st.executeQuery(createTable);
			System.out.println("Co Owner table created");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);
		}
	}
	
	public void destroy() 
	{
		try {
			Statement st = conn.createStatement();
			
			String deleteTable = "DROP TABLE Saving_Account";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Checking_Account";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Pocket_Account";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Student_Account";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE InterestChecking_Account";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Primary_Owner";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Co_Owner";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Account";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Customer";
			st.executeQuery(deleteTable);
			
			System.out.println("Tables are deleted");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);		}
	}
	
	public void initData()
	{
		try {
			System.out.println("Adding data into Account and Customer table...");
			Statement stmt = conn.createStatement();
		      String data = "INSERT INTO Account(aid, Interest, Balance) VALUES (11111, 0.1, 1000.0)";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Account(aid, Interest, Balance) VALUES (22222, 0.2, 2000.0)";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Account(aid, Interest, Balance) VALUES (33333, 0.3, 3000.0)";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Customer(taxID, PIN, Address, Name) VALUES ('abc', 1234, 'SB', 'John Doe')";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Customer(taxID, PIN, Address, Name) VALUES ('def', 5678, 'IV', 'Jane Doe')";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Primary_Owner(taxID, aid) VALUES ('abc', 11111)";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Primary_Owner(taxID, aid) VALUES ('abc', 33333)";
		      stmt.executeQuery(data);
		      data = "INSERT INTO Primary_Owner(taxID, aid) VALUES ('def', 22222)";
		      stmt.executeQuery(data);
		      System.out.println("Done with setup...");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);
		}
	}
	
}

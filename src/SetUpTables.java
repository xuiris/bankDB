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
			String createTable =  "CREATE TABLE Accounts( aid INTEGER," +
									"Interest FLOAT," +
									"Balance FLOAT," +
									"Open CHAR(1)," + // 0 if closed
									"PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Accounts table created");
			
			//Saving_Account 
			createTable =  "CREATE TABLE SavingAccounts( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Saving Account table created");
		
			//Checking Account
			createTable =  "CREATE TABLE CheckingAccounts( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Checking Account table created");
			
			//Pocket Account
			createTable =  "CREATE TABLE PocketAccounts( pid INTEGER," 
					+ "aid INTEGER," 
					+ "flat_rate FLOAT," 
					+ "PRIMARY KEY (pid)," 
					+ "FOREIGN KEY (aid) REFERENCES Accounts ON DELETE CASCADE)";
			st.executeQuery(createTable);
			System.out.println("Pocket Account table created");
			
			//Student Account 
			createTable =  "CREATE TABLE StudentAccounts( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Student Account table created");
			
			//InterestChecking Account
			createTable =  "CREATE TABLE InterestCheckingAccounts( aid INTEGER," + "PRIMARY KEY (aid))";
			st.executeQuery(createTable);
			System.out.println("Interest Checking Account table created");
			
			//Customer Table
			createTable = "CREATE TABLE Customers ( taxID CHAR(9)," + 
						   							"PIN INTEGER," + 
						   							"Address CHAR(40)," +
						   							"Name CHAR(20)," +
						   							"PRIMARY KEY(taxID))";
			st.executeQuery(createTable);
			System.out.println("Customers table created");
			
			//Primary_Ownwer
			createTable = "CREATE TABLE PrimaryOwners( taxID CHAR(9)," +
							"aid INTEGER," + 
							"PRIMARY KEY( taxID, aid)," + 
							"FOREIGN KEY (taxID) REFERENCES Customers ON DELETE CASCADE," +
							"FOREIGN KEY(aid) REFERENCES Accounts)";
			st.executeQuery(createTable);
			System.out.println("Primary Owner table created");
			
			//CoOwner table
			
			createTable = "CREATE TABLE CoOwners( taxID CHAR(9)," + 
												"aid INTEGER," +
												"PRIMARY KEY (taxID, aid)," +
												"FOREIGN KEY (taxID) REFERENCES Customers ON DELETE CASCADE," +
												"FOREIGN KEY (aid) REFERENCES Accounts)";
			
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
			
			String deleteTable = "DROP TABLE PocketAccounts";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE SavingAccounts";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE CheckingAccounts";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE StudentAccounts";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE InterestCheckingAccounts";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE PrimaryOwners";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE CoOwners";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Accounts";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Customers";
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
			System.out.println("Adding data tables...");
			Statement stmt = conn.createStatement();
			String data = "";
			data = "INSERT INTO Customers(taxID, PIN, Address, Name) VALUES ('361721022', 1234, '6667 El Colegio #40', 'Alfred Hitchcock')";
		    stmt.executeQuery(data);
		    data = "INSERT INTO Customers(taxID, PIN, Address, Name) VALUES ('231403227', 1468, '5777 Hollister', 'Billy Clinton')";
		    stmt.executeQuery(data);

		    data = "INSERT INTO Accounts(aid, Interest, Balance, Open) VALUES (43942, 0.1, 1000.0, '1')";
		    stmt.executeQuery(data);
		    data = "INSERT INTO Accounts(aid, Interest, Balance, Open) VALUES (60413, 0.2, 2000.0, '1')";
		    stmt.executeQuery(data);
		    
		    data = "INSERT INTO PrimaryOwners(taxID, aid) VALUES ('361721022', 43942)";
		    stmt.executeQuery(data);
		    
		    data = "INSERT INTO CoOwners(taxID, aid) VALUES ('231403227', 60413)";
		    stmt.executeQuery(data);
		    data = "INSERT INTO CoOwners(taxID, aid) VALUES ('361721022', 60413)";
		    stmt.executeQuery(data);

		    System.out.println("Done with setup...");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);
		}
	}
	
}

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
			
			//Accounts Table 
			Statement st = conn.createStatement();
			String createTable =  "CREATE TABLE Accounts(aid INTEGER," +
									" balance FLOAT," +
									" interest FLOAT," +
									" open CHAR(1)," + // 0 if closed
									" type VARCHAR(20)," +
									" PRIMARY KEY (aid)," +
									" CHECK (type IN ('Savings','Student-Checking','Interest-Checking','Pocket')))";
			st.executeQuery(createTable);
			System.out.println("Accounts table created");
			
			//Pocket Accounts Table
			createTable =  "CREATE TABLE LinkedPockets(pid INTEGER," 
					+ " aid INTEGER,"
					+ " PRIMARY KEY (pid)," 
					+ " FOREIGN KEY (aid) REFERENCES Accounts ON DELETE CASCADE,"
					+ " FOREIGN KEY (pid) REFERENCES Accounts ON DELETE CASCADE)";
			st.executeQuery(createTable);
			System.out.println("Linked Pocket Account table created");
			
			//Customers Table
			createTable = "CREATE TABLE Customers ( taxID CHAR(9)," + 
						   							"PIN INTEGER," + 
						   							"address CHAR(40)," +
						   							"name CHAR(20)," +
						   							"PRIMARY KEY(taxID))";
			st.executeQuery(createTable);
			System.out.println("Customers table created");
			
			//Owners Table
			createTable = "CREATE TABLE Owners(taxID CHAR(9)," +
							" aid INTEGER," + 
							" type VARCHAR(8)," +
							" PRIMARY KEY( taxID, aid)," + 
							" FOREIGN KEY (taxID) REFERENCES Customers ON DELETE CASCADE," +
							" FOREIGN KEY(aid) REFERENCES Accounts," +
							" CHECK (type in ('Primary','Co-Owner')))";
			st.executeQuery(createTable);
			System.out.println("Owners table created");
			
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
			String deleteTable = "";
			
			deleteTable = "DROP TABLE Owners";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Customers";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE LinkedPockets";
			st.executeQuery(deleteTable);
			
			deleteTable = "DROP TABLE Accounts";
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
			data = "INSERT INTO Customers(taxID, PIN, address, name) VALUES ('361721022', 1234, '6667 El Colegio #40', 'Alfred Hitchcock')";
		    stmt.executeQuery(data);
		    data = "INSERT INTO Customers(taxID, PIN, address, name) VALUES ('231403227', 1468, '5777 Hollister', 'Billy Clinton')";
		    stmt.executeQuery(data);

		    data = "INSERT INTO Accounts(aid, balance, interest, open, type) VALUES (43942, 1000.0, 1.0, '1', 'Savings')";
		    stmt.executeQuery(data);
		    data = "INSERT INTO Accounts(aid, balance, interest, open, type) VALUES (60413, 500.0, 0.0, '1', 'Pocket')";
		    stmt.executeQuery(data);
		    
		    data = "INSERT INTO LinkedPockets(pid, aid) VALUES (60413, 43942)";
		    stmt.executeQuery(data);
		    
		    data = "INSERT INTO Owners(taxID, aid, type) VALUES ('361721022', 43942, 'Primary')";
		    stmt.executeQuery(data);
		    data = "INSERT INTO Owners(taxID, aid, type) VALUES ('231403227', 60413, 'Co-Owner')";
		    stmt.executeQuery(data);
		    data = "INSERT INTO Owners(taxID, aid, type) VALUES ('361721022', 60413, 'Co-Owner')";
		    stmt.executeQuery(data);

		    System.out.println("Done with setup...");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);
		}
	}
	
}

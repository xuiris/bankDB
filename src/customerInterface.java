import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class customerInterface {
	
	private Connection conn;
	private ArrayList<Integer> accounts;

	customerInterface(Connection conn){
		try {
			this.conn = conn;
			
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Welcome to the Customer ATM Interface!");
	        System.out.println("Enter your PIN: ");
	        String pin = input.readLine();
			String qry = "SELECT c.taxID from Customers c where c.PIN = '" + pin + "'";
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(qry);
	        
	        if (rs.next()) {
	        	String id = rs.getString("taxID");
	        	
	        	// find all accounts associated
	        	accounts = new ArrayList<Integer>();
				qry = "SELECT * from Accounts a, Owners o where o.taxID = '" + id + "' AND a.aid = o.aid";
				ResultSet accts = stmt.executeQuery(qry);
				
				while(accts.next()){
					//Retrieve by column name
					int aid  = accts.getInt("aid");
					
					//Add to list
					accounts.add(aid);
				}
				accts.close();
	        	
	        	System.out.println("What would you like to do?");
	        	String command = input.readLine();
	        	processCommand(command, id);
	        }
	        else {
	        	System.out.println("Incorrect pin.");
	        }
	        rs.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error");
			System.exit(0);
			}
	    
	}
	
	private void processCommand(String c, String id) {
		switch(c) {
		case "print accounts": // THIS IS FAKE, not a real transaction. Delete before demo.
			printAccounts(id);
			break;
		case "deposit":
			deposit(id);
			break;
//		case "top up":
//			topUp(id);
//		case "withdraw":
//			withdraw(id);
//		case "purchase":
//			purchase(id);
//		case "transfer":
//			transfer(id);
//		case "collect":
//			collect(id);
//		case "pay friend":
//			payFriend(id);
//		case "wire":
//			wire(id);
//		case "write check":
//			writeCheck(id);
//		case "accrue interest":
//			accrueInterest(id);
	    default: 
	    	System.out.println("Please enter a valid transaction.");
	    }
	}
	
	private int chooseAccount(String id) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			Statement stmt = conn.createStatement();
			String qry = "SELECT * from Accounts a, Owners o where o.taxID = '" + id + "' AND a.aid = o.aid";
			ResultSet accts = stmt.executeQuery(qry);
			
			while(accts.next()){
				//Retrieve by column name
				int aid  = accts.getInt("aid");
				double rate  = accts.getDouble("Interest_rate");
				double balance = accts.getDouble("Balance");

				//Display values
				System.out.print("aid: " + aid);
				System.out.print(", interest rate: " + rate);
				System.out.println(", balance: " + balance);
			}
			accts.close();
			
			System.out.println("Please enter the AID of the account you would like to transact on: ");
			int aid = 0;
			try {
				aid = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (accounts.contains(aid)) {
				return aid;
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error accessing accounts");
		}
		return 0;
	}
	
	private void printAccounts(String id) {
		try {
			Statement stmt = conn.createStatement();
			String qry = "SELECT * from Accounts a, Owners o where o.taxID = '" + id + "' AND a.aid = o.aid";
			ResultSet accts = stmt.executeQuery(qry);
			while(accts.next()){
				//Retrieve by column name
				int aid  = accts.getInt("aid");

				//Display values
				System.out.print("aid: " + aid);
				System.out.println(", taxID: " + id);
			}
			accts.close();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error printing accounts");
		}
	}
	
	private void deposit(String id) {
		try {
			// Ask user for account they want to transact on
			int aid = chooseAccount(id);
			if (aid == 0) {
				System.out.println("Error when choosing account to deposit to.");
				return;
			}			
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("How much would you like to deposit?");
			int amt = 0;
			try {
				aid = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			// Pull account, place in Account object
			Account a = Account.getAccount(conn, aid);
			// Modify object to mirror deposit
			a.balance += amt;
			// Update this in the DB using account object.
			a.updateAccountDB(conn);
			
			
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error depositing into account");
			System.exit(0);
		}
	}
}

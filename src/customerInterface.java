import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class customerInterface {
	
	private Connection conn;
	private ArrayList<Account> accounts;
	private String id;

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
	        	id = rs.getString("taxID");
	        	
	        	// find all OPEN accounts associated with this person
	        	accounts = new ArrayList<Account>();
				qry = "SELECT DISTINCT a.aid, a.interest, a.balance, a.open, a.type FROM Accounts a, Owners o"
						+ " WHERE o.taxID = '" + id + "'"
						+ " AND a.aid = o.aid"
						+ " AND a.Open = '1'";
				ResultSet accts = stmt.executeQuery(qry);
				
				while(accts.next()){
					//Retrieve by column name
					int aid  = accts.getInt("aid");
					
					//Add to list of accounts for this customer
					accounts.add(Account.getAccount(conn, aid));
				}
				accts.close();
	        	
	        	System.out.println("What would you like to do?");
	        	String command = input.readLine();
	        	processCommand(command);
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
	
	private void processCommand(String c) {
		switch(c) {
		case "print accounts": // THIS IS FAKE, not a real transaction. Delete before demo.
			printAccounts();
			break;
		case "deposit":
			deposit();
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
	
	private int chooseAccount() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Your accounts: ");
			printAccounts();
			
			ArrayList<Integer> aids = new ArrayList<Integer>();
			for (Account a: accounts) aids.add(a.aid);
			
			System.out.println("Please enter the AID of the account you would like to transact on: ");
			int aid = 0;
			try {
				aid = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (aids.contains(aid)) {
				return aid;
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error accessing accounts");
		}
		return 0;
	}
	
	private void printAccounts() {
		for (Account a: accounts) {
			System.out.println(a.toString());
		}
	}
	
	private void deposit() {
		try {
			// Ask user for account they want to transact on
			int aid = chooseAccount();
			if (aid == 0) {
				System.out.println("Error when choosing account to deposit to.");
				return;
			}			
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("How much would you like to deposit?");
			int amt = 0;
			try {
				amt = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			// Pull account, place in Account object
			Account a = Account.getAccount(conn, aid);
			// Modify object to mirror deposit
			a.balance += amt;
			// Update this in the DB using account object.
			if (a.updateAccountDB(conn)) {
				// Add transaction.
			} 
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error depositing into account");
		}
	}
}

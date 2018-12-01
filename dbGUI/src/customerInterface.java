import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class customerInterface {
	
	private Connection conn;
	private Map<Integer, Account> accounts;
	private String id;
	private Map<Integer, Integer> linked; // pid, aid
	private String day;

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
	        	accounts = new HashMap<Integer, Account>();
				qry = "SELECT DISTINCT a.aid, a.interest, a.balance, a.open, a.type FROM Accounts a, Owners o"
						+ " WHERE o.taxID = '" + id + "'"
						+ " AND a.aid = o.aid"
						+ " AND a.Open = '1'";
				ResultSet accts = stmt.executeQuery(qry);
				
				while(accts.next()){
					//Retrieve by column name
					int aid  = accts.getInt("aid");
					
					//Add to list of accounts for this customer
					accounts.put(aid, Account.getAccount(conn, aid));
				}
				accts.close();
				
				// Enter linked accounts into linked map
				linked = new HashMap<Integer, Integer>();
				for (Map.Entry<Integer, Account> a: accounts.entrySet()) {
					if (a.getValue().type.equals("Pocket")) {
						stmt = conn.createStatement();
						qry = "SELECT * from LinkedPockets p where p.pid = " + a.getKey();
						rs = stmt.executeQuery(qry);
						if (rs.next()) {
							linked.put(rs.getInt("pid"), rs.getInt("aid"));
						}
					}
				}
	        	
				String cont = "y";
				
				while (cont.equals("y")) {
					System.out.println("What would you like to do?");
		        	String command = input.readLine();
		        	
			        System.out.println("Enter the date (mm-dd-yyyy): ");
			        String dateInput = input.readLine();
			        if (null != dateInput && dateInput.trim().length() > 0){
			            day = dateInput;
			        } else {
			        	System.out.println("Using default date 01-01-2000");
			        	day = "01-01-2000";
			        }
			        
		        	processCommand(command);
		        	System.out.println("Continue? (y/n)");
		        	cont = input.readLine();	
				}	
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
		case "top up":
			topUp();
			break;
		case "withdraw":
			withdraw();
			break;
		case "purchase":
			purchase();
			break;
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
			
			System.out.println("Please enter the AID of the account you would like to transact on: ");
			int aid = 0;
			try {
				aid = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (accounts.containsKey(aid)) {
				return aid;
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error accessing accounts");
		}
		return 0;
	}
	
	private void printAccounts() {
		for (Map.Entry<Integer, Account> a: accounts.entrySet()) {
			System.out.println(a.getValue().toString());
		}
	}
	
	private void deposit() {
		try {
			// Ask user for account they want to transact on
			int count = 0;
			int aid = 0;
			Account a = new Account();
			while (count < 3) {
				aid = chooseAccount();
				if (aid == 0) {
					System.out.println("Error when choosing account to deposit to.");
					return;
				}
				// Pull account, place in Account object, check if its savings or checkings
				a = accounts.get(aid);
				if (a.type.equals("Savings") || a.type.equals("Student-Checking") || a.type.equals("Interest-Checking")) break;
				System.out.println("Please choose only Savings or Checkings.");
				count += 1;
			}
			if (count > 2) {
				System.out.println("Failed to choose valid account.");
				return;
			}
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("How much would you like to deposit?");
			double amt = 0;
			try {
				amt = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (amt < 0) {
				System.out.println("Cannot deposit negative amount.");
				return;
			}
			
			a.balance += amt;
			// Update this in the DB using account object.
			if (a.updateAccountDB(conn)) {
				System.out.println("Deposit successful.");
				printAccounts();
				// Add transaction.
				if (Transaction.createDeposit(conn, day, amt, aid, id)) {
					System.out.println("Transaction recorded.");
				} else {
					System.out.println("Bad behavior - Error recording deposit transaction.");
				}
			} 
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error depositing into account");
		}
	}
	
	private void topUp() {
		try {
			// Ask user for account they want to transact on
			int count = 0;
			int pid = 0;
			Account pa = new Account();
			while (count < 3) {
				pid = chooseAccount();
				if (pid == 0) {
					System.out.println("Error when choosing account to topup.");
					return;
				}
				
				pa = accounts.get(pid);
				if (pa.type.equals("Pocket")) break;
				System.out.println("Please choose only a pocket account.");
				count += 1;
			}
			if (count > 2) {
				System.out.println("Failed to choose valid account.");
				return;
			}
			
			// find the linked Saving/Checking acct
			int link = linked.get(pid);
			Account la = accounts.get(link);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("How much would you like to top up into the linked Pocket account?");
			int amt = 0;
			try {
				amt = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (amt < 0) {
				System.out.println("Cannot topup negative amount.");
				return;
			}
			if (amt > la.balance) {
				System.out.println("Insufficient funds.");
				return;
			}
			
			pa.balance += amt;
			la.balance -= amt;
			
			// Update this in the DB using account object.
			if (pa.updateAccountDB(conn) && la.updateAccountDB(conn)) {
				System.out.println("Top up successful.");
				printAccounts();
				if (Transaction.createTopUp(conn, day, amt, pid, id)) {
					System.out.println("Transaction recorded.");
				} else {
					System.out.println("Bad behavior - Error recording TopUp transaction.");
				}
			} 
			
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error topping up account");
		}
	}
	
	private void withdraw() {
		try {
			// Ask user for account they want to transact on
			int count = 0;
			int aid = 0;
			Account a = new Account();
			while (count < 3) {
				aid = chooseAccount();
				if (aid == 0) {
					System.out.println("Error when choosing account to withdraw from.");
					return;
				}
				// Pull account, place in Account object, check if its savings or checkings
				a = accounts.get(aid);
				if (a.type.equals("Savings") || a.type.equals("Student-Checking") || a.type.equals("Interest-Checking")) break;
				System.out.println("Please choose only Savings or Checkings.");
				count += 1;
			}
			if (count > 2) {
				System.out.println("Failed to choose valid account.");
				return;
			}
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("How much would you like to withdraw?");
			int amt = 0;
			try {
				amt = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (amt < 0) {
				System.out.println("Cannot withdraw negative amount.");
				return;
			}
			if (amt > a.balance) {
				System.out.println("Insufficient funds.");
				return;
			}
			
			a.balance -= amt;
			// Update this in the DB using account object.
			if (a.updateAccountDB(conn)) {
				System.out.println("Withdrawal successful.");
				printAccounts();
				if (Transaction.createWithdraw(conn, day, amt, aid, id)) {
					System.out.println("Transaction recorded.");
				} else {
					System.out.println("Bad behavior - Error recording withdraw transaction.");
				}
			} 
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error withdrawing from account");
		}
	}
	
	private void purchase() {
		try {
			// Ask user for account they want to transact on
			int count = 0;
			int pid = 0;
			Account pa = new Account();
			while (count < 3) {
				pid = chooseAccount();
				if (pid == 0) {
					System.out.println("Error when choosing account to purchase from.");
					return;
				}
				
				pa = accounts.get(pid);
				if (pa.type.equals("Pocket")) break;
				System.out.println("Please choose only a pocket account.");
				count += 1;
			}
			if (count > 2) {
				System.out.println("Failed to choose valid account.");
				return;
			}
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("How much is this purchase transaction?");
			int amt = 0;
			try {
				amt = Integer.parseInt(input.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Not a number");
			}
			
			if (amt < 0) {
				System.out.println("Cannot purchase a negative amount.");
				return;
			}
			if (amt > pa.balance) {
				System.out.println("Insufficient funds for this purchase.");
				return;
			}
			
			pa.balance -= amt;
			
			// Update this in the DB using account object.
			if (pa.updateAccountDB(conn)) {
				System.out.println("Purchase successful.");
				printAccounts();
				if (Transaction.createPurchase(conn, day, amt, pid, id)) {
					System.out.println("Transaction recorded.");
				} else {
					System.out.println("Bad behavior - Error recording purchase transaction.");
				}
			} 
			
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Error using account to purchase");
		}
	}
}

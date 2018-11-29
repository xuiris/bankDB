import java.sql.*;

public class Account {
	public int aid;
	public double interest;
	public double balance;
	public boolean isOpen;
	
	Account() {}
	
	Account(int Aid, double Interest, double Balance, boolean Open) {
		aid = Aid;
		interest = Interest;
		balance = Balance;
		isOpen = Open;
	}
	
	public static Account getAccount(Connection conn, int aid) throws SQLException {
		Statement stmt = conn.createStatement();
		String qry = "SELECT * from Accounts a where a.aid = " + aid;
		ResultSet rs = stmt.executeQuery(qry);
		Account a = new Account();
		
		if (rs.next()) {
			a.aid = rs.getInt("aid");
			a.interest = rs.getDouble("Interest");
			a.balance = rs.getDouble("Balance");
			String status = rs.getString("Open");
			a.isOpen = true;
			if (status.equals("0")) a.isOpen = false;
        } 
		else {
			System.out.println("No account found with aid: " + aid);
	        throw new SQLException("Access with invalid aid");
		}
		
		return a;
	}
	
	public boolean updateAccountDB(Connection conn){
		try {
			Statement stmt = conn.createStatement();
			int status = 0;
			if (isOpen) status = 1;
			String qry = "UPDATE Accounts "
					+ "SET Interest = " + interest 
					+ ", Balance = " + balance 
					+ ", Open = " + status 				
					+ " WHERE aid = " + aid;
			stmt.executeQuery(qry);
			return true;	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error updating account " + aid);
			return false;
		}	
	}
	
	public String toString() {
		return ("aid: " + aid +  ", balance: " + balance + ", interest: " + interest + ", open: " + isOpen);
	}
		
	
}
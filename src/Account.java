import java.sql.*;

public class Account {
	public int aid;
	public double interest;
	public double balance;
	
	Account() {}
	
	Account(int Aid, double Interest, double Balance) {
		aid = Aid;
		interest = Interest;
		balance = Balance;
	}
	
	public static Account getAccount(Connection conn, int aid) throws SQLException {
		Statement stmt = conn.createStatement();
		String qry = "SELECT * from Accounts a where a.aid = " + aid;
		ResultSet rs = stmt.executeQuery(qry);
		Account a = new Account();
		
		if (rs.next()) {
			a.aid = rs.getInt("aid");
			a.interest = rs.getDouble("Interest_rate");
			a.balance = rs.getDouble("Balance");
        } 
		else {
			System.out.println("No account found with aid: " + aid);
	        throw new SQLException("Access with invalid aid");
		}
		
		return a;
	}
	
	public void updateAccountDB(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String qry = "UPDATE Accounts "
				+ "SET Interest_rate = " + interest 
				+ ", Balance = " + balance 
				+ " WHERE aid = " + aid;
		stmt.executeQuery(qry);
	}
		
	
}
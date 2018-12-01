import java.sql.*;

public class Transaction {
	// This class will handle creation and deletion
	// of transactions into the database.
	
	// also need to handle the DATE!
	
	public static boolean createDeposit(Connection conn, String day, double added, int aid, String taxID){
		try {
			Statement stmt = conn.createStatement();
			int tid = newTid(conn);
			String qry = "INSERT INTO Transactions(tid, day, type) VALUES (" 
					+ tid 
					+ ", TO_DATE('" + day + "', 'MM-DD-YYYY')"
					+ ", 'Deposit')";
			System.out.println(qry);
			stmt.executeQuery(qry);
			qry = "INSERT INTO Deposit(tid, amt, aid, taxID) VALUES (" 
					+ tid 
					+ ", " + added
					+ ", " + aid
					+ ", " + taxID + ")";
			stmt.executeQuery(qry);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean createTopUp(Connection conn, String day, double added, int pid, String taxID){
		try {
			Statement stmt = conn.createStatement();
			int tid = newTid(conn);
			String qry = "INSERT INTO Transactions(tid, day, type) VALUES (" 
					+ tid 
					+ ", TO_DATE('" + day + "', 'MM-DD-YYYY')"
					+ ", 'TopUp')";
			System.out.println(qry);
			stmt.executeQuery(qry);
			qry = "INSERT INTO TopUp(tid, amt, pid, taxID) VALUES (" 
					+ tid 
					+ ", " + added
					+ ", " + pid 
					+ ", " + taxID + ")";
			stmt.executeQuery(qry);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public static boolean createWithdraw(Connection conn, String day, double subtracted, int aid, String taxID) {
		try {
			Statement stmt = conn.createStatement();
			int tid = newTid(conn);
			String qry = "INSERT INTO Transactions(tid, day, type) VALUES (" 
					+ tid 
					+ ", TO_DATE('" + day + "', 'MM-DD-YYYY')"
					+ ", 'Withdraw')";
			System.out.println(qry);
			stmt.executeQuery(qry);
			qry = "INSERT INTO Withdraw(tid, amt, aid, taxID) VALUES (" 
					+ tid 
					+ ", " + subtracted
					+ ", " + aid 
					+ ", " + taxID + ")";
			stmt.executeQuery(qry);
			return true;
		} catch (SQLException e) {
			return false;
		}	
	}
	
	public static boolean createPurchase(Connection conn, String day, double amt, int pid, String taxID) {
		try {
			Statement stmt = conn.createStatement();
			int tid = newTid(conn);
			String qry = "INSERT INTO Transactions(tid, day, type) VALUES (" 
					+ tid 
					+ ", TO_DATE('" + day + "', 'MM-DD-YYYY')"
					+ ", 'Purchase')";
			System.out.println(qry);
			stmt.executeQuery(qry);
			qry = "INSERT INTO Purchase(tid, amt, pid, taxID) VALUES (" 
					+ tid 
					+ ", " + amt
					+ ", " + pid 
					+ ", " + taxID + ")";
			stmt.executeQuery(qry);
			return true;
		} catch (SQLException e) {
			return false;
		}	
	}
	
	// Come up with an available and unique tid.
	private static int newTid(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String qry = "SELECT MAX(t.tid) AS Max FROM Transactions t";
		ResultSet rs = stmt.executeQuery(qry);
		if (rs.next()) {
			// use next available index.
			return (rs.getInt("Max") + 1);
		} else {
			// no transactions yet. start at 0.
			return 0;			
		}
	}
}

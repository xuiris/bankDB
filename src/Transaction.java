import java.sql.*;

public class Transaction {
	// This class will handle creation and deletion
	// of transactions into the database.
	
	// also need to handle the DATE!
	
	public static boolean createDeposit(Connection conn, java.util.Date day, double added, int aid, String taxID) throws SQLException{
		Statement stmt = conn.createStatement();
		int tid = newTid(conn);
		String qry = "INSERT INTO Transactions(tid, day, type) VALUES (" 
				+ tid 
				+ ", " + day
				+ ", 'Deposit')";
		stmt.executeQuery(qry);
		qry = "INSERT INTO Deposit(tid, added, aid) VALUES (" 
				+ tid 
				+ ", " + added
				+ ", " + aid + ")";
		stmt.executeQuery(qry);
		
		return false;
	}
	
	public static boolean createTopUp(Connection conn, double added, int pid, String taxID) {
		return false;
	}

	public static boolean createWithdraw(Connection conn, double substracted, int aid, String taxID) {
		return false;
	}
	
	public static boolean createPurchase(Connection conn, double withdrawn, int pid, String taxID) {
		return false;
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

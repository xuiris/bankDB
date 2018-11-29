import java.sql.*;

public class Transaction {
	// This class will handle creation and deletion
	// of transactions into the database.
	
	// need to come up with unique tid for the 
	// transaciton. Perhaps getMax tid from the table
	// and do +1 of that. If empty then start at 0.
	
	// also need to handle the DATE!
	
	public static boolean deposit(double added, int aid, String taxID) {
		return false;
	}
	
	public static boolean topUp(double added, int pid, String taxID) {
		return false;
	}

	public static boolean withdraw(double substracted, int aid, String taxID) {
		return false;
	}
	
	public static boolean purchase(double withdrawn, int pid, String taxID) {
		return false;
	}
}

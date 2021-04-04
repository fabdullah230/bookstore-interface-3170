import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class orderQuery {

	//orderID, customerID, orderDate and charge from a particular yyyy-mm
	public static String oQuery(String s) {
		return "select order_id, customer_id, o_date, charge from orders where month(o_date) = "
				+ s.substring(s.length() - 2, s.length()) + " and year(o_date) = " + s.substring(0, 4)
				+ " order by order_id";
	}

	//sum of charges in a yyyy-mm
	public static String totalCharge(String s) {
		return "select sum(charge) from orders where month(o_date) = " + s.substring(s.length() - 2, s.length())
				+ " and year(o_date) = " + s.substring(0, 4);
	}

	public static void start() throws SQLException {

		String url = "jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false";
		String uname = "root";
		String password = "fabdullah230";
		Scanner s = new Scanner(System.in);

		//user input
		System.out.print("Please input the month for order query (e.g. 2005-09): ");
		String n = s.nextLine();

		Connection con = DriverManager.getConnection(url, uname, password);
		Statement statement = con.createStatement();
		
		//error handling
		ResultSet result = null;
		try {
		result = statement.executeQuery(oQuery(n));
		}
		catch(StringIndexOutOfBoundsException | MySQLSyntaxErrorException e ){
			System.out.println("Incorrect Date format");
			return;
		}
		
		//printing out results
		int j = 1;
		while (result.next()) {
			System.out.println("Record : " + j);
			try {
				int id = result.getInt(1);
				System.out.println("order_id : " + String.format("%08d", id));
				System.out.println("customer_id : " + result.getString(2));
				System.out.println("date : " + result.getString(3));
				System.out.println("charge : " + result.getString(4));

			} catch (SQLException e) {
				//do nothing, harmless exception
			}
			System.out.println("\n");
			j++;
		}

		//fetch total charge
		result = statement.executeQuery(totalCharge(n));
		result.next();
		System.out.println("Total charges this month is " + result.getString(1));

	}

}

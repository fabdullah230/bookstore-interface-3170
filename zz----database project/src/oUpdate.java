import java.sql.*;
import java.util.Scanner;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.sun.net.httpserver.Authenticator.Result;

public class oUpdate {

	// fetch shipping status and total number of books of an order by orderid
	public static String orderUpdate(String s) {
		return "select s.shipping_status, sum(r.quantity) from orders s, ordering r where s.order_id = r.order_id AND s.order_id ="
				+ s;
	}

	// update shipping status
	public static String updateSS(String s) {
		return "update orders set shipping_status = \"Y\" where order_id =" + s;
	}

	public static void start() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false";
		String uname = "root";
		String password = "fabdullah230";
		Scanner s = new Scanner(System.in);

		// user input
		System.out.print("Please input the order ID: ");
		String n = s.nextLine();

		//error handling
		Connection con = DriverManager.getConnection(url, uname, password);
		Statement statement = con.createStatement();
		ResultSet result = null;
		try {
			result = statement.executeQuery(orderUpdate(n));
		}
		catch(MySQLSyntaxErrorException e) {
			System.out.println("Incorrect format of order ID");
			return;
		}
		String quantity = null;
		String shipping_statsu = null;

		result.next();
		shipping_statsu = result.getString(1);
		quantity = result.getString(2);
		
		//handling non-existent orderID
		if(shipping_statsu == null && quantity == null) {
			System.out.println("Order ID does not exist");
			return;
		}

		//printing out results
		System.out.println("The Shipping status of " + n + " is " + shipping_statsu + " and " + quantity + " books ordered.");
		if (shipping_statsu.equals("N")) {
			System.out.print("Are you sure to update the shipping status? (Yes=Y)");
			String c = s.nextLine();
			if (c.toLowerCase().equals("y")) {
				statement.executeUpdate(updateSS(n));
				System.out.println("Updated Shipping status");
			} else {
				// invalid input
				System.out.println("Invalid command, task aborted");
			}
		}

	}

}

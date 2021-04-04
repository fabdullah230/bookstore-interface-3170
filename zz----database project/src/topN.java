import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class topN {

	public String ISBN;
	public int quantity;

	public topN(String ISBN, int quantity) {
		this.ISBN = ISBN;
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	// fetch ALL books
	public static String getBooks() {
		return "select ISBN from book";
	}

	// fetch quantity of book by isbn
	public static String getQtity(String s) {
		return "select sum(quantity) from ordering where ISBN = \"" + s + "\"";
	}

	// fetch title of book ny isbn
	public static String getTitle(String s) {
		return "select title from book where ISBN = \"" + s + "\"";
	}

	public String toString() {
		return "\nBook ISBN: " + ISBN + " | Order Quantity: " + quantity;
	}

	public static void start() throws SQLException {

		String url = "jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false";
		String uname = "root";
		String password = "fabdullah230";
		Scanner s = new Scanner(System.in);

		Connection con = DriverManager.getConnection(url, uname, password);
		Statement statement = con.createStatement();

		// Arraylist of all books and object{isbn, quantity}
		ArrayList<String> books = new ArrayList<String>();
		ArrayList<topN> bookplusquantity = new ArrayList<topN>();

		// Fetching all the books
		ResultSet result = statement.executeQuery(getBooks());
		while (result.next()) {
			books.add(result.getString(1));
		}
		// System.out.println(books);

		// for each book fetching its quantity and inserting in arraylist of
		// object{isbn, quantity}
		for (String i : books) {
			result = statement.executeQuery(getQtity(i));
			result.next();
			int quantity = result.getInt(1);
			bookplusquantity.add(new topN(i, quantity));
		}
		// System.out.println(bookplusquantity);

		// Sort books in descending order of quantity
		bookplusquantity.sort((topN n1, topN n2) -> {
			if (n1.quantity > n2.quantity)
				return -1;
			if (n1.quantity < n2.quantity)
				return 1;
			return 0;
		});
		// System.out.println("Sorted book order:" + bookplusquantity);

		// getting rid of books with 0 orders
		int size = bookplusquantity.size();
		for (int k = 0; k < size; k++) {
			if (bookplusquantity.get(k).quantity == 0) {
				bookplusquantity.remove(k);
			}
		}
		// System.out.println("Books with >0 orders: "+ bookplusquantity);

		// User input and validation
		System.out.print("Please input the N popular books number: ");
		int n = 0 ;
		try{
			 n = s.nextInt();
		}
		catch(InputMismatchException e) {
			System.out.println("Invalid input, function terminated");
			return;
		}

		// Fetches title of books on the go, displays as isbn|title|copies
		int in = 0;
		try {
			System.out.println("ISBN\t\tTitle\t\tCopies");
			while (in < n) {
				result = statement.executeQuery(getTitle(bookplusquantity.get(in).ISBN));
				result.next();
				String title = result.getString(1);
				System.out.println(
						bookplusquantity.get(in).ISBN + "\t" + title + "\t" + bookplusquantity.get(in).quantity);
				if (bookplusquantity.get(in).quantity == bookplusquantity.get(in + 1).quantity) {
					n++;
				}
				in++;
			}
		} catch (IndexOutOfBoundsException e) {
			// if user inputs n greater than available books, just catches the error and
			// does nothing
		}

	}

}

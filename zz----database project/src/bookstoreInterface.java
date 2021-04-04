import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class bookstoreInterface {

	public static void main(String[] args) throws SQLException {

		System.out.println(
				"<This is the bookstore interface.>\n" + "----------------------------------\n" + "1. Order Update.\n"
						+ "2. Order Query.\n" + "3. N most Popular Book Query.\n" + "4. Back to main menu.");

		Scanner s = new Scanner(System.in);

		int choice = 0;
		while (choice != 4) {

			// user input and format validation
			System.out.print("What is your choice??.. ");
			while (true) {
				try {
					choice = s.nextInt();
					break;
				} catch (InputMismatchException e) {
					System.out.print("Invalid choice format.. try again ");
					s.next();
				}
			}

			// choices
			if (choice == 1) {
				// Order Update
				oUpdate.start();
			} else if (choice == 2) {
				// Order Query
				orderQuery.start();
			} else if (choice == 3) {
				// N most Popular Book Query
				topN.start();
			} else if (choice == 4) {
				// Back to main menu
				System.out.println("Returning to main menu");
			} else {
				System.out.println("Invalid choice");
			}
			System.out.println();
		}

	}

}

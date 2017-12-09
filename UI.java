package moviescheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UI {
	
	Connection conn;
	Scanner sc;
	User u;
	
	public UI(Connection conn) {
		this.conn = conn;
		sc = new Scanner(System.in);
	}
	
	void initialPrompt() {
		System.out.println("Welcome! Please select a number:");
		System.out.println("1. Login");
		System.out.println("2. Create new account");
		System.out.println("3. Exit");
		String selection = sc.next();
		switch(selection) {
		case "1":
			login();
			break;
		case "2":
			newAccount();
			break;
		case "3":
			System.out.println("Goodbye!");	
			break;
		default:
			System.out.println("Error! Please select again.");
			initialPrompt();
			break;
		}
	}
	
	void login() {
		
		System.out.println("Please enter your username ");
		String username = sc.next();
		menu(username);
		
	}
	
	
	void newAccount() {
		
		System.out.println("Please enter a username: ");
		String username = sc.next();
		System.out.println("Please enter your phone number: ");
		String phone = sc.next();
		System.out.println("Please enter your email: ");
		String email = sc.next();
		createUser(username,phone,email);
	}
	
	void createUser(String username, String phoneNumber, String email) {
		
		try {
			
			String sql = "INSERT INTO user(username,phoneNumber,email,admin,rewardsNumber) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement insertUser = conn.prepareStatement(sql);
			insertUser.setString(1, username);
			insertUser.setString(2, phoneNumber);
			insertUser.setString(3, email);
			insertUser.setInt(4, 1);
			insertUser.setString(5, "1113");
			insertUser.executeUpdate();
			System.out.println("Success Account Created");
			menu(username);
			
		}catch(SQLException se) {
			
			System.out.println("add User Error");
			
		};

	}
	
	void menu(String username) {
		
		u = new User(conn,username);
		System.out.println("Please select an option: ");
		System.out.println("1. Reservations");
		System.out.println("2. Showtimes");
		System.out.println("3. Reviews");
		if(u.isAdmin()) {
			System.out.println("4. Manage Users");
			System.out.println("5. Manage Showtimes");
		}
		System.out.println("6. Logout");
		System.out.println("7. Exit");
		String selection = sc.next();
		switch(selection) {
		case "1":
			reservationMenu(username);
			break;
		case "2":
			showtimeMenu(username);
			break;
		case "3":
			reviewMenu(username);
			break;
		case "4":
			manageUsersMenu(username);
			break;
		case "5":
			manageShowtimesMenu(username);
			break;
		case "6":
			initialPrompt();
			break;
		case "7":
			System.out.println("Goodbye!");
			break;
		default:
			System.out.println("Error! Please select again");
			menu(username);
			break;
		}
	}
	
	void reservationMenu(String username) {
		String movTitle;
		String time;
		String seatRes;
		System.out.println("Please select an option: ");
		System.out.println("1. View Reservations");
		System.out.println("2. Add Reservations");
		System.out.println("3. Cancel Reservations");
		System.out.println("4. Back");
		String selection = sc.next();
		switch(selection) {
		case "1":
			ArrayList<String[]> res = u.viewReservation();
			System.out.println("MOVIE NAME \tSHOWTIME \t\tSEATS RESERVED");
			for(String[] entry : res) {
				System.out.println(entry[0] + "\t" + entry[1] + "\t" + entry[2]);
			}
			reservationMenu(username);
			break;
		case "2":
			System.out.println("How many seats to be reserved?");
			seatRes = sc.next();
			sc.nextLine();
			System.out.println("What movie?");
			movTitle = sc.nextLine();
			System.out.println("What showtime?");
			time = sc.nextLine();
			System.out.println(seatRes);
			System.out.println(movTitle);
			System.out.println(time);
			u.addReservation(Integer.parseInt(seatRes), time, movTitle);
			System.out.println("Successfully added reservation!");
			reservationMenu(username);
			break;
		case "3":
			sc.nextLine();
			System.out.println("What movie?");
			movTitle = sc.nextLine();
			System.out.println("What showtime?");
			time = sc.nextLine();
			u.cancelReservation(time, movTitle);
			System.out.println("Successfully deleted reservation!");
			reservationMenu(username);
			break;
		case "4":
			menu(username);
			break;
		}
	}
	
	/**
	 * 
	 */
	void showtimeMenu(String username) {
		
	}
	
	/**
	 * Functions offered: 
	 * 		Users can obtain movie reviews with corresponding rating ****NOTE: Change to find movies with average rating 
	 * 		Users can add reviews for a specified movie
	 * 		Users can remove their review for a specified movie
	 */
	void reviewMenu(String username) 
	{
		System.out.println("Reviews were selected.\n Please select an option: ");
		System.out.println("1. Obtain movie reviews with a corresponding rating.");
		System.out.println("2. Add reviews for a specified movie.");
		System.out.println("3. Remove a review you wrote.");
		System.out.println("4. Back");
		
		String selection = sc.next();

		switch(selection) {
			case "1": 
				getMovieReviewsBasedOnRating();
				reviewMenu(username);
				break;
			case "2": 
				addReviews();
				reviewMenu(username);				
				break;
			case "3":
				removeReviews();
				reviewMenu(username);				
				break;
			case "4":
				menu(username);
				break;
			default:
				System.out.println("Incorrect response. Please insert a valid value in the menu:");
				reviewMenu(username);
		}
	}
	
	/**
	 * Functions offered: 
	 * 		Admins can obtain information about a user, specifically their name, phone number, and rewards number
	 * 		Admins can add users
	 * 		Admins can delete users
	 */
	void manageUsersMenu(String username) {
		System.out.println("Managing users menu. \n Please select an option: ");
		System.out.println("1. Obtain information about a user.");
		System.out.println("2. Add a new user.");
		System.out.println("3. Delete a user.");
		System.out.println("4. Back");

		String selection = sc.next();

		switch(selection) {
			case "1":
				obtainUserInfo();
				manageUsersMenu(username);
				break;
			case "2":
				addNewUser();
				manageUsersMenu(username);
				break;
			case "3":
				deleteUser();
				manageUsersMenu(username);
				break;
			case "4":
				menu(username);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Functions offered: 
	 * 		Admins can add new show times for a specified movie
	 * 		Admins can add a movie, given they add at least one show
	 */
	void manageShowtimesMenu(String username)
	{
		System.out.println("Managing showtimes menu. \n Please select an option: ");
		System.out.println("1. Add a new showtime for an existing movie.");
		System.out.println("2. Add a new movie. At least one show for this movie must be added as well.");
		System.out.println("3. Back");

		String selection = sc.next();

		switch(selection) {
			case "1":
				addShowtime();
				manageShowtimesMenu(username);
				break;
			case "2":
				addMovie();
				manageShowtimesMenu(username);
				break;
			case "3":
				menu(username);
				break;
			default:
				break;
		}
	}

	void addShowtime()
	{
		String movTitle, time;
		int theaterNum;
	
		sc.nextLine();

		System.out.println("What movie would you like to add a showtime for?");
		movTitle = sc.nextLine();
		System.out.println("What time will this show be?");
		time = sc.nextLine();
		System.out.println("What theater will this show be in?");
		theaterNum = sc.nextInt();

		u.addShowtimes(movTitle, time, theaterNum);
		System.out.println("Successfully added your showtime.\n");
	}

	void addMovie()
	{
		String movTitle, movDescrip, time;
		int theaterNum;

		sc.nextLine();

		System.out.println("What is the title of the new movie?");
		movTitle = sc.nextLine();
		System.out.println("Write the movie description, press [Enter] when finished.");
		movDescrip = sc.nextLine();
		System.out.println("Please add a show. Indicate a time for this new show: ");
		time = sc.nextLine();
		System.out.println("What theater will this show be in?");
		theaterNum = sc.nextInt();
		sc.nextLine();
		
		u.addMovie(movTitle, movDescrip, time, theaterNum);
		System.out.println("Successfully added a new movie with a new show.\n");
	}
	
	void getMovieReviewsBasedOnRating()
	{
		int rating;

		System.out.println("To obtain movie reviews with the given rating, please indicate the rating (1-5): ");
		rating = sc.nextInt();

		u.viewRatingReview(rating);
	}

	void addReviews()
	{
		String movieName, movieReview;
		int movieRating;
		sc.nextLine();
		System.out.println("What movie would you like to add a review for?");
		movieName = sc.nextLine();
		
		/**Check if this movie has already been reviewed by the specified user
		if(u.checkReview(movieName))
		{
			System.out.println("This movie has been reviewed already by you. Would you like to delete your current review?");
		}**/

		System.out.println("What rating do you give this movie (1-5)?");
		movieRating = sc.nextInt();
		sc.nextLine();

		System.out.println("Please write your review, and then press enter when finished.");
		movieReview = sc.nextLine();

		u.addReview(movieName, movieRating, movieReview);
		System.out.println("Successfully added your review.\n");
	}

	void removeReviews()
	{
		String movieName;

		System.out.println("What movie would you like to delete the review for?");
		movieName = sc.nextLine();

		u.removeReview(movieName);
		System.out.println("Successfully deleted your review.\n");


	}

	void obtainUserInfo()
	{
		String username;

		sc.nextLine();
		System.out.println("Enter the username of the user that you're interested in: ");
		username = sc.nextLine();

		u.userInfo(username);
	}

	void addNewUser()
	{
		String username;

	}

	void deleteUser()
	{

	}
}

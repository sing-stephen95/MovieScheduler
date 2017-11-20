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
			insertUser.setString(5, "1111");
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
		}
		System.out.println("5. Logout");
		System.out.println("6. Exit");
		String selection = sc.next();
		switch(selection) {
		case "1":
			reservations();
			break;
		case "2":
			showtimes();
			break;
		case "3":
			reviews();
			break;
		case "4":
			manageUsers();
			break;
		case "5":
			initialPrompt();
			break;
		case "6":
			System.out.println("Goodbye!");
			break;
		default:
			System.out.println("Error! Please select again");
			menu(username);
			break;
		}
	}
	
	void reservations() {
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
			reservations();
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
			reservations();
			break;
		case "3":
			sc.nextLine();
			System.out.println("What movie?");
			movTitle = sc.nextLine();
			System.out.println("What showtime?");
			time = sc.nextLine();
			u.cancelReservation(time, movTitle);
			System.out.println("Successfully deleted reservation!");
			reservations();
			break;
		case "4":
			reservations();
			break;
		}
	}
	
	void showtimes() {
		
	}
	
	void reviews() {
		
	}
	
	void manageUsers() {
		
	}
	
	
	

}

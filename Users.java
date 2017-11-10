import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class User{
	
	int uID;
	String username;
	int phoneNumber;
	String email;
	String rewardsNumber;
	Connection conn;
	ResultSet rs;
	
	/**
	 * Instantiates User class and sets variables to relevant information
	 * @param conn Database connection used
	 * @param username User to pull relevant information from database
	 */
	public User(Connection conn, String username) {
		
		this.conn = conn;
		
		try {
		
		PreparedStatement getInfo = conn.prepareStatement("SELECT * FROM User WHERE username = ?");
		getInfo.setString(1, username);
		rs = getInfo.executeQuery();
		
		while(rs.next()) {
			uID = rs.getInt("uID");
			this.username = rs.getString("username");
			phoneNumber = rs.getInt("phoneNumber");
			email = rs.getString("email");
			rewardsNumber = rs.getString("rewardsNumber");
		}
		
		
		}catch(SQLException se) {
			
			System.out.println("SQL Error");
			
		};
	}

	
	/**
	 *  Users can view their reservation within a show
	 */
	public void viewReservation(){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		try {
			
			PreparedStatement viewRes = conn.prepareStatement("SELECT * FROM Reservation WHERE uID = ?");
			viewRes.setInt(1, uID);
			rs = viewRes.executeQuery();
			
			while(rs.next()) {
				int seatNumsRes = rs.getInt("seatNumsRes");
				Date time = rs.getDate("time");
				String movTitle = rs.getString("movTitle");
				System.out.println("Movie: " + movTitle);
				System.out.println("Showtime: " + dateFormat.format(time));
				System.out.println("Seats Reserved: " + seatNumsRes);
			}
			
			
			}catch(SQLException se) {
				
				System.out.println("view Reservation Error");
				
			};
		
		

	}

	/**
	 * Users can add a seat within a show
	 * @param seatNumsRes Number of seats being reserved
	 * @param time Time of movie being reserved
	 * @param movTitle Title of movie being reserved
	 */
	public void addReservation(int seatNumsRes, String time, String movTitle){
		
		
		try {
			
			PreparedStatement viewRes = conn.prepareStatement("INSERT INTO Reservation(uID, seatNumsRes,time, movTitle) VALUES (?,?,?,?)");
			viewRes.setInt(1, uID);
			viewRes.setInt(2, seatNumsRes);
			viewRes.setTimestamp(3, java.sql.Timestamp.valueOf(time));
			viewRes.setString(4, movTitle);
			viewRes.executeUpdate();
			
			
			}catch(SQLException se) {
				
				System.out.println("add Reservation Error");
				
			};

			

	}

	/**
	 * Users can cancel a specified seat for a show.
	 * @param time Time of movie being deleted
	 * @param movTitle Title of movie being deleted
	 */
	public void cancelReservation(String time, String movTitle){
		
		try {
			
			PreparedStatement viewRes = conn.prepareStatement("DELETE FROM Reservation WHERE uID = ? AND time = ? AND movTitle = ?");
			viewRes.setInt(1, uID);
			viewRes.setTimestamp(2, java.sql.Timestamp.valueOf(time));
			viewRes.setString(3, movTitle);
			viewRes.executeUpdate();
			
			
			}catch(SQLException se) {
				
				System.out.println("cancel Reservation Error");
				
			};

	}

	/* Users can modify a seat within a show */
	public void modifyReservation(int seatNumsRes, String time, String movTitle){

	}

	/* Users can search the database for a movie to find the times it is being played. */
	public void viewAllShowtimes(){

	}

	/* Users can search for the movies that are playing for a certain time period (i.e., movies playing a certain week). */
	public void viewShowtimes(String movTitle){

	}

	/* Users can obtain the movie review with the corresponding rating through search. */
	public void showRatingReview(int stars){

	}

	/* Users can add reviews for a specified movie. */
	public void addReview(String movTitle, int rating, String review){

	}

	/* Users can remove their review for a specified movie. */
	public void removeReview(String movTitle){

	}

	





}
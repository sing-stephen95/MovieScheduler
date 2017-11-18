package finalproject;

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
			
			String sql = "INSERT INTO Reservation(uID, seatNumsRes,time, movTitle) VALUES (?,?,?,?)";
			PreparedStatement addRes = conn.prepareStatement(sql);
			addRes.setInt(1, uID);
			addRes.setInt(2, seatNumsRes);
			addRes.setTimestamp(3, java.sql.Timestamp.valueOf(time));
			addRes.setString(4, movTitle);
			addRes.executeUpdate();
			
			
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
			
			String sql = "DELETE FROM Reservation WHERE uID = ? AND time = ? AND movTitle = ?";
			PreparedStatement cancelRes = conn.prepareStatement(sql);
			cancelRes.setInt(1, uID);
			cancelRes.setTimestamp(2, java.sql.Timestamp.valueOf(time));
			cancelRes.setString(3, movTitle);
			cancelRes.executeUpdate();
			
			
			}catch(SQLException se) {
				
				System.out.println("cancel Reservation Error");
				
			};

	}

	/**
	 * Users can modify a seat within a show.
	 * @param seatNumRes Number of seats being reserved
	 * @param time Time of movie being deleted
	 * @param movTitle Title of movie being deleted
	 */
	public void modifyReservation(int seatNumsRes, String time, String movTitle){
		
		try {
			
			String sql = "UPDATE Reservation SET seatNumsRes = ? WHERE uID = ? AND time = ? AND movTitle = ?";
			PreparedStatement modRes = conn.prepareStatement(sql);
			modRes.setInt(1, uID);
			modRes.setInt(2, seatNumsRes);
			modRes.setTimestamp(3, java.sql.Timestamp.valueOf(time));
			modRes.setString(4, movTitle);
			modRes.executeUpdate();
			
			
		} catch(SQLException se) {
			
			System.out.println("modify Reservation Error");
			
		};
	}

	/**
	 * Users can search the database for a movie to find the times it is being played.
	 * @param movTitle Title of movie being searched
	 */
	public void viewShowtimesForMovie(String movTitle){

		try {
			
			String sql = "SELECT time FROM Shows WHERE movTitle = ?";
			PreparedStatement viewTime = conn.prepareStatement(sql);
			viewTime.setString(1, movTitle);
			ResultSet rs = viewTime.executeQuery();
			
			while(rs.next()){
				
		        //Retrieve by column name
				Timestamp time = rs.getTimestamp("time");
				//Display values
		        System.out.println("TIME: " + time);
		        
		      }
			
		}catch(SQLException se) {
				
				System.out.println("View Showtimes for Movie Error");
				
		};
		
	}

	/**
	 * Users can search for the movies that are playing for a certain time period
	 * (i.e., movies playing a certain week)
	 * @param startTime Starting end of the time being searched
	 * @param endTime Ending end of the time being searched
	 */
	public void viewMovieDuringTime(String startTime, String endTime){
	
		try {
			
			String sql = "SELECT movTitle, time FROM Shows WHERE time > ? AND time < ?";
			PreparedStatement viewMovieAndTime = conn.prepareStatement(sql);
			viewMovieAndTime.setTimestamp(1, java.sql.Timestamp.valueOf(startTime));
			viewMovieAndTime.setTimestamp(2, java.sql.Timestamp.valueOf(endTime));
			ResultSet rs = viewMovieAndTime.executeQuery();
			
			while(rs.next()){
				
		        //Retrieve by column name
				String movTitle = rs.getString("movTitle");
				Timestamp time = rs.getTimestamp("time");
				//Display values
				System.out.println("TITLE: " + movTitle);
		        System.out.println("TIME: " + time);
		        
		      }
			
		}catch(SQLException se) {
				
				System.out.println("View Showstimes During Time Error");
				
		};
	}

	/**
	 * Users can obtain the movie review with the corresponding rating through search.
	 * @param stars A specific number of stars being searched
	 */
	public void viewRatingReview(int stars){
		
		try {
			
			String sql = "SELECT movTitle, review FROM Review WHERE rating = ?";
			PreparedStatement viewReview = conn.prepareStatement(sql);
			viewReview.setInt(1, stars);
			ResultSet rs = viewReview.executeQuery();
			
			while(rs.next()){
				
		        //Retrieve by column name
				String movTitle = rs.getString("movTitle");
				String review = rs.getString("review");
				//Display values
				System.out.println("TITLE: " + movTitle);
		        System.out.println("REVIEW: " + review);
		        
		      }
			
		}catch(SQLException se) {
				
				System.out.println("View Rating Review Error");
				
		};
	}

	/**
	 * Users can add reviews for a specified movie.
	 * @param movTitle Title of movie being reviewed
	 * @param rating Rating of movie being reviewed
	 * @param review Review of movie being reviewed
	 */
	public void addReview(String movTitle, int rating, String review){

		try {
			
			String sql = "INSERT INTO Review(movTitle, uID, rating, review) VALUES (?,?,?,?)";
			PreparedStatement addRev = conn.prepareStatement(sql);
			addRev.setString(1, movTitle);
			addRev.setInt(2, uID);
			addRev.setInt(3, rating);
			addRev.setString(4, review);
			addRev.executeUpdate();
			
			}catch(SQLException se) {
				
				System.out.println("Add Review Error");
				
			};

	}

	/**
	 * Users can remove their review for a specified movie.
	 * @param movTitle TItle of movie being removed from Review
	 */
	public void removeReview(String movTitle){
		
		try {
			
			String sql = "DELETE FROM Review WHERE uID = ? AND movTitle = ?";
			PreparedStatement removeRev = conn.prepareStatement(sql);
			removeRev.setInt(1, uID);
			removeRev.setString(2, movTitle);
			removeRev.executeUpdate();
			
			
			}catch(SQLException se) {
				
				System.out.println("Remove Review Error");
				
			};

	}

	
}

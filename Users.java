
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class User{
	
	int uID;
	String username;
	int phoneNumber;
	String email;
	String rewardsNumber;
	Connection conn;
	ResultSet rs;
	boolean isAdmin;
	
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
			if(rs.getInt("admin") == 1) {
				isAdmin = true;
			}
		}
		
		
		}catch(SQLException se) {
			
			System.out.println("SQL Error");
			
		};
	}
	
	boolean isAdmin() {
		if(isAdmin) {
			return true;
		}else {
			return false;
		}
	}

	
	/**
	 *  Users can view their reservation within a show
	 */
	public ArrayList<String[]> viewReservation(){
		
		ArrayList<String[]> reservations = new ArrayList<String[]>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		try {
			
			PreparedStatement viewRes = conn.prepareStatement("SELECT * FROM Reservation WHERE uID = ?");
			viewRes.setInt(1, uID);
			rs = viewRes.executeQuery();
			
			while(rs.next()) {
				String[] entry = new String[3];
				int seatNumsRes = rs.getInt("seatNumsRes");
				Date time = rs.getDate("time");
				String movTitle = rs.getString("movTitle");
				entry[0] = movTitle;
				entry[1] = dateFormat.format(time);
				entry[2] = Integer.toString(seatNumsRes);
				reservations.add(entry);
			}
			
			
			}catch(SQLException se) {
				
				System.out.println("view Reservation Error");
				
			};
			
		return reservations;
		
		

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
	public ArrayList<Timestamp> viewShowtimesForMovie(String movTitle){

		ArrayList<Timestamp> times = new ArrayList<Timestamp>();
		
		try {
			
			String sql = "SELECT time FROM Shows WHERE movTitle = ?";
			PreparedStatement viewTime = conn.prepareStatement(sql);
			viewTime.setString(1, movTitle);
			ResultSet rs = viewTime.executeQuery();
			
			while(rs.next()){
				
		        //Retrieve by column name
				Timestamp time = rs.getTimestamp("time");
				//Display values
		        times.add(time);
		        
			}
			
		}catch(SQLException se) {
				
				System.out.println("View Showtimes for Movie Error");
				
		};
		
		return times;
		
	}

	/**
	 * Users can search for the movies that are playing for a certain time period
	 * (i.e., movies playing a certain week)
	 * @param startTime Starting end of the time being searched
	 * @param endTime Ending end of the time being searched
	 */
	public ArrayList<String[]> viewMovieDuringTime(String startTime, String endTime){
		
		ArrayList<String[]> showtime = new ArrayList<String[]>();
		String[] show = new String[2];
	
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
				//String formattedDate = new SimpleDateFormat("yyyyMMdd").format(date);
				//Display values
				show[0] = movTitle;
				//show[1] = time;
		        
		      }
			
		}catch(SQLException se) {
				
			System.out.println("View Showstimes During Time Error");
				
		};
		
		return showtime;
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
	
	
	/* Users can sort movie reviews based on their rating */
	public void sortReview(String movTitle) {
	
		
	}
		
	/**
	 * Administrators can obtain information about seats for a specified show
	 * (for example, they can obtain seat capacity, and the list of users in that showing). 
	 * 
	 */
	public void showInfo() {
		
		if (isAdmin){
			
			try {
				
				String sql = "SELECT * FROM Shows INNER JOIN Reservation ON Shows.movTitle = Reservation.movTitle";
				Statement showInfo = conn.createStatement();
				ResultSet rs = showInfo.executeQuery(sql);
				
				while(rs.next()){
					
			        //Retrieve by column name
				String movTitle = rs.getString("movTitle");
				int seatsAvail = rs.getInt("seatsAvail");
				int tNum = rs.getInt("tNum");
				int sID = rs.getInt("sID");
				Timestamp time = rs.getTimestamp("time");
				int uID = rs.getInt("uID");
				int seatNumsRes = rs.getInt("seatNumsRes");
				//Display values
				System.out.println("TITLE: " + movTitle);
			        System.out.println("SEATS AVAILABLE: " + seatsAvail);
			        System.out.println("THEATER NUMBER : " + tNum);
			        System.out.println("SID: " + sID);
			        System.out.println("TIME: " + time);
			        System.out.println("UID: " + uID);
			        System.out.println("SEATS RESERVED: " + seatNumsRes);
			        
			      }
				
				
			}catch(SQLException se) {
				
				System.out.println("Show Info Error");
				
			};
		}
	}
	
	
	/**
	 * Administrators can add new show times for a specified movie
	 * @param movTitle Title of movie being added
	 * @param time Show time of movie being added
	 * @param tNum Theater number of movie being added
	 */
	public void addShowtimes(String movTitle, String time, int tNum) {
		
		if (isAdmin){
			
			try {
				
				String sql = "INSERT INTO Show(movTitle, seatsAvail, seatsFull, time, tNum) VALUES(?,(SELECT numSeats FROM Theater WHERE tNum = ?),0,?,?)";
				PreparedStatement addShow = conn.prepareStatement(sql);
				addShow.setString(1, movTitle);
				addShow.setInt(2, tNum);
				addShow.setTimestamp(3, java.sql.Timestamp.valueOf(time));
				addShow.setInt(4, tNum);
				addShow.executeUpdate();
				
			}catch(SQLException se) {
				
				System.out.println("Add Showtimes Error");
				
			};
		}
	}
	
	/**
	 * Administrators can add a movie, given they at least add one show.
	 * @param movTitle Title of movie being added
	 * @param movDescrip Description of movie being added
	 * @param time Show time of movie being added
	 * @param tNum Theater number of movie being added
	 */
	public void addMovie(String movTitle, String movDescrip, String time, int tNum) {
		
		if (isAdmin){
			
			try {
				
				String sql = "INSERT INTO Movie(movTitle, movDescrip) VALUES(?, ?)";
				PreparedStatement addMov = conn.prepareStatement(sql);
				addMov.setString(1, movTitle);
				addMov.setString(2, movDescrip);
				addMov.executeUpdate();
				addShowtimes(movTitle, time, tNum);
				
			}catch(SQLException se) {
				
				System.out.println("Add Movie Error");
				
			};
		}
	}

	
	/**
	 * Administrators can obtain information about a user, specifically
	 * their name, phone number, and rewards number
	 * @param uID Identification of a user being searched
	 */
	public void userInfo(int uID) {
		
		if (isAdmin){
			
			try {
				
				String sql = "SELECT * FROM User WHERE uID = ?";
				PreparedStatement userInfo = conn.prepareStatement(sql);
				userInfo.setInt(1, uID);
				ResultSet rs = userInfo.executeQuery();
				
				while(rs.next()){
					
			        //Retrieve by column name
				String username = rs.getString("username");
				int phoneNumber = rs.getInt("phoneNumber");
				String email = rs.getString("email");
				String rewardsNumber = rs.getString("rewardsNumber");
				//Display values
				System.out.println("USERNAME: " + username);
			        System.out.println("PHONE NUMBER: " + phoneNumber);
			        System.out.println("EMAIL : " + email);
			        System.out.println("REWARDSNUMBER: " + rewardsNumber);
			        
				}
				
			}catch(SQLException se) {
				
				System.out.println("User Info Error");
				
			};
		}
	}
	
	/**
	 * Administrators can obtain the statistics for a specified movie in a given time period.
	 * @param movTitle Title of movie being searched
	 * @param startTime Starting period of time being searched
	 * @param endTime Ending period of time being searched
	 */
	public void statforMovDuringTime(String movTitle, String startTime, String endTime){
		
		if (isAdmin){
			
			try {
				
				String sql = "SELECT mID, movTitle, movDescrip FROM Shows NATURAL JOIN Movie WHERE time > ? AND time < ? AND movTitle = ?";
				PreparedStatement statMov = conn.prepareStatement(sql);
				statMov.setTimestamp(1, java.sql.Timestamp.valueOf(startTime));
				statMov.setTimestamp(2, java.sql.Timestamp.valueOf(endTime));
				statMov.setString(3, movTitle);
				ResultSet rs = statMov.executeQuery();
				
				while(rs.next()){
					
			        //Retrieve by column name
				int mid = rs.getInt("mID");
				String title = rs.getString("movTitle");
				String descrip = rs.getString("movDescrip");
				//Display values
				System.out.println("MID: " + mid);
				System.out.println("TITLE: " + title);
			        System.out.println("TIME: " + descrip);
			        
			      }
				
			}catch(SQLException se) {
					
				System.out.println("Stat for Movie During Time Error");
					
			};
		}
	}
	
	
	/**
	 * Administrators can obtain the statistics for a specified movie in a given time period.
	 * @param movTitle Title of movie being searched
	 * @param startTime Starting period of time being searched
	 * @param endTime Ending period of time being searched
	 */
	public void statforAllMovDuringTime(String startTime, String endTime){
		
		if (isAdmin){
			
			try {
				
				String sql = "SELECT mID, movTitle, movDescrip FROM Shows NATURAL JOIN Movie WHERE time > ? AND time < ?";
				PreparedStatement statAllMov = conn.prepareStatement(sql);
				statAllMov.setTimestamp(1, java.sql.Timestamp.valueOf(startTime));
				statAllMov.setTimestamp(2, java.sql.Timestamp.valueOf(endTime));
				ResultSet rs = statAllMov.executeQuery();
				
				while(rs.next()){
					
			        //Retrieve by column name
				int mid = rs.getInt("mID");
				String title = rs.getString("movTitle");
				String descrip = rs.getString("movDescrip");
				//Display values
				System.out.println("MID: " + mid);
				System.out.println("TITLE: " + title);
			        System.out.println("TIME: " + descrip);
			        
			      }
				
			}catch(SQLException se) {
					
				System.out.println("Stat for All Movie During Time Error");
					
			};
		}
	}
	
}
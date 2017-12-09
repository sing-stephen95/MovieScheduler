import java.sql.*;

public class database {

	Connection conn;
	
	/**
	 * Creates initial connection to database
	 * @param dbAddress Address of connection database
	 * @param user User name for login
	 * @param pass Password for login
	 */
	public database(String dbAddress, String user, String pass) {
		
		try {
		
			conn = DriverManager.getConnection(dbAddress, user, pass);
			checkDatabase();
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		
		}
		
	}
	
	void checkDatabase() {
		try {
			Statement s = conn.createStatement();
			s.executeUpdate("CREATE DATABASE IF NOT EXISTS MovieScheduler");
			s.execute("USE MovieScheduler");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Movie" + 
					"	(" + 
					"		mID INT PRIMARY KEY NOT NULL AUTO_INCREMENT," + 
					"		movTitle VARCHAR(30)," + 
					"		movDescrip VARCHAR(500)" + 
					"	)");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Review" + 
					"   	( " + 
					"   		movTitle VARCHAR(30) REFERENCES MOVIE(movTitle)," + 
					"   	  	uID INT REFERENCES User(UID)," + 
					"   	  	rating INT NOT NULL," + 
					"   	  	review VARCHAR(500)," + 
					"   	  	CHECK(rating<=5 AND rating >=0)," + 
					"   	  	PRIMARY KEY (movTitle, uID)" + 
					"   	)");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS User" + 
					"	( " + 
					"		uID INT PRIMARY KEY NOT NULL AUTO_INCREMENT," + 
					"	  	username VARCHAR(15) UNIQUE," + 
					"	  	phoneNumber INT," + 
					"	  	email VARCHAR(20)," + 
					"		admin TINYINT(1)," +	
					"	  	rewardsNumber VARCHAR(20) UNIQUE" + 
					"	)");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Shows" + 
					"	(" + 
					" 		sID INT PRIMARY KEY NOT NULL AUTO_INCREMENT," + 
					" 	  	movTitle VARCHAR(30) REFERENCES MOVIE(movTitle)," + 
					" 	  	seatsAvail INT," + 
					" 	  	seatsFull INT," + 
					" 	  	time DATETIME," + 
					" 	  	tNum INT REFERENCES THEATER(tNum)" + 
					"	)");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS archivedShows" + 
					"	(" + 
					" 		sID INT PRIMARY KEY NOT NULL AUTO_INCREMENT," + 
					" 	  	movTitle VARCHAR(30) REFERENCES MOVIE(movTitle)," + 
					" 	  	seatsAvail INT," + 
					" 	  	seatsFull INT," + 
					" 	  	time DATETIME," + 
					" 	  	tNum INT REFERENCES THEATER(tNum)" + 
					"	)");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Theater" + 
					" 	( " + 
					"	 	tNum INT PRIMARY KEY NOT NULL AUTO_INCREMENT," + 
					"	 	numSeats INT NOT NULL" + 
					" 	)");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Reservation" + 
					" 	( " + 
					" 		resNum INT PRIMARY KEY NOT NULL AUTO_INCREMENT," + 
					"		uID INT REFERENCES User(UID)," + 
					"		seatNumsRes INT," + 
					"		time DATETIME REFERENCES Shows(time)," + 
					"		movTitle VARCHAR(30) REFERENCES MOVIE(movTitle)" + 
					"	)");
			s.executeUpdate("CREATE TRIGGER IF NOT EXISTS updateShowSeats" + 
					" AFTER INSERT ON Reservation " + 
					"FOR EACH ROW " + 
					"BEGIN " + 
					"UPDATE Shows SET seatsAvail = seatsAvail - new.seatNumsRes WHERE movTitle = new.movTitle; " +
					"UPDATE Shows SET seatsFull = seatsFull + new.seatNumsRes WHERE movTitle = new.movTitle; " + 
					"END;"
					);
			s.executeUpdate("CREATE TRIGGER IF NOT EXISTS updateShowOnDeleteReservation\n" + 
					"AFTER DELETE ON Reservation\n" + 
					"FOR EACH ROW\n" + 
					"	BEGIN\n" + 
					"		UPDATE Shows SET seatsAvail = seatsAvail + old.seatNumsRes" + 
					"		WHERE movTitle = old.movTitle;" + 
					"        " + 
					"		UPDATE Shows SET seatsFull = seatsFull - old.seatNumsRes" + 
					"		WHERE movTitle = old.movTitle;" + 
					"" + 
					"    " + 
					"	END");
			s.executeUpdate("START TRANSACTION;");
			s.executeUpdate("set @N := (now());");
			s.executeUpdate("INSERT INTO archivedShows select * from shows where time < date_sub(@N,INTERVAL 32 DAY);");
			s.executeUpdate("DELETE FROM shows WHERE time < date_sub(@N,INTERVAL 32 DAY);");
			s.executeUpdate("COMMIT;");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Returns a connection variable to database
	 * @return Connection to database
	 */
	Connection getConnection() {
		return conn;
	}
	
	/**
	 * Closes connection to database
	 */
	void closeConnection() {
		try {
			
			conn.close();
		
		} catch (SQLException e) {
		
			e.printStackTrace();
		
		}
	}
}

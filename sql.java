import java.sql.*;

public class sql{

	public static void main(String args[]){

		try{
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MovieScheduler", "root", "");
			User u = new User(conn,"testUser");
			u.viewReservation();
			u.addReservation(1, "2017-01-01 12:30:00" , "Test Movie");
			conn.close();
		
		}catch (SQLException se){

			System.out.println("Error");

		}



	}
}
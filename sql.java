import java.sql.*;

public class sql{

	public static void main(String args[]){

		try{
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
			System.out.println("Error");
		
		}catch (SQLException se){

			System.out.println("Error");

		}



	}

}
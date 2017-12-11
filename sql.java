

import java.sql.Connection;
import java.sql.*;

/*
	To run:
		javac moviescheduler/*.java
		java -classpath .;"Path to connector" moviescheduler.sql
*/
public class sql
{

	public static void main(String args[]){		
		/**try {
			Class.forName("com.mysql.jdbc.Driver");			
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/d1", "root", "aimrocks");
			UI view = new UI(conn);
			view.initialPrompt();
		} catch(SQLException se) {
			System.out.println(se);
		} catch(Exception e) {
			System.out.println(e);
		};**/
	    
		final String dbAddress = "jdbc:mysql://localhost:3306/";
	    final String username = "root";
	    final String password = "aimrocks";

		database d1 = new database(dbAddress,username,password);
		UI view = new UI(d1.getConnection());
		view.initialPrompt();
		d1.closeConnection();


	}
	
	
}
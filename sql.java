public class sql{

	public static void main(String args[]){
		
	    final String dbAddress = "jdbc:mysql://localhost:3306/";
	    final String username = "root";
	    final String password = "";

		database d1 = new database(dbAddress,username,password);
		UI view = new UI(d1.getConnection());
		view.initialPrompt();
		d1.closeConnection();



	}
	
	
}
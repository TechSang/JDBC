import java.sql.SQLException;

/**
 * This class is the main function
 * that can execute all statements
 * 
 * @throws SQLException
 * @author Sang Haotian
 */
public class Main {

	DbInfo myInfo = null;
	DbBuild myBuild = null;
	DbDriver myDriver = null;
	DbFile myFile = null;
	public String dbName = "test"; // University, LSH, Chinook, Northwind

	/**
	 * This function is create build & file & getInfo class and call its functions
	 * to get, backup, create the specific database
	 * 
	 * @throws SQLException
	 */
	private void go() throws SQLException {
		System.out.println("Mission start...");
		
		/* Read all data in the database*/
		myInfo = new DbInfo(dbName); 
		myInfo.getInfo(); 
		
		/* Disposal all data in the database*/
		myDriver = new DbDriver(dbName); 
		myDriver.cTable(); // Disposal the schema of database
		myDriver.cAttributes(); // Disposal the attributes of database

		/* Backup all data in the database*/
		myFile = new DbFile(dbName);
		myFile.createFile();
		
		/* Create a new database to backup*/
		//myBuild = new DbBuild("Backup_"+dbName);
		//myBuild.createTable(); // Create the schema of database
		//myBuild.createItems(); //Insert the attributes in the database

		System.out.println("Processing over");

		myInfo.close();
		//myBuild.close();
	};

	/**
	 * This function is main function and call go() function to start whole program
	 */
	public static void main(String[] args) {
		Main myMain = new Main();
		try {
			myMain.go();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

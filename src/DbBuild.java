import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class can backup all information a new database
 * 
 * @throws SQLException
 * @author Sang Haotian
 */
public class DbBuild extends DbDriver {

	public DbBuild(String _dbName) {
		super(_dbName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This function can create table with relevant Information which include
	 * columns, keys, index and put them in backup database
	 * 
	 * @throws SQLException
	 */
	public void createTable() throws SQLException {
		Statement sqlSt = con.createStatement();
		sqlSt.executeUpdate(tableFile); // Execute the statement of CREATE Table
		System.out.println("Created!\n");

		sqlSt.executeUpdate(indexFile); // Execute the statement of CREATE Index
		sqlSt.close();
	}

	/**
	 * This function can insert all attributes
	 * 
	 * @throws SQLException
	 */
	public void createItems() throws SQLException {
		Statement sqlSt = con.createStatement();
		sqlSt.executeUpdate(attributeFile); // Execute the statement of INSERT attributes
		sqlSt.close();
	}
}

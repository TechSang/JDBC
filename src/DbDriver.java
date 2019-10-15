import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class is a driver to read all information
 * 
 * @throws SQLException
 * @author Sang Haotian
 */
public class DbDriver extends DbInfo {
	public static String tableFile = ""; // Store all information of tables
	public static String indexFile = ""; // Store all information of index
	public static String attributeFile = ""; // Store all information of attributes

	public DbDriver(String _dbName) {
		super(_dbName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This function can create table with relevant Information which include
	 * columns, keys, index and put them in backup database
	 */
	public void cTable() {
		int num = 0;
		Collections.sort(IndexInfo); // Format the Index statements
		for (int i = 0; i < tName.size(); i++) {
			Map<String, String> NField = null;
			String columns = "";
			NField = mapField.get(tName.get(i));
			for (@SuppressWarnings("unused") String key : NField.keySet()) { // Create statement of Values
				columns = columns + ColName.get(num) + " " + NField.get(ColName.get(num)) + ",\r\n	";
				num++;
			}
			columns = columns.substring(0, columns.length() - 4);

			String cPK = "	PRIMARY KEY " + "(" + PKName.get(tName.get(i)) + ")"; // Create statement of Primary Key
			String cFK = FKName.get(tName.get(i)); // Create statement of Foreign Key
			String cTable = "CREATE TABLE IF NOT EXISTS '" + tName.get(i) + "' (\r\n	" + columns + ", \r\n" + cPK
					+ cFK + "\r\n);"; // Create statement of table
			System.out.println(cTable);

			tableFile += cTable + "\r\n";
		}

		/* Create the statement of CREATE Index */
		for (int j = 0; j < IndexInfo.size(); j++) {
			String cIndex = "";
			cIndex = IndexInfo.get(j);
			System.out.println(cIndex);

			indexFile += cIndex + "\r\n";
		}
	}

	/**
	 * This function is a driver to insert all attributes in backup database
	 */
	public void cAttributes() {
		for (int i = 0; i < tName.size(); i++) {
			cItems(tName.get(i));
		}
	}

	/**
	 * This function can insert all attribute in specific table
	 */
	public void cItems(String tableName) {
		List<String> value = items.get(tableName);
		String update = "";

		/* Create the statement of INSERT Attributes */
		for (int i = 0; i < value.size(); i++) {
			update = "INSERT INTO " + tableName + " VALUES (" + value.get(i) + ");\r\n";
			//System.out.println(update);
			attributeFile += update;
		}
		//System.out.println();
	}
}

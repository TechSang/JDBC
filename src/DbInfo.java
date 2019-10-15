import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class can catch all information in the database
 * 
 * @throws SQLException
 * @author Sang Haotian
 */
public class DbInfo extends DbBasic {
	public DbInfo(String _dbName) {
		super(_dbName);
		// TODO Auto-generated constructor stub
	}

	public DatabaseMetaData dbmd = null;
	public static List<String> tName = new ArrayList<String>(); // Store Table Name
	public static List<String> ColName = new ArrayList<String>(); // Store Column Name
	public static Map<String, Map<String, String>> mapField = new HashMap<String, Map<String, String>>(); // Store Name & Type in specific table
	public static Map<String, String> field = new HashMap<String, String>(); // Store Name & Type
	public static Map<String, String> PKName = new HashMap<String, String>(); // Store Primary Key Name
	public static Map<String, String> FKName = new HashMap<String, String>(); // Store Foreign Key Name
	public static List<String> IndexInfo = new ArrayList<String>(); // Store Index Information
	public static Map<String, List<String>> items = new HashMap<String, List<String>>();// Store Attributes in the table
	int indexNum = 0;

	/**
	 * This function is a driver that running to get all information
	 * 
	 * @throws SQLException
	 * @author Sang Haotian
	 */
	public void getInfo() throws SQLException {
		dbmd = con.getMetaData();
		getTable();
		for (int i = 0; i < tName.size(); i++) {
			System.out.println(tName.get(i));
			getColumn(tName.get(i));
			getKeys(tName.get(i));
			//getIndex(tName.get(i));
			getItems(tName.get(i));
			System.out.println("-----------------------------------------------");
		}
	}

	/**
	 * This function is get the table Info from the request database and store it in
	 * the tName
	 * 
	 * @throws SQLException
	 */
	public void getTable() throws SQLException {
		String[] types = { "TABLE" };
		ResultSet rsTable = dbmd.getTables(null, null, "%", types);

		while (rsTable.next()) {
			String tableName = rsTable.getString("TABLE_NAME"); // Get the table name
			tName.add(tableName);
		}
	}

	/**
	 * This function is get the columns Info from the request table and store it in
	 * the colName, field
	 * 
	 * @throws SQLException
	 */
	public void getColumn(String tableName) throws SQLException {
		ResultSet rsColumn = dbmd.getColumns(null, "%", tableName, "%");
		String nullValue = " not null";
		while (rsColumn.next()) {
			String cname = rsColumn.getString("COLUMN_NAME"); // Get the column name
			String ctype = rsColumn.getString("TYPE_NAME"); // Get the data type in the column
			int nullAble = rsColumn.getInt("NULLABLE"); // Get the uniqueness
			System.out.println(cname + "," + ctype + "," + nullAble);
			if (nullAble == 0) // Check the uniqueness
				ctype = ctype + nullValue;
			field.put(cname, ctype);
			ColName.add(cname);
		}
		System.out.println(tableName + "=" + field);
		mapField.put(tableName, field);
		field = new HashMap<String, String>();
	}

	/**
	 * This function is get the primary key and foreign key from the request table
	 * and store it in the PKName, FKName
	 * 
	 * @throws SQLException
	 */
	public void getKeys(String tableName) throws SQLException {
		ResultSet rsPrimaryKey = dbmd.getPrimaryKeys(null, "%", tableName);
		String columnName = "";
		String foreignKey = "";
		while (rsPrimaryKey.next()) {
			columnName += ", " + rsPrimaryKey.getString("COLUMN_NAME"); // Get the column that is Primary Key
			// String pkName = rsPrimaryKey.getString("PK_NAME"); // Get the name of Primary Key
		}
		if (columnName.length() != 0) // Format the statements
			columnName = columnName.substring(2, columnName.length());
		PKName.put(tableName, columnName);
		System.out.println("PrimaryKey: " + columnName);

		ResultSet rsForeignKey = dbmd.getImportedKeys(null, "%", tableName);
		while (rsForeignKey.next()) {
			String pkTableName = rsForeignKey.getString("PKTABLE_NAME"); // Get the name of relevant table
			String fkName = rsForeignKey.getString("FKCOLUMN_NAME"); // Get the name of Foreign Key
			String pkName = rsForeignKey.getString("PKCOLUMN_NAME");// Get the name of relevant Primary Key
			foreignKey += ",\r\n	FOREIGN KEY " + "(" + fkName + ")" + " REFERENCES " + pkTableName + "(" + pkName + ")";
		}
		FKName.put(tableName, (foreignKey));
		System.out.println("ForeignKey:" + foreignKey);
	}

	/**
	 * This function is get the index from the request table and store it in the
	 * IndexInfo which has UNIQUE and no-UNIQUE and can detect "sqlite_autoindex"
	 * 
	 * @throws SQLException
	 */
	/*public void getIndex(String tableName) throws SQLException {
		ResultSet rsIndex = dbmd.getIndexInfo(null, null, tableName, true, true);
		String index = "";

		while (rsIndex.next()) {
			if (rsIndex.getString("INDEX_NAME").indexOf("sqlite_autoindex") == -1) {
				String uniqueness = "";
				if (rsIndex.getInt("NON_UNIQUE") == 0) // Check the uniqueness
					uniqueness = "UNIQUE ";
				String indexName = rsIndex.getString("INDEX_NAME"); // Get the name of Index
				String columnName = rsIndex.getString("COLUMN_NAME"); // Get the name of column that related toIndex
				String indexOrder = "";
				if (rsIndex.getString("ASC_OR_DESC") != null) // Check the sequence
					indexOrder = " " + rsIndex.getString("ASC_OR_DESC");
				index = "CREATE " + uniqueness + "INDEX " + indexName + " ON " + tableName + " (" + columnName
						+ indexOrder + ");\n";
				if (indexNum > 0) { // Format the statements
					if (IndexInfo.get(indexNum - 1).indexOf(indexName) != -1) {
						indexNum = indexNum - 1;
						index = IndexInfo.get(indexNum).substring(0, IndexInfo.get(indexNum).length() - 3) + ", "
								+ columnName + indexOrder + ");\n";
						IndexInfo.remove(indexNum);
					}
				}
				IndexInfo.add(index);
				System.out.println(index);
				indexNum++;
			}
		}
	}*/

	/**
	 * This function is get the attributes from the request table and store it in
	 * the request form
	 * 
	 * @throws SQLException
	 */
	public void getItems(String tableName) throws SQLException {
		String query = "SELECT * FROM " + tableName;
		List<String> attributes = new ArrayList<String>();
		Statement sqlSt = null;

		sqlSt = con.createStatement();
		ResultSet rsItems = sqlSt.executeQuery(query);
		ResultSetMetaData metaData = rsItems.getMetaData();

		while (rsItems.next()) {
			String attr = "";
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				String str = rsItems.getString(i + 1); // Get the attributes in the table
				if (str != null)
					if (!str.matches("[0-9]+")) // Store number and text in different type
						str = "\"" + str + "\"";
				attr += str + ", ";
			}
			attr = attr.substring(0, attr.length() - 2); // Format the statements
			attributes.add(attr);
			System.out.println(attr);
		}
		items.put(tableName, attributes);
		rsItems.close();
		sqlSt.close();
	}
}

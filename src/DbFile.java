import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to store all information to file
 * 
 * @author Sang Haotian
 */
public class DbFile extends DbDriver {
	private String fileName = null;
	private String filePath = "C:/Users/Teacher.Sang/Documents/JavaWorkSpace/JDBC3/DBFiles/";
	public String fileInfo = null;

	public DbFile(String _dbName) {
		super(_dbName);
		fileName = filePath + _dbName + ".txt";
		// TODO Auto-generated constructor stub
	}

	/**
	 * This function creates the file in requested path and store information to it
	 */
	public void createFile() {
		File myFilePath = new File(fileName);
		try {
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			fileInfo = tableFile + "\r\n" + indexFile + "\r\n" + attributeFile;
			writeFile(fileInfo); // Call the write method
		} catch (Exception e) {
			System.out.println("Create file error!");
			e.printStackTrace();
		}
	}

	/**
	 * This function stores information to file
	 */
	public void writeFile(String data) {
		try {
			FileWriter fw = new FileWriter(fileName);
			fw.write(data);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

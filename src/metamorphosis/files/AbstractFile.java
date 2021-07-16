package metamorphosis.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract class AbstractFile
 * Contains the "kernel" of info of a file
 * Implements the methods of FileActions, which will be Overridden
 */
public abstract class AbstractFile implements FileActions {

	protected File originFile;

	protected boolean doAction;
	
	protected String workDirectory;

	/**
     * Constructor
     * @param File originFile
	 * Saves the file in the field
     */
	public AbstractFile(File originFile) {
		this.originFile = originFile;
	}

	// These 3 methods will be overridden
	public void action() {
		return;
	}

	public void doAction() {
		return;
	};

	public void revertAction() {
		return;
	}

	// UTILS
	/**
	 * Returns the files file extension
	 * @return returns the file directory 
	 * Example: path = /Home/Desktop/OOP/file.txt
	 *         extension = txt
	 */
	public String getFileExtension() {
		String extension = "";
		int index = originFile.getName().lastIndexOf('.');
		if (index > 0) {
			extension = originFile.getName().substring(index + 1);
		}
		return extension;
	}

	/**
	 * @return returns the file directory 
	 * Example: path = /Home/Desktop/OOP/file.txt
	 *         return parentPath = /Home/Desktop/OOP
	 */
	public String getFilePath() {
		String parentPath = this.originFile.getAbsoluteFile().getParent();
		return parentPath;
	}

	/**
	 * @return returns the filename without extension 
	 * Example: fullFileName = file.txt 
	 * 			return str = file
	 */
	public String getFileNameWithoutExt() {
		String str = originFile.getName();
		// Handle null case specially.
		if (str == null)
			return null;
		// Get position of last '.'.
		int pos = str.lastIndexOf(".");
		// If there wasn't any '.' just return the string as is.
		if (pos == -1)
			return str;
		// Otherwise return the string, up to the dot.
		return str.substring(0, pos);
	}

	/**
	 * Creates a folder from a given path 
	 * @param String folderName
	 * Example: 
	 * 		filePath = /Home/Desktop
	 * 		folderName = "NewFolder" 
	 * 		create /Home/Desktop/NewFolder 
	 * 		New folder gets created
	 */
	public String newFolder(String folderName) {
		String newFolder = getFilePath() + "/" + folderName;
		Path path = Paths.get(newFolder);
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Directory created");
		} else {
			System.out.println("Directory already exists");
		}
		return newFolder;
	}

	// No setter because the file should not change once the object is created
	public File getOriginFile() {
		return originFile;
	}

}

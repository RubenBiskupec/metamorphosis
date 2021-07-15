package metamorphosis.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractFile implements FileActions {

	protected File originFile;

	protected boolean doAction;
	
	protected String workDirectory;

	public AbstractFile() {
		// noop
	}

	public AbstractFile(File originFile) {
		this.originFile = originFile;
	}

	public void action() {
		// guess form file extension name what to do
		if (doAction) {
			doAction();
		} else {
			revertAction();
		}
	}

	public void doAction() {
		return;
	};

	public void revertAction() {
		return;
	}

	// UTILS

	public String getFileExtension() {
		String extension = "";
		int index = originFile.getName().lastIndexOf('.');
		if (index > 0) {
			extension = originFile.getName().substring(index + 1);
		}
		return extension;
	}

	/**
	 * @param path filepath of given file
	 * @return returns the file directory Example: path = /Home/Desktop/OOP/file.txt
	 *         parentPath = /Home/Desktop/OOP
	 */
	public String getFilePath() {
		String parentPath = this.originFile.getAbsoluteFile().getParent();
		return parentPath;
	}

	/**
	 *
	 * @param fullFileName
	 * @return returns the filename without extension 
	 * Example: fullFileName = file.txt 
	 * 			str = file
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
	 * Creates a folder from a given path Example: str = /Home/Desktop/NewFolder -->
	 * New folder gets created
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

	public File getOriginFile() {
		return originFile;
	}

	public void setOriginFile(File originFile) {
		this.originFile = originFile;
	}

}

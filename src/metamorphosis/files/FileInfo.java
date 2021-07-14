package metamorphosis.files;

public class FileInfo {
	
	public enum Mode {
		ZIP,
		SPLIT,
		CRYPT
	}
	
	public enum Split {
		NUMBER,
		SIZE
	}
	
	private int numberOfParts;
	private int sizeOfFile;
	private String password;
	
	public FileInfo() {
		// noop
	}
	
	private Mode currentMode;
	public Mode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
	}

	public int getNumberOfParts() {
		return numberOfParts;
	}

	public void setNumberOfParts(int numberOfParts) {
		this.numberOfParts = numberOfParts;
	}

	public int getSizeOfFile() {
		return sizeOfFile;
	}

	public void setSizeOfFile(int sizeOfFile) {
		this.sizeOfFile = sizeOfFile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

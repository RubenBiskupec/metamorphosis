package metamorphosis.files;

import java.io.File;

public class Crypter extends AbstractFile {
	
	private String password;

	public Crypter () {
		super();
	}
	
	public Crypter (File originFile, String password) {
		super(originFile);
		this.password = password;
	}
	
	// Crypt
	@Override 
	public void doAction () {
		// TODO 
		// Encrypt
	}
	
	// Decrypt
	public void revertAction () {
		// TODO 
		// Decrypt
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getMode() {
		return "CRYPT";
	}

}

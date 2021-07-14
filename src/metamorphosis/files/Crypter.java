package metamorphosis.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Crypter extends AbstractFile {

	private String password;

	public Crypter() {
		super();
	}

	public Crypter(File originFile, String password) {
		super(originFile);
		this.password = password;
	}

	@Override
	public void action() {
		System.out.println("Crypt Action on file: " + originFile.getName() + " and directory: " + originFile.getPath());
		String fileExtension = getFileExtension();
		System.out.println("File Extension: " + fileExtension);

		if (fileExtension.contentEquals("c")) {
			workDirectory = getFilePath();
			revertAction();
		} else {
			workDirectory = newFolder("crypt");
			doAction();
		}
	}

	// Crypt
	@Override
	public void doAction() {
		System.out.println("Encrypt");
		try {
			encrypt();
		} catch (InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	public void encrypt() throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, IOException {
		FileInputStream fis = new FileInputStream(originFile);
		File encryptedFile = new File(workDirectory + "/" + originFile.getName() + ".c");
		FileOutputStream fos = new FileOutputStream(encryptedFile);
		// Password has to have at least 8 bytes
		// Convert string to arr
		DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());

		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = skf.generateSecret(desKeySpec);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		write(cis, fos);
	}

	// Decrypt
	public void revertAction() {
		System.out.println("Decrypt");
		try {
			decrypt();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	public void decrypt () throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException {
		FileInputStream fis = new FileInputStream(originFile);
		File encryptedFile = new File(workDirectory + "/" + getFileNameWithoutExt());
		FileOutputStream fos = new FileOutputStream(encryptedFile);
		// Password has to have at least 8 bytes
		// Convert string to arr
		DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());

		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = skf.generateSecret(desKeySpec);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		
		cipher.init(Cipher.DECRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        write(fis, cos);
	}

	private static void write(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int nBytesRead = 0;

		while ((nBytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, nBytesRead);
		}
		out.close();
		in.close();
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

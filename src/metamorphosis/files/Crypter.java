package metamorphosis.files;

import java.io.File;
import java.io.FileInputStream;
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

	/**
	 * Zipper constructor
	 * Takes an additional parameter in the constructor
	 * @param String password
	 * password has no getters and setters for privacy reasons
	 */
	public Crypter(File originFile, String password) {
		super(originFile);
		this.password = password;
	}

	/**
	 * Checks the file extension
	 * Calls method to zip file if the extension is different than "c"
	 */
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

	/**
	 * Creates the new folder if necessary
	 * Encrypts the file with the given password
	 */
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

	public void revertAction() {
		System.out.println("Decrypt");
		try {
			decrypt();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the new folder if necessary
	 * Decrypts the file with the given password
	 */
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

	/**
	 * Writes on file
	 * @param InputStream in
	 * @param OutputStream out
	 * Extracted because it is shared by both encrypt and decrypt
	 */
	private static void write(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int nBytesRead = 0;
		while ((nBytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, nBytesRead);
		}
		out.close();
		in.close();
	}

	@Override
	public String getMode() {
		return "CRYPT";
	}

}

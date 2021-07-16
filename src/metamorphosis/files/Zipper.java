package metamorphosis.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
 import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Zipper class
 * Depending on the file extension, it zips or unzips the file
 * Extesion of Abstract file
 */
public class Zipper extends AbstractFile {

	/**
	 * Zipper constructor
	 */
	public Zipper(File originFile) {
		super(originFile);
	}

	/**
	 * Checks the file extension
	 * Calls methos to zip file if the extension is different than "z"
	 */
	@Override
	public void action() {
		System.out.println("Zip Action on file: " + originFile.getName() + " and directory: " + originFile.getPath());
		String fileExtension = getFileExtension();
		System.out.println("File Extension: " + fileExtension);	
		if (fileExtension.contentEquals("z")) {
			workDirectory = getFilePath();
			revertAction();
		} else {
			workDirectory = newFolder("zip");
			doAction();
		}
	}

	@Override
	public void doAction() {
		System.out.println("Zip");
		zip();
	}
	
	/**
	 * Creates the new folder if necessary
	 * Zips the file
	 */
	private void zip () {
		try {
			FileInputStream fis = new FileInputStream(originFile);
			FileOutputStream fos = new FileOutputStream(workDirectory + "/" + originFile.getName() + ".z");
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			int nReadBytes = 0;
			byte[] buffer = new byte[1024];
			while ((nReadBytes = fis.read(buffer)) != -1) {
				gzos.write(buffer, 0, nReadBytes);
			}
			gzos.finish();
			gzos.close();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void revertAction() {
		System.out.println("Unzip");
		unzip();
	}
	
	/**
	 * Creates the new folder if necessary
	 * Unzips
	 */
	private void unzip () {
		try {
			FileInputStream fis = new FileInputStream(originFile);
			FileOutputStream fos = new FileOutputStream(workDirectory + "/" + getFileNameWithoutExt());
            GZIPInputStream gzis = new GZIPInputStream(fis);
            int nReadBytes = 0;
            byte[] buffer = new byte[1024];
            while ((nReadBytes = gzis.read(buffer)) != -1) {
                fos.write(buffer, 0, nReadBytes);
            }
            gzis.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public String getMode() {
		return "ZIP";
	};

}

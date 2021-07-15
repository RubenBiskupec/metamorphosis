package metamorphosis.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Splitter extends AbstractFile {
	
	protected int bufferSize = 8000;
    protected byte[] buffer = new byte [bufferSize];

	public Splitter() {
		super();
	}

	public Splitter(File originFile) {
		super(originFile);
	}
	
//	@Override
//	public void action () {
//		System.out.println("Split Action on file: " + originFile.getName() + " and directory: " + originFile.getPath());
//		String fileExtension = getFileExtension();
//		System.out.println("File Extension: " + fileExtension);
//		
//		if (fileExtension.contentEquals("1")) {
//			workDirectory = getFilePath();
//			revertAction();
//		} else {
//			workDirectory = newFolder("split");
//			doAction();
//		}
//	}

	@Override
	public void revertAction() {
		try {
			merge();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void merge() throws IOException {
		int x = 1;
		int length = 0;

		FileOutputStream fos = new FileOutputStream(workDirectory + "/" + getFileNameWithoutExt());;
		File currentFile = originFile;
		FileInputStream fis = null;

		while (currentFile.exists()) {		
			currentFile = new File(workDirectory + "/" + getFileNameWithoutExt() + "." + x);
			if (currentFile.exists()) {
				System.out.println("Merging part " + x);
				fis = new FileInputStream(currentFile);		
				while ((length = fis.read(buffer, 0, buffer.length)) >= 0) {
					fos.write(buffer, 0, length);
				}
				fis.close();				
			} // else break while loop		
			x++;			
		}
		fis.close();
		fos.close();
		System.out.println("Merge done");
	}

	@Override
	public String getMode() {
		return "SPLIT";
	}
}

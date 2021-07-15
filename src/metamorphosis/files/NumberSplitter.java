package metamorphosis.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NumberSplitter extends Splitter {
	
	private int numberOfParts;
	
	public NumberSplitter () {
		super();
	}
	
	public NumberSplitter (File originFile, int numberOfParts) {
		super(originFile);
		this.numberOfParts = numberOfParts;
	}
	
	@Override
	public void action () {
		System.out.println("Split Action on file: " + originFile.getName() + " and directory: " + originFile.getPath());
		String fileExtension = getFileExtension();
		System.out.println("File Extension: " + fileExtension);
		
		if (fileExtension.contentEquals("1")) {
			workDirectory = getFilePath();
			revertAction();
		} else {
			workDirectory = newFolder("num");
			doAction();
		}
	}
	
	@Override
	public void doAction () {
		try {
			split();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void split() throws IOException {
		int chunkSize = (int) (originFile.length() / numberOfParts);
		System.out.println("File has " + originFile.length() + " bytes");
		System.out.println(originFile.length() + " / " + numberOfParts + " = " + chunkSize);
		
		FileInputStream fis = new FileInputStream(originFile);
		int x = 1;
		while (fis.available() != 0) {
            int i = 0;
            String t = workDirectory + "/"+ originFile.getName() + "." + x;
            FileOutputStream fos = new FileOutputStream(t);
            while ((i <= chunkSize) && (fis.available() != 0) )
            {
                int readBytes = fis.read(buffer);
                i = i + readBytes; // Counts the number of bytes read
                fos.write(buffer); // Writes the buffer in the new memory              
            }
            fos.close();
            System.out.println("Part " + x +" created");
            x++;
        } // End of outer while loop
        System.out.println("File splitted successfully");
        fis.close();
	}

	@Override
	public String getMode() {
		return "NUMBER";
	}
}

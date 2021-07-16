package metamorphosis.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Size splitter class
 * Extension of Splitter, which implements the "merge" method
 */
public class SizeSplitter extends Splitter {
	
	private int chunkSize;
	
	/**
	 * Constructor
	 * The extra parameter is chuckSize, used to determine the 
	 * size of new files to be generated
	 */
	public SizeSplitter(File originFile, int chunkSize) {
		super(originFile);
		this.chunkSize = chunkSize;
	}
	
	/**
	 * Implementation of split
	 * Splits the file in multiple files of size = chunkSize
	 */
	@Override
	protected void split() throws IOException {
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
	
}

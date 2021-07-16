package metamorphosis.files;

import java.io.File;

/**
 * Number splitter class
 * Extension of Size Splitter, which implements the "split" method
 */
public class NumberSplitter extends SizeSplitter {
	
	/**
	 * Constructor
	 * The extra parameter is numberOfParts, 
	 * which gets converted in chunkSize depending by the file length in the super call
	 */
	public NumberSplitter (File originFile, int numberOfParts) {
		// calculate size from number of files and pass it to SizeSplitter
		super(originFile, (int) (originFile.length() / numberOfParts));	
	}
}

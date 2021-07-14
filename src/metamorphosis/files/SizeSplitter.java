package metamorphosis.files;

import java.io.File;

public class SizeSplitter extends Splitter {
	
	private int sizeOfFile;
	
	public SizeSplitter () {
		super();
	}
	
	public SizeSplitter(File originFile, int sizeOfFile) {
		super(originFile);
		this.sizeOfFile = sizeOfFile;
	}
	
	@Override
	public void doAction () {
		// TODO split
		
	}
	
	@Override
	public String getMode() {
		return "SIZE";
	}
	
}

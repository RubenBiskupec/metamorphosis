package metamorphosis.files;

import java.io.File;

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
	public void doAction () {
		// TODO 
		// Split
	}

	@Override
	public String getMode() {
		return "NUMBER";
	}
}

package metamorphosis.files;

import java.io.File;

public class Splitter extends AbstractFile {
	
	public Splitter () {
		super();
	}
	
	public Splitter (File originFile) {
		super(originFile);
	}
	
	@Override
	public void revertAction () {
		// TODO 
		// merge
	}

	@Override
	public String getMode() {
		return "SPLIT";
	}
}

package metamorphosis.files;

import java.io.File;

public class Zipper extends AbstractFile {

	public Zipper () {
		
	}
	
	public Zipper (File originFile) {
		super(originFile);
	}

	// Zip
	@Override
	public void doAction() {
		// TODO
		// Zip
	}

	// Zip
	@Override
	public void revertAction() {
		// TODO
		// Zip
	}
	
	@Override
	public String getMode () {
		return "ZIP";
	};

}

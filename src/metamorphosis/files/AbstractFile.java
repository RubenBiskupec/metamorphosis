package metamorphosis.files;

import java.io.File;

public abstract class AbstractFile implements FileActions {
	
	
	protected File originFile;

	protected boolean doAction;	
	
	public AbstractFile () {
		// noop
	}
	
	public AbstractFile (File originFile) {
		this.originFile = originFile;
	}
	
	public void action () {
		// guess form file extension name what to do
		if (doAction) {
			doAction();
		} else {
			revertAction();
		}
	}
	
	public void doAction() {
		return;
	};
	
	public void revertAction() {
		return;
	}
	
	public File getOriginFile() {
		return originFile;
	}

	public void setOriginFile(File originFile) {
		this.originFile = originFile;
	}

}

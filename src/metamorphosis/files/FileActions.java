package metamorphosis.files;

/**
 * Interface used by all the extensions of AbstractFile
 */
public interface FileActions {
	
	/**
	 * Used to choose if the file is to be split/merged stc. 
	 * Check the file extension and then
	 * call doAction or revertAction depending on the result
	 */
	public void action();
	
	/**
	 * It will call the implementation of split/encryot/zip
	 */
	public void doAction ();
	
	/**
	 * It will call the implementation of merge/decrypt/unzip
	 */
	public void revertAction ();
	
	/**
	 * Returns the mode of action:
	 * SPLIT, ZIP, CRYPT
	 */
	public String getMode ();

}

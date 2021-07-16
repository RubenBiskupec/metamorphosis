package metamorphosis.gui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import metamorphosis.files.AbstractFile;

/**
 * Table Data Model Class
 */
public class TableModel extends DefaultTableModel {
	
	private Vector<AbstractFile> queue;
	String[] tableColumnNames = {"Filename", "Mode", "Size (bytes)"};


	/**
     * Constructor
     * @param columnNames is the Vector with columns names
     * @param rowCount
     * @Param queue is the Vector containing the files data
     */
	public TableModel(Object[] columnNames, int rowCount, Vector<AbstractFile> queue) {
		super(columnNames, rowCount);
		this.queue = queue;
	}
	
	/**
	 * Returns the value of the given cell
     */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AbstractFile file = queue.elementAt(rowIndex);
        switch (columnIndex){
            case 0: 
            	return file.getOriginFile().getName();
            case 1:
            	return file.getMode();
            case 2: 
            	return (int) file.getOriginFile().length();
            default: 
            	return null;
        }
    }
}

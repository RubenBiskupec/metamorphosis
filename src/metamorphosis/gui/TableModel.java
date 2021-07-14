package metamorphosis.gui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import metamorphosis.files.AbstractFile;
import metamorphosis.files.Crypter;

public class TableModel extends DefaultTableModel {
	
	private Vector<AbstractFile> queue;
	String[] tableColumnNames = {"Filename", "Mode", "Size (bytes)"};


	public TableModel(Object[] e, int rowCount, Vector<AbstractFile> queue) {
		super(e, rowCount);
		this.queue = queue;
	}
	
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

	@Override
    public int getRowCount() {
        if(queue == null)
            return 0;
        return queue.size();
    }
	
	@Override
    public int getColumnCount() {
        return tableColumnNames.length;
    }
	
	@Override
    public String getColumnName(int columnIndex) {
        return tableColumnNames[columnIndex];
    }
	
	@Override
    public Class<?> getColumnClass(int columnIndex) {
        return tableColumnNames[columnIndex].getClass();
    }
	
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}

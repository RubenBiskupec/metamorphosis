package metamorphosis.gui;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import metamorphosis.files.AbstractFile;
import metamorphosis.files.Crypter;
import metamorphosis.files.FileInfo;
import metamorphosis.files.NumberSplitter;
import metamorphosis.files.SizeSplitter;
import metamorphosis.files.FileInfo.Mode;
import metamorphosis.files.FileInfo.Split;
import metamorphosis.files.Zipper;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class RootPanel extends JPanel implements ActionListener {

	// TODO forse da togliere
	// INFO FILE DA CREARE

	private Mode currentMode = Mode.ZIP;
	private Split currentSplit = Split.NUMBER;

	private int splitInt = 10;
	private String password = "";
	private AbstractFile currentFile;

	private FileInfo currentFileInfo;

	// LAYOUT
	JRadioButton zipRadio;

	// INFO LAYOUT ALTERNATIVI
	private JLabel passwordLabel1;
	private JPasswordField passwordField;
	private JLabel passwordLabel2;

	private JLabel splitLabel;
	private JRadioButton numberFiles;
	private JRadioButton fileSize;
	private JTextField splitField;

	private JTable queueTable;
	private TableModel tableModel;

	private JButton deleteFilesButton;
	private JButton startButton;

	private static final String ZIP = "Zip/Unzip";
	private static final String SPLIT = "Split/Merge";
	private static final String CRYPT = "Encrypt/Decrypt";

	private static final String NUMBER = "N Files";
	private static final String SIZE = "Size X (bytes)";

	// I use vector because it's the parameter required in the JTable
	// Also vectors are synchronized so they are preferred in multi-threaded
	// applications
	// If I want to add threads I won't have to change much code
	// It is slower than ArrayList but that is a tradeoff I have to take
	// The other option was to use a Queue (Linked List)
	private Vector<AbstractFile> fileQueue = new Vector<>();

	public RootPanel() {
		super();
		// Zip (default), Split, Merge selection
		Box rowBox = Box.createHorizontalBox();
		add(rowBox);
		zipRadio = new JRadioButton(ZIP);
		JRadioButton splitRadio = new JRadioButton(SPLIT);
		JRadioButton cryptRadio = new JRadioButton(CRYPT);

		ButtonGroup selectionGroup = new ButtonGroup();
		selectionGroup.add(zipRadio);
		selectionGroup.add(splitRadio);
		selectionGroup.add(cryptRadio);
		// set default as zip
		zipRadio.setSelected(true);

		rowBox.add(zipRadio);
		rowBox.add(splitRadio);
		rowBox.add(cryptRadio);

		// Register a listener for the buttons
		zipRadio.addActionListener(this);
		splitRadio.addActionListener(this);
		cryptRadio.addActionListener(this);

		Box box = Box.createVerticalBox();
		add(box);

		// Password Option
		passwordLabel1 = new JLabel("Type password (at least 8 chars)");
		passwordField = new JPasswordField(20);
		passwordLabel2 = new JLabel("Press ENTER to save it");
		passwordField.addActionListener(new ChangePasswordListener());
		box.add(passwordLabel1);
		box.add(passwordField);
		box.add(passwordLabel2);
		showPasswordOptions(false);

		// Split option

		splitLabel = new JLabel("Type your INT choice and press ENTER");
		add(splitLabel);

		Box splitBox = Box.createHorizontalBox();
		add(splitBox);

		numberFiles = new JRadioButton(NUMBER);
		fileSize = new JRadioButton(SIZE);
		ButtonGroup splitGroup = new ButtonGroup();
		splitGroup.add(numberFiles);
		splitGroup.add(fileSize);
		numberFiles.setSelected(true);
		numberFiles.addActionListener(new ChangeSplitModeListener());
		fileSize.addActionListener(new ChangeSplitModeListener());
		splitField = new JTextField("10", 16);
		splitField.setSize(200, 50);

		splitField.addActionListener(new ChangeSplitFieldListener());
		splitBox.add(numberFiles);
		splitBox.add(fileSize);
		splitBox.add(splitField);
		showSplitOptions(false);

		// Create box
		Box fileBox = Box.createHorizontalBox();
		add(fileBox);

		// Choose files
		JButton addFilesButton = new JButton("Add files");
		addFilesButton.addActionListener(new FileChooserListener());
		fileBox.add(addFilesButton);

		// Delete files
		deleteFilesButton = new JButton("Delete files");
		deleteFilesButton.addActionListener(new DeleteFileListener());
		fileBox.add(deleteFilesButton);
		deleteFilesButton.setVisible(false);

		// Start

		startButton = new JButton("Start");
		startButton.addActionListener(new StartAction());
		fileBox.add(startButton);
		startButton.setVisible(false);

		// Table
		String[] tableColumnNames = { "Filename", "Mode", "Size" };
		tableModel = new TableModel(tableColumnNames, 0, fileQueue);
		queueTable = new JTable(tableModel);
		queueTable.setSize(450, 300);
		JScrollPane scrollPane = new JScrollPane(queueTable);
		add(scrollPane);

		// Progress Bar

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		currentFileInfo = new FileInfo();
		switch (e.getActionCommand()) {

		case SPLIT:
			currentMode = Mode.SPLIT;
			showPasswordOptions(false);
			showSplitOptions(true);
			break;

		case CRYPT:
			currentMode = Mode.CRYPT;
			showPasswordOptions(true);
			showSplitOptions(false);
			break;

		default:
			currentMode = Mode.ZIP;
			showSplitOptions(false);
			showPasswordOptions(false);
			break;
		}
	}

	private class ChangePasswordListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			password = e.getActionCommand();
			System.out.println("Password:");
			System.out.println(password);
		}
	}

	private class ChangeSplitModeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case NUMBER:
				currentSplit = Split.NUMBER;
				break;
			case SIZE:
				currentSplit = Split.SIZE;
				break;
			}
			System.out.println("Current Split:");
			System.out.println(currentSplit);
		}
	}

	private class ChangeSplitFieldListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			splitInt = Integer.parseInt(e.getActionCommand());
			System.out.println("New Split Int" + splitInt);
		}
	}

	private class DeleteFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRows = queueTable.getSelectedRows();
			if (selectedRows.length != 0) {
				for (int i = selectedRows.length - 1; i >= 0; i--) {
					fileQueue.remove(selectedRows[i]);
				}
				tableModel.fireTableDataChanged();
				if (fileQueue.size() == 0) {
					deleteFilesButton.setVisible(false);
					startButton.setVisible(false);
				}
			}
		}
	}

	private class StartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Implement
			System.out.println("Start action");
			
			//Convert
			for (AbstractFile abstractFile : fileQueue) {
				System.out.println(abstractFile.toString());			
				abstractFile.action();
			}
			fileQueue.clear();
			tableModel.fireTableDataChanged();
			resetSelection();
			deleteFilesButton.setVisible(false);
			startButton.setVisible(false);
			
			System.out.println("File queue size: " + fileQueue.size());
		}
	}

	private class FileChooserListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.showOpenDialog(getParent());
			if (fileChooser.showDialog(getParent(), "Confirm selection") == JFileChooser.APPROVE_OPTION) {
				File[] chosenFiles = fileChooser.getSelectedFiles();

				for (File file : chosenFiles) {
					switch (currentMode) {
					case CRYPT:
						Crypter crypterFile = new Crypter(file, password);
						fileQueue.add(crypterFile);
						System.out.println("Adding element to queue");
						break;

					case SPLIT:
						switch (currentSplit) {
						case NUMBER:
							NumberSplitter numberSplitter = new NumberSplitter(file, splitInt);
							fileQueue.add(numberSplitter);
							System.out.println("Adding element to queue");
							break;

						case SIZE:
							SizeSplitter sizeSplitter = new SizeSplitter(file, splitInt);
							fileQueue.add(sizeSplitter);
							System.out.println("Adding element to queue");
							break;
						}
						break;

					default:
						Zipper zipperFile = new Zipper(file);
						fileQueue.add(zipperFile);
						System.out.println("Adding element to queue");
						break;
					}
				}
				resetSelection();
				tableModel.fireTableDataChanged();
				if (fileQueue.size() > 0) {
					deleteFilesButton.setVisible(true);
					startButton.setVisible(true);
				}
			}
		}

	}
	
	private void resetSelection() {
		currentMode = Mode.ZIP;
		currentSplit = Split.NUMBER;
		splitInt = 0;
		splitField.setText("10");
		password = "";
		passwordField.setText("");
		zipRadio.setSelected(true);
		numberFiles.setSelected(true);
		showPasswordOptions(false);
		showSplitOptions(false);
	}

//	private class QueuePrintListener implements ActionListener {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			System.out.println("Print Queue!!!");
//			for (AbstractFile file : fileQueue) {
//				System.out.println("File " + file.toString());
//			}			
//		}	
//	}

	private void showPasswordOptions(boolean show) {
		this.passwordLabel1.setVisible(show);
		this.passwordField.setVisible(show);
		this.passwordLabel2.setVisible(show);
	}

	private void showSplitOptions(boolean show) {
		this.splitLabel.setVisible(show);
		this.numberFiles.setVisible(show);
		this.fileSize.setVisible(show);
		this.splitField.setVisible(show);
	}

}

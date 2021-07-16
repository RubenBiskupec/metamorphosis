package metamorphosis.gui;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import metamorphosis.files.AbstractFile;
import metamorphosis.files.Crypter;
import metamorphosis.files.NumberSplitter;
import metamorphosis.files.SizeSplitter;
import metamorphosis.files.FileInfo.Mode;
import metamorphosis.files.FileInfo.Split;
import metamorphosis.files.Zipper;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import java.awt.event.ActionEvent;


/**
 * GUI Class - Java Swing
 */
public class RootPanel extends JPanel implements ActionListener {

	// LAYOUT
	private JRadioButton zipRadio;
	private JButton addFilesButton;
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
	private JProgressBar progressBar;

	private static final String ZIP = "Zip/Unzip";
	private static final String SPLIT = "Split/Merge";
	private static final String CRYPT = "Encrypt/Decrypt";
	private static final String NUMBER = "N Files";
	private static final String SIZE = "Size X (bytes)";
	
	// INFO FILE DA CREARE
	private Mode currentMode = Mode.ZIP;
	private Split currentSplitMode = Split.NUMBER;
	private int splitInt = 10;
	private String password = "";

	// I use Vector because it's the parameter required in the JTable
	// Also vectors are synchronized so they are preferred in multi-threaded
	// applications
	// If I want to add threads I won't have to change much code
	// It is slower than ArrayList but that is a tradeoff I have to take
	// The other option was to use a Queue (Linked List)
	private Vector<AbstractFile> fileQueue = new Vector<>();

	
	/**
     * Constructor - generates all the GUI componenets and attaches listeners
     */
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
		addFilesButton = new JButton("Add files");
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

		// Progress Bar
		progressBar = new JProgressBar();
		progressBar.setSize(400, 100);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(true);
		add(progressBar);

		// Table
		String[] tableColumnNames = { "Filename", "Mode", "Size" };
		tableModel = new TableModel(tableColumnNames, 0, fileQueue);
		queueTable = new JTable(tableModel);
		queueTable.setSize(450, 300);
		JScrollPane scrollPane = new JScrollPane(queueTable);
		add(scrollPane);
	}
	
	/**
     * Action listener for zip/split/crypt choice
     * Saves the result in field currentMode
     */
	@Override
	public void actionPerformed(ActionEvent e) {
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

	/**
     * Listener on password changed
     */
	private class ChangePasswordListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			password = e.getActionCommand();
			System.out.println("Password:");
			System.out.println(password);
		}
	}

	/**
     * Listener on Split mode: Size/Number
     * Saves it in field currentSplitMode
     */
	private class ChangeSplitModeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case NUMBER:
				currentSplitMode = Split.NUMBER;
				break;
			case SIZE:
				currentSplitMode = Split.SIZE;
				break;
			}
			System.out.println("Current Split:");
			System.out.println(currentSplitMode);
		}
	}

	/**
     * Listener on Size/Number field
     * Saves it in field splitInt
     */
	private class ChangeSplitFieldListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			splitInt = Integer.parseInt(e.getActionCommand());
			System.out.println("New Split Int" + splitInt);
		}
	}


	/**
     * Listener on delete files from table
     * Deletes the selected files
     */
	private class DeleteFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRows = queueTable.getSelectedRows();
			// remove in reverse order to keep indeces unchanged
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


	/**
     * Listener on Start
     * For each file in the Queue performs method "action"
     * Updates the progress bar
     */
	private class StartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			deleteFilesButton.setVisible(false);
			startButton.setVisible(false);
			addFilesButton.setVisible(false);
			System.out.println("Start action");
			int chunk = 100 / fileQueue.size();
			for (AbstractFile abstractFile : fileQueue) {
				System.out.println(abstractFile.toString());
				abstractFile.action();
				progressBar.setValue(progressBar.getValue() + chunk);
			}
			fileQueue.clear();
			tableModel.fireTableDataChanged();
			resetSelection();
			System.out.println("File queue size: " + fileQueue.size());
		}
	}


	/**
     * Listener on file chooser
     * Categorizes file and adds it to queue
     */
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
							switch (currentSplitMode) {
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
				progressBar.setValue(0);
				tableModel.fireTableDataChanged();
				if (fileQueue.size() > 0) {
					deleteFilesButton.setVisible(true);
					startButton.setVisible(true);
					progressBar.setValue(0);		
				}
			}
		}
	}


	/**
     * Resets field selection
     */
	private void resetSelection() {
		currentMode = Mode.ZIP;
		currentSplitMode = Split.NUMBER;
		splitInt = 10;
		splitField.setText("10");
		password = "";
		passwordField.setText("");
		zipRadio.setSelected(true);
		numberFiles.setSelected(true);
		addFilesButton.setVisible(true);
		showPasswordOptions(false);
		showSplitOptions(false);
	}


	/**
     * show/hide password options
     */
	private void showPasswordOptions(boolean show) {
		this.passwordLabel1.setVisible(show);
		this.passwordField.setVisible(show);
		this.passwordLabel2.setVisible(show);
	}

	/**
     * show/hide split options
     */
	private void showSplitOptions(boolean show) {
		this.splitLabel.setVisible(show);
		this.numberFiles.setVisible(show);
		this.fileSize.setVisible(show);
		this.splitField.setVisible(show);
	}

}

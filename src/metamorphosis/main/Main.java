package metamorphosis.main;

import javax.swing.JFrame;

import metamorphosis.gui.RootPanel;

/**
 * Main class of the program
 */
public class Main {
	
	/**
	 * Sets the jframe and adds the RootPanel which is the core of the GUI
	 */
	public static void main(String[] args){
        JFrame jframe = new JFrame("Metarmophosis");        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        jframe.setBounds(0, 0, 500, 500);  
        jframe.setLocationRelativeTo(null);  
        jframe.setResizable(false);                             
        jframe.add(new RootPanel());                           
        jframe.setVisible(true);                             
    }

}

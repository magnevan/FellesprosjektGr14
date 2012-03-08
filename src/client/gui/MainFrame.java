package client.gui;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame(String title) {
		super(title);
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new MainFrame("Kalender");
		
		frame.pack();
		frame.setVisible(true);

	}

}

package client.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import client.gui.week.WeekView;


public class MainPanel extends JPanel {

	private static final long serialVersionUID = -6453034572305492442L;


	public MainPanel() {
		super();
	}
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Kalender");
		
		JPanel content = new MainPanel();
		frame.setContentPane(content);
		
		frame.pack();
		frame.setVisible(true);
	}

}

package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	public MainFrame(String title) {
		super(title);
	}
	
	
	public static void main(String[] args) {
		
		JFrame frame = new MainFrame("Kalender");
		
		JPanel content = new JPanel();
		content.add(new WeekView());
		frame.setContentPane(content);
		
		
		frame.pack();
		frame.setVisible(true);
		

	}

}

package client.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	public MainFrame(String title) {
		super(title);
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new MainFrame("Kalender");
		
		JPanel temp = new JPanel();
		temp.setPreferredSize(new Dimension(300,600));
		temp.setBackground(new Color(0xFFFFFF));

		JPanel content = new JPanel();
		content.add(temp);
		content.add(new WeekView());
		frame.setContentPane(content);
		
		
		frame.pack();
		frame.setVisible(true);
		

	}

}

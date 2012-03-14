package client.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class AvtalePanel extends JPanel {
	
	private final JTextField tittelText;
//	private final JComboBox moteromComboBox;
//	private final JTextField moteromText;
	
	public AvtalePanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));

		
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(20);
		tittelText.setPreferredSize(new Dimension(50,100));
		this.add(tittelText);
		
		
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		
		JPanel content = new AvtalePanel();
		frame.setContentPane(content);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
	}
	

}

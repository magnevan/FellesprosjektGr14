package client.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AvtalePanel extends JPanel {
	
	private final JTextField tittelText;
//	private final JComboBox moteromComboBox;
//	private final JTextField moteromText;
	
	public AvtalePanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));

		
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(20);
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

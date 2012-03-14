package client.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AvtalePanel extends JPanel {
	
	private final JTextField tittelText;
	
	public AvtalePanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(25);
		JPanel test = new JPanel();
		test.add(tittelText);
		this.add(test);
		
		JPanel fratilPanel = new JPanel();
//		this.add(Box.createVerticalGlue());
		this.add(new JLabel("test"));
	}

}

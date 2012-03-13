package client.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel for the "Hoved" tab
 * @author Magne
 *
 */
public class HovedPanel extends JPanel{
	
	private final JLabel nameLabel;
	private final JButton logoutButton;
	
	public HovedPanel() {
		super(new BorderLayout());
		
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		logoutButton = new JButton("Logg ut");
		JPanel logoutButtonPanel = new JPanel(); //this panel is here to avoid that the button stretches vertically
		logoutButtonPanel.add(logoutButton);
		
		JPanel topPanel = new JPanel(new BorderLayout(30,0));
		topPanel.add(nameLabel, BorderLayout.WEST);
		topPanel.add(logoutButtonPanel, BorderLayout.EAST);
		
		this.add(topPanel, BorderLayout.NORTH);
	}
	
}

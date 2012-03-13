package client.gui;

import javax.swing.ImageIcon;
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
	
	public HovedPanel() {
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		
		
		
		this.add(nameLabel);
	}
	
}

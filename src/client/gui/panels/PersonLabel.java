package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PersonLabel extends JPanel {
	
	private final JLabel nameLabel;
	private final JButton logoutButton;
	
	public PersonLabel(){
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200,100));
		
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		logoutButton = new JButton("Logg ut");
		JPanel logoutButtonPanel = new JPanel(); //this panel is here to avoid that the button stretches vertically
		logoutButtonPanel.add(logoutButton);
		
		
		
		JPanel topPanel = new JPanel(new BorderLayout(10,0));
		
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//logout
				JOptionPane.showMessageDialog(null, "Logger ut..");
			}
		});
		
		topPanel.add(nameLabel, BorderLayout.WEST);
		topPanel.add(logoutButtonPanel, BorderLayout.EAST);
		
		this.add(topPanel,    BorderLayout.NORTH);
		
		
		
	}

}

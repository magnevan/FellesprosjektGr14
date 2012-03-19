package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import client.ClientMain;
import client.ServerConnection;
import client.model.UserModel;

@SuppressWarnings("serial")
public class PersonLabel extends JPanel implements PropertyChangeListener, ActionListener {
	
	private final JLabel nameLabel;
	private final JButton logoutButton;
	
	public PersonLabel(){
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200,100));
		
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel(ClientMain.getActiveUser().getFullName(), icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		logoutButton = new JButton("Logg ut");
		JPanel logoutButtonPanel = new JPanel(); //this panel is here to avoid that the button stretches vertically
		logoutButtonPanel.add(logoutButton);
		
		JPanel topPanel = new JPanel(new BorderLayout(10,0));
		topPanel.add(nameLabel, BorderLayout.WEST);
		topPanel.add(logoutButtonPanel, BorderLayout.EAST);
		
		this.add(topPanel,    BorderLayout.NORTH);
		
		//Listeners
		ClientMain.getActiveUser().addPropertyChangeListener(this);
		
		//Events
		logoutButton.addActionListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == UserModel.NAME_CHANGE) {
			nameLabel.setName((String)evt.getNewValue());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerConnection.logout();
	}

}

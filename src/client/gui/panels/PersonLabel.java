package client.gui.panels;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import client.ClientMain;
import client.ServerConnection;
import client.model.UserModel;

/**
 * The default top-label with an icon, the name of the user and a logoutbutton 
 * 
 * @author Peter Ringset
 *
 */
public class PersonLabel extends JPanel implements PropertyChangeListener, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4966425223154846186L;
	private final JLabel nameLabel;
	private final JButton logoutButton;
	
	public PersonLabel(){
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));		
		
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel(ClientMain.getActiveUser().getFullName(), icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		logoutButton = new JButton("Logg ut");
		logoutButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		this.add(nameLabel);
		this.add(Box.createHorizontalGlue());
		this.add(logoutButton);
		
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

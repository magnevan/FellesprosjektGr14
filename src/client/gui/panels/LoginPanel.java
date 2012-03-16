package client.gui.panels;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.gui.MainPanel;
import client.gui.week.WeekView;

public class LoginPanel extends JPanel {
	
	
	private final JTextField txtUsername, txtPassword;
	private final JButton loginButton;
	
	public LoginPanel() {
		
		//GUI setup
		JLabel  lblUsername = new JLabel("Brukernavn:"), 
				lblPassword = new JLabel("Passord:");
		txtUsername = new JTextField(15);
		txtPassword = new JPasswordField(15);
		
		
		
		loginButton = new JButton("Logg inn");
		
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(lblUsername)
							.addComponent(lblPassword))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(txtUsername)
							.addComponent(txtPassword)
							.addComponent(loginButton))
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(lblUsername)
							.addComponent(txtUsername))
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(lblPassword)
							.addComponent(txtPassword))
					.addComponent(loginButton)
				);
		
		//Behaviors
		
	}
}

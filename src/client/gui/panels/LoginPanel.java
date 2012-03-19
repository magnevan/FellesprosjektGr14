package client.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientMain;
import client.IServerConnectionListener;
import client.ServerConnection;

public class LoginPanel extends JPanel implements ActionListener{
	
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
		
		txtUsername.addActionListener(this);
		txtPassword.addActionListener(this);
		loginButton.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent actE) {
		if 			(txtUsername.getText().length() == 0) {
			txtUsername.requestFocusInWindow();
			
		} else if 	(txtPassword.getText().length() == 0) {
			txtPassword.requestFocusInWindow();
			
		} else {
			attemptLogin(txtUsername.getText(), txtPassword.getText());
		}
		
	}
	
	private void attemptLogin(String username, String password) {
		//TODO temp
		//ClientMain.client().serverConnectionChange(IServerConnectionListener.LOGIN);
		//if (true) return;
		
		InetAddress target;
		int port;
		
		try {
			Properties p = new Properties();
			p.load(new FileReader(new File("src/client.properties")));
			
			target = InetAddress.getByName(p.getProperty("fp.target.url"));
			port = Integer.parseInt(p.getProperty("fp.target.port"));
		} catch (IOException exp) {
			System.out.println("Couldn't read properties");
			exp.printStackTrace();
			return;
		}
		
		
		try {
			ServerConnection.login(target, port, username, password);
		} catch (ConnectException e) {
			System.out.println("Connect Exception");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("Bad login");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		
//		JFrame frame = new JFrame("Kalender");
//		
//		JPanel content = new LoginPanel();
//		frame.setContentPane(content);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
//		frame.setVisible(true);
//	}

}

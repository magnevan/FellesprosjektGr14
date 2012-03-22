package client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ServerConnection;
import client.gui.exceptions.BadLoginException;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel implements ActionListener{
	
	private final JTextField txtUsername, txtPassword;
	private final JButton loginButton;
	private final JLabel lblStatus;
	
	public LoginPanel() {
		
		//GUI setup
		lblStatus = new JLabel("   ");
		lblStatus.setForeground(Color.RED);
		lblStatus.setFont(new Font(
					lblStatus.getFont().getName(),
					lblStatus.getFont().getStyle(),
					11
				));
		
		JLabel  lblUsername = new JLabel("Brukernavn:"), 
				lblPassword = new JLabel("Passord:");
		txtUsername = new JTextField("",20);
		txtPassword = new JPasswordField("",20);
		
		loginButton = new JButton("Logg inn");
		loginButton.setMinimumSize(new Dimension(
					txtPassword.getPreferredSize().width,
					loginButton.getMinimumSize().height
				));
		
		
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
							.addComponent(lblStatus)
							.addComponent(txtUsername)
							.addComponent(txtPassword)
							.addComponent(loginButton))
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(lblStatus)
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
		
		InetAddress target;
		int port;
		
		try {
			Properties p = new Properties();
			p.load(new FileReader(new File("src/client.properties")));
			
			target = InetAddress.getByName(p.getProperty("fp.target.url"));
			port = Integer.parseInt(p.getProperty("fp.target.port"));
		} catch (IOException exp) {
			exp.printStackTrace();
			return;
		}
		
		
		try {
			ServerConnection.login(target, port, username, password);
			//ServerConnection.login(InetAddress.getByName("78.91.7.8"), 9034, "runar", "runar");
		} catch (BadLoginException e) {
			lblStatus.setText("Ugyldig brukernavn/passord");
		} catch (ConnectException e) {
			lblStatus.setText("Klarte ikke opprette tilkobling til server");
		} catch (Exception e) {
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

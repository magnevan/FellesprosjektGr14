package client;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.gui.panels.LoginPanel;
import client.gui.panels.MainPanel;

/**
 * Main entry point for the client
 * 
 * @author Magne
 *
 */
public class ClientMain extends JFrame implements IServerConnectionListener{

	private LoginPanel loginPanel;
	private MainPanel  mainPanel;
	
	private JPanel contentPane;
	
	private static ClientMain client;
	
	public ClientMain() {
		super("Kalender");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		contentPane = new JPanel(new GridBagLayout());
		this.setContentPane(contentPane);
		
		loginPanel = new LoginPanel();
		mainPanel = new MainPanel();
		
		contentPane.add(loginPanel,new GridBagConstraints());
		this.pack();
		this.setLocationRelativeTo(getRootPane());
		this.setResizable(false);
		
		ServerConnection.addServerConnectionListener(this);
		
		this.setVisible(true);
	}
	

	@Override
	public void serverConnectionChange(String change) {
		if (change == IServerConnectionListener.LOGIN) {
			this.setResizable(true);
			contentPane.remove(loginPanel);
			this.setLayout(new BorderLayout());
			contentPane.add(mainPanel, BorderLayout.CENTER);
			this.pack();
			this.setLocationRelativeTo(getRootPane()); //TODO This doesn't place it perfectly in the center as it should
		}
	}
	
	/**
	 * Get Singleton instance
	 * @return
	 */
	public static ClientMain client() {
		return client;
	}
	
	public static void main(String[] args) {
		client = new ClientMain();
	}
	
//	public static void main(String[] args) {
//	    JFrame frame = new JFrame();
//	    frame.setLayout(new GridBagLayout());
//	    JPanel panel = new JPanel();
//	    panel.add(new JLabel("This is a label"));
//	    panel.setBorder(new LineBorder(Color.BLACK)); // make it easy to see
//	    frame.add(panel, new GridBagConstraints());
//	    frame.setSize(400, 400);
//	    frame.setLocationRelativeTo(null);
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    frame.setVisible(true);
//	}

}

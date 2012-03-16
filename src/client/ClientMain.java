package client;

import javax.swing.JFrame;

import client.gui.panels.LoginPanel;

/**
 * Main entry point for the client
 * 
 * @author Magne
 *
 */
public class ClientMain extends JFrame {

	public ClientMain() {
		super("Kalender");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		LoginPanel lp = new LoginPanel();
		
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}

}

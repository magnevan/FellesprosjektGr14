package client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JFrame;

import client.gui.AbstractFilteredUserListModel;
import client.gui.FilteredUserList;
import client.gui.FilteredUserListModel;
import client.model.UserModel;

/**
 * Main entry point for the client
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ClientMain {

	public ClientMain() throws IOException {
		// Attempt to login, will throw a IOException login error
		ServerConnection.login(InetAddress.getLocalHost(), 9034, "runar", "runar");
		
		//ServerConnection.instance().requestFilteredUserList(this, "");
		JFrame frame = new JFrame("Search test");
		frame.add(new FilteredUserList(new FilteredUserListModel()));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		new ClientMain();
	}

}

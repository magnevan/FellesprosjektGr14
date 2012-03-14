package client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JFrame;

import client.gui.AbstractFilteredUserListModel;

public class Main implements IServerResponseListener {

	public Main() throws IOException {
		ServerConnection.login(InetAddress.getLocalHost(), 9034, "runar", "runar");
		
		ServerConnection.instance().requestFilteredUserList(this, "");
	}
	
	public static void main(String[] args) throws IOException {
		new Main();
	}

	@Override
	public void onServerResponse(int requestId, Object data) {
		System.out.println("Response");
		ArrayList<UserModel> users = (ArrayList<UserModel>) data;
		for(UserModel u : users) {
			System.out.println(u);
		}
		
	}

}

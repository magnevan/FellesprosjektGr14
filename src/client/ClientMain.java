package client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import client.model.InvitationModel;
import client.model.MeetingModel;

/**
 * Main entry point for the client
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ClientMain implements IServerResponseListener {

	public ClientMain() throws IOException {
		// Attempt to login, will throw a IOException login error
		ServerConnection.login(InetAddress.getLocalHost(), 9034, "runar", "runar");
		
		ServerConnection sc = ServerConnection.instance();
		
		int id = sc.requestMeeting(this, 1);
		
		//ServerConnection.instance().requestFilteredUserList(this, "");
		/*JFrame frame = new JFrame("Search test");
		frame.add(new FilteredUserList(new FilteredUserListModel()));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);*/
	}
	
	public static void main(String[] args) throws IOException {
		new ClientMain();
	}

	@Override
	public void onServerResponse(int requestId, Object data) {
		ArrayList<MeetingModel> meetings = (ArrayList<MeetingModel>) data;
		
		for(MeetingModel m : meetings) {
			System.out.println(m.getName());
			System.out.println(m.getOwner());
			System.out.println(m.getInvitations().size());
			for(InvitationModel u : m.getInvitations()) {
				System.out.println(u);
			}
		}
		
	}

}

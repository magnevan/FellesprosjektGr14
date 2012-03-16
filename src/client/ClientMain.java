package client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;

import client.model.MeetingModel;
import client.model.UserModel;

/**
 * Main entry point for the client
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ClientMain implements IServerResponseListener {

	public ClientMain() throws IOException {
		// Attempt to login, will throw a IOException login error
		ServerConnection.login(InetAddress.getLocalHost(), 3306, "root", "root");
		
		ServerConnection sc = ServerConnection.instance();
		
		//System.out.println(sc.getUser());
		
		Calendar c = Calendar.getInstance();
		c.set(2012, 3, 14, 10, 15);
		Calendar c1 = Calendar.getInstance();
		c1.set(2012, 3, 18, 11, 15);		
		sc.requestMeetings(this, new UserModel[]{sc.getUser()}, c, c1);
		
		//MeetingModel model = new MeetingModel(c, c1, sc.getUser());
		
		//model.setName("Super viktig møte!");
		//model.setDescription("Dette er beskrivelsen\r\nOg denne er da følgelig minst like viktig\r\n\r\n");
		
		//model = (MeetingModel) sc.storeModel(model);
		
		//System.out.println("Stored! "+model.getId());
		
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
			System.out.println(m);
			//System.out.println(m.getOwner());
		}
		
	}

}

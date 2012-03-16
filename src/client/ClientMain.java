package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.gui.usersearch.FilteredUserList;
import client.model.FilteredUserListModel;
import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.UserModel;

/**
 * Demonstrasjon av ServerConnection
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ClientMain implements IServerResponseListener {

	private int meetingRequest;
	private MeetingModel newMeeting;
	private FilteredUserList ful;
	private ServerConnection sc;
	
	
	public ClientMain() throws IOException {
		// Login connects to server, tries to authenticate and throws an exception of anything fails
		ServerConnection.login(InetAddress.getLocalHost(), 9034, "runar", "runar");
		
		// Get singleton instance for use later.
		sc = ServerConnection.instance();
		
		// sc.request*(listener, parameters) requests data off the server, result comes in onServerResponse() 
		meetingRequest = sc.requestMeeting(this, 1);

		// Current online user can be found in ServerConnection
		UserModel user = sc.getUser();
		
		// New models are created using their default constructor
		Calendar from = Calendar.getInstance();
		from.set(2012, 3, 16, 14, 30);
		Calendar to = Calendar.getInstance();
		to.set(2012, 3, 16, 16, 30);
		newMeeting = new MeetingModel(from, to, user);
		
		System.out.println("Meeting has id: "+newMeeting.getId()+" (ie. not stored yet)");
		newMeeting = (MeetingModel) sc.storeModel(newMeeting);
		System.out.println("Meeting now has id: "+newMeeting.getId()+" (ie. has been stored)");
				
		// Test adding users to the newly created meeting by showing a user list and a add button
		JFrame frame = new JFrame("Search test");
		JPanel panel = new JPanel();
		frame.add(panel);
		
		ful = new FilteredUserList(new FilteredUserListModel());
		panel.add(ful);
		JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAttendees();
			}
		});
		panel.add(add);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * Add attendees on button click
	 */
	public void addAttendees() {
		newMeeting.addAttendee(ful.getSelectedUsers());
		newMeeting = (MeetingModel) sc.storeModel(newMeeting);
	}
	
	public static void main(String[] args) throws IOException {
		new ClientMain();
	}

	@Override
	public void onServerResponse(int requestId, Object data) {
		if(requestId == meetingRequest) {
			ArrayList<MeetingModel> meetings = (ArrayList<MeetingModel>) data;
			if(meetings.size() == 1) {
				MeetingModel m = meetings.get(0);
				System.out.println(m.getName());
				System.out.println(m.getOwner());
				System.out.println(m.getInvitations().size());
				for(InvitationModel u : m.getInvitations()) {
					System.out.println(u);
				}
			}
		}
		
	}

}

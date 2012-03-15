package client;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

import client.gui.usersearch.FilteredUserList;
import client.model.FilteredUserListModel;
import client.model.MeetingModel;

/**
 * Main entry point for the client
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ClientMain {

	public ClientMain() throws IOException {
		// Attempt to login, will throw a IOException login error
		ServerConnection.login(InetAddress.getLocalHost(), 9034, "runar", "runar");
		
		ServerConnection sc = ServerConnection.instance();
		
		//System.out.println(sc.getUser());
		
		//Calendar c = Calendar.getInstance();
		//c.set(2012, 3, 15, 10, 15);
		//Calendar c1 = Calendar.getInstance();
		//c1.set(2012, 3, 15, 11, 15);
				
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

}

package server.model;

import java.sql.SQLException;

import server.DBConnection;
import client.model.InvitationModel;
import client.model.NotificationModel;
import client.model.NotificationType;

public class ServerInvitationModel extends InvitationModel implements IServerModel {

	public ServerInvitationModel() {
		super();
	}
	
	/**
	 * Copy constructor,
	 * 
	 * We have to change the way meetings are transfered, this should not be needed
	 * 
	 * @param i
	 */
	public ServerInvitationModel(InvitationModel i){
		super(i.getUser(), i.getMeeting(), i.getStatus());
	}

	/**
	 * Get the invitation sent to the given user for the given meeting id,
	 * or null if none was found
	 * 
	 * @param db
	 * @param username
	 * @param mid
	 * @return
	 */
	public ServerInvitationModel findInvitation(DBConnection db, String username, int mid) {
		
		return null;
	}
	
	@Override
	public void store(DBConnection db) {
		ServerInvitationModel old = findInvitation(db, getUser().getUsername(), getMeeting().getId());
		
		try {
			// New invitation
			if(old == null) {
				db.preformUpdate(String.format("INSERT INTO user_appointment" +
						"(appointment_id, username, status) VALUES(%d, '%s', '%s')",
						getMeeting().getId(), getUser().getUsername(), getStatus()));
				
				// Create a new notification
				ServerNotificationModel notification = new ServerNotificationModel(
						NotificationType.A_INVITATION, getUser(), getMeeting());
				notification.store(db);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

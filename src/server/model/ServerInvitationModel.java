package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import server.DBConnection;
import client.model.InvitationModel;
import client.model.InvitationStatus;
import client.model.MeetingModel;
import client.model.NotificationModel;
import client.model.NotificationType;
import client.model.TransferableModel;
import client.model.UserModel;

/**
 * Server side version of the Invitation Model
 * 
 * Adds a couple of DBConnection interaction methods
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerInvitationModel extends InvitationModel implements IServerModel {
	
	/**
	 * Construct model from a ResultSet and the related user and meeting model
	 * 
	 * @param rs
	 */
	public ServerInvitationModel(ResultSet rs, UserModel user, 
			MeetingModel meeting) throws SQLException {
		super(user, meeting, InvitationStatus.valueOf(rs.getString("status")));
	}
	
	/**
	 * Construct a ServerInvitationModel from a stream and a given buffer
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ServerInvitationModel(BufferedReader reader, 
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		super(reader, modelBuff);
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
	
	/**
	 * Store invitation
	 */
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
	
	
	/**
	 * Find all invitations registered for the given meeting
	 * 
	 * @param id
	 * @param dbConnection
	 * @return
	 */
	public static ArrayList<InvitationModel> findByMeeting(MeetingModel meeting,
			DBConnection db) {
		
		ArrayList<InvitationModel> ret = new ArrayList<InvitationModel>();
		try {
			ResultSet rs = db.preformQuery(
					"SELECT * FROM user_appointment as ua " +
					"INNER JOIN user as u ON ua.username = u.username " +
					"WHERE ua.appointment_id = "+meeting.getId()+";");
			while (rs.next()) {
				UserModel user = new ServerUserModel(rs);
				ret.add(new ServerInvitationModel(rs, user, meeting));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}

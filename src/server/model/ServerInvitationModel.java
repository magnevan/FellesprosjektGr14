package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.DBConnection;
import client.model.InvitationModel;
import client.model.InvitationStatus;
import client.model.MeetingModel;
import client.model.NotificationType;
import client.model.UserModel;

/**
 * Server side version of the Invitation Model
 * 
 * Adds a couple of DBConnection interaction methods
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerInvitationModel extends InvitationModel 
	implements IDBStorableModel {
	
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
	public ServerInvitationModel(BufferedReader reader) throws IOException {
		super(reader);
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
	public ServerInvitationModel findInvitation(DBConnection db, UserModel user, MeetingModel meeting) {
		try {
			ResultSet rs = db.performQuery(
					"SELECT * FROM user_appointment " +
					"WHERE username='"+user.getUsername()+"' AND appointment_id=" + meeting.getId());
			
			if(rs.next()) {
				return new ServerInvitationModel(rs, user, meeting);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Store invitation
	 */
	@Override
	public void store(DBConnection db) {
		ServerInvitationModel old = findInvitation(db, getUser(), getMeeting());
		
		try {
			// New invitation
			if(old == null) {
				db.performUpdate(String.format("INSERT INTO user_appointment" +
						"(appointment_id, username, status) VALUES(%d, '%s', '%s')",
						getMeeting().getId(), getUser().getUsername(), getStatus()));
				
				// Create a new notification
				ServerNotificationModel notification = new ServerNotificationModel(
						NotificationType.A_INVITATION, getUser(), getMeeting(), 
						getMeeting().getOwner());
				notification.store(db);
				

			// Update invitation
			} else {
				db.performUpdate(String.format("UPDATE user_appointment " +
						"SET status = '%s' WHERE appointment_id=%d AND username='%s'",
						getStatus(), getMeeting().getId(), getUser().getUsername()));
				
				if(getStatus() == InvitationStatus.DECLINED) {
					ServerNotificationModel notification = new ServerNotificationModel(
							NotificationType.A_USER_DENIED, getMeeting().getOwner(), 
							getMeeting(), getUser());
					notification.store(db);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete the invitation without notifying the meeting owner
	 * @param db
	 */
	public void delete(DBConnection db) {
		try {
			db.performUpdate(String.format("DELETE FROM user_appointment " +
					"WHERE appointment_id=%d AND username='%s'",
					getMeeting().getId(), getUser().getUsername()));
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete invitation, and send a notification either to the meeting owner
	 * its the user that deleted. Or the user if the meeting owner deletes it.
	 * 
	 * @param db
	 * @param isMeetingOwner
	 */
	public void delete(DBConnection db, boolean isMeetingOwner) {
		delete(db);
		
		if(!isMeetingOwner) {
			new ServerNotificationModel(
					NotificationType.A_USER_DENIED, getMeeting().getOwner(),
					getMeeting(), getUser()
			).store(db);
		} else {
			new ServerNotificationModel(
					NotificationType.A_CANCELED, getUser(), getMeeting(), 
					getMeeting().getOwner()
			).store(db);
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
			ResultSet rs = db.performQuery(
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
	
	/**
	 * Find a single meeting based on the meeting and user model
	 * 
	 * @param meeting
	 * @param user
	 * @param db
	 * @return
	 */
	public static ServerInvitationModel findByMeetingAndUser(
			MeetingModel meeting, UserModel user, DBConnection db) {
		
		ServerInvitationModel ret = null;
		try {
			ResultSet rs = db.performQuery(
					"SELECT * FROM user_appointment " +
					"WHERE appointment_id = "+meeting.getId()+" " +
					"AND username = '"+user.getUsername()+"';");
			if(rs.next()) {
				ret = new ServerInvitationModel(rs, user, meeting);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}

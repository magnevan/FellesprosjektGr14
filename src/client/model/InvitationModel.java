package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.DBConnection;
import server.model.ServerUserModel;

/**
 * Model representing a single invitation
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class InvitationModel extends TransferableModel {

	protected UserModel user;
	protected MeetingModel meeting;
	protected InvitationStatus status;
	
	/**
	 * Create a new invitation
	 * 
	 * @param user
	 * @param meeting
	 * @param status
	 */
	public InvitationModel(UserModel user, MeetingModel meeting, InvitationStatus status) {
		this.user = user;
		this.meeting = meeting;
		this.status = status;
	}
	
	/**
	 * Create a new invitation with INVITED status
	 * @param user
	 * @param meetingModel
	 */
	public InvitationModel(UserModel user, MeetingModel meetingModel) {
		this(user, meetingModel, InvitationStatus.INVITED);
	}
	
	public InvitationModel() {}
	
	/**
	 * Construct model from ResultSet
	 * 
	 * @param rs
	 */
	public InvitationModel(ResultSet rs) throws SQLException {
		this.status = InvitationStatus.valueOf(rs.getString("status"));
	}
	

	/**
	 * Unique ID
	 */
	@Override
	protected Object getMID() {
		if(getUser() != null && getUser().getUsername() != null && 
				getMeeting() != null && getMeeting().getId() != -1) {
			return getUser().getUsername() + "_" + getMeeting().getId();
		}
		return null;
	}
	
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public MeetingModel getMeeting() {
		return meeting;
	}
	public void setMeeting(MeetingModel meeting) {
		this.meeting = meeting;
	}
	public InvitationStatus getStatus() {
		return status;
	}
	public void setStatus(InvitationStatus status) {
		this.status = status;
	}
	
	public String toString() {
		return getUser().getFullName() + " | " + status;
	}
	
	/**
	 * Read a invitation and its user object off stream
	 */
	@Override
	public void fromStream(BufferedReader reader) throws IOException {
		setStatus(InvitationStatus.valueOf(reader.readLine()));
		//reader.readLine(); // User header
		user = new UserModel();
		user.fromStream(reader);
	}
	
	/**
	 * Dump the invitation to stream
	 * 
	 * A invitation will dump it's internal status, and the related user object,
	 * but not the related meeting object. 
	 */
	@Override
	public void toStream(BufferedWriter writer) throws IOException {
		writer.write(status.toString()+"\r\n");
		user.toStream(writer);
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
				InvitationModel invitation = new InvitationModel(rs);
				invitation.setUser(user);
				invitation.setMeeting(meeting);
				ret.add(invitation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}

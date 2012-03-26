package client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import client.ClientMain;
import client.ServerConnection;

import server.ModelEnvelope;

/**
 * Model representing a single invitation
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class InvitationModel implements TransferableModel {

	protected UserModel user;
	protected MeetingModel meeting;
	protected InvitationStatus status;
	
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	public static final String STATUS_CHANGED = "status changed";
	public static final String INVITATION_DELETED = "invitation deleted";
	
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
	 * Create a new invitation with NOT_YET_SAVED status
	 * @param user
	 * @param meetingModel
	 */
	public InvitationModel(UserModel user, MeetingModel meetingModel) {
		this(user, meetingModel, InvitationStatus.NOT_YET_SAVED);
	}
	
	/**
	 * Construct a Invitation model based on a BufferedReader and a model buffer
	 * 
	 * @param reader
	 * @param modelBuff
	 */
	public InvitationModel(BufferedReader reader) throws IOException {
		
		status = InvitationStatus.valueOf(reader.readLine());
		meeting_umid = reader.readLine();
		user_umid = reader.readLine();
	}
	
	private String meeting_umid;
	private String user_umid;
	
	/**
	 * Load in sub models
	 */
	public void registerSubModels(HashMap<String, TransferableModel> modelBuff) {
		meeting = (MeetingModel) modelBuff.get(meeting_umid);
		user = (UserModel) modelBuff.get(user_umid);
	}

	/**
	 * Copy updated data from a broadcasted model
	 */
	@Override
	public void copyFrom(TransferableModel source) {
		setStatus(((InvitationModel)source).getStatus());
	}
	
	/**
	 * Unique ID
	 */
	@Override
	public String getUMID() {
		if(getUser() != null && getUser().getUsername() != null && 
				getMeeting() != null && getMeeting().getId() != -1) {
			return "invitation_"+getUser().getUsername() + "_" + getMeeting().getId();
		}
		return null;
	}
	
	/**
	 * @return User that was invited
	 */
	public UserModel getUser() {
		return user;
	}
	
	/**
	 * @return Meeting that user has been invited to
	 */
	public MeetingModel getMeeting() {
		return meeting;
	}
	
	/**
	 * Set meeting
	 * 
	 * @param meeting
	 */
	public void setMeeting(MeetingModel meeting) {
		this.meeting = meeting;
	}
	
	/**
	 * @return Current invitation status
	 */
	public InvitationStatus getStatus() {
		return status;
	}
	
	/**
	 * Change current invitation status
	 * @param status
	 */
	public void setStatus(InvitationStatus status) {
		InvitationStatus oldval = this.status;
		this.status = status;
		changeSupport.firePropertyChange(STATUS_CHANGED, oldval, status);
	}
	
	/**
	 * Add property change listener
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * Remove property change listener
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
	
	/**
	 * clear property change listeners
	 */
	public void clearPropertyChangeListeners() {
		for (PropertyChangeListener listener : changeSupport.getPropertyChangeListeners())
			changeSupport.removePropertyChangeListener(listener);
	}
	
	/**
	 * String representation of invitation
	 */
	public String toString() {
		return getUser().getFullName() + " | " + status;
	}
	
	/**
	 * Part of the model envelope support, request that the model registers
	 * all of its sub or dependant models
	 */
	@Override
	public void addSubModels(ModelEnvelope envelope) {
		envelope.addModel(getMeeting());
		envelope.addModel(getUser());		
	}

	/**
	 * Dump the model to a string buffer
	 * 
	 * This dumps three lines, the status value aswell as the UMID for the
	 * related meeting and user models for later reassembly
	 *  
	 */
	@Override
	public void toStringBuilder(StringBuilder sb) {
		sb.append(getStatus()+"\r\n");
		sb.append(getMeeting().getUMID()+"\r\n");
		sb.append(getUser().getUMID()+"\r\n");
	}
	
	/**
	 * Delete a invitation
	 */
	public void delete() throws IOException {
		if(!ClientMain.getActiveUser().equals(getUser()))
			throw new IOException("User does not own invitation");
		ServerConnection.instance().deleteInvitation(this);
	}
	
	/**
	 * Trigger a deleted event
	 */
	public void onDelete() {
		changeSupport.firePropertyChange(INVITATION_DELETED, null, null);
	}
	
}

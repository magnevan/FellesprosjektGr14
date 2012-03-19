package client.model;

import java.beans.PropertyChangeSupport;
import java.util.Calendar;

public class NotificationModel {

	
	
	public final static String REGARDS_MEETING_PROPERTY = "regards_meeting";
	public final static String TYPE_PROPERTY = "type";
	public final static String TIME_PROPERTY = "time";
	public final static String GIVEN_TO_PROPERTY = "given_to";
	public final static String REGARDS_USER_PROPERTY = "regards_user";
	public final static String READ_PROPERTY = "read";
	
	
	private int id;
	private MeetingModel regards_meeting;
	private NotificationType type;
	private Calendar time;
	private UserModel given_to, regards_user;
	private boolean read;
	
	private PropertyChangeSupport changeSupport;
	
	
	
	public NotificationModel(MeetingModel regards_meeting, NotificationType type, Calendar time, UserModel given_to, UserModel regards_user, boolean read) {
		
		changeSupport = new PropertyChangeSupport(this);
		
		this.regards_meeting = regards_meeting;
		this.type = type;
		this.time = time;
		this.given_to = given_to;
		this.regards_user = regards_user;
		this.read = read;
	}
	
	
	public MeetingModel getRegards_meeting() {
		return regards_meeting;
	}
	public void setRegards_meeting(MeetingModel regards_meeting) {
		MeetingModel oldValue = this.regards_meeting;
		this.regards_meeting = regards_meeting;
		changeSupport.firePropertyChange(REGARDS_MEETING_PROPERTY, oldValue, regards_meeting);
	}
	
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		NotificationType oldValue = this.type;
		this.type = type;
		changeSupport.firePropertyChange(TYPE_PROPERTY, oldValue, type);
	}
	
	public Calendar getTime() {
		return time;
	}
	public void setTime(Calendar time) {
		Calendar oldValue = this.time;
		this.time = time;
		changeSupport.firePropertyChange(TIME_PROPERTY, oldValue, time);
	}
	
	public UserModel getGiven_to() {
		return given_to;
	}
	public void setGiven_to(UserModel given_to) {
		UserModel oldValue = this.given_to;
		this.given_to = given_to;
		changeSupport.firePropertyChange(GIVEN_TO_PROPERTY, oldValue, given_to);
	}
	
	public UserModel getRegards_user() {
		return regards_user;
	}
	public void setRegards_user(UserModel regards_user) {
		UserModel oldValue = this.regards_user;
		this.regards_user = regards_user;
		changeSupport.firePropertyChange(REGARDS_USER_PROPERTY, oldValue, regards_user);
	}
	
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		boolean oldValue = this.read;
		this.read = read;
		changeSupport.firePropertyChange(READ_PROPERTY, oldValue, read);
	}
	
	public int getId() {
		return id;
	}
	
	
	
	
	
	
}

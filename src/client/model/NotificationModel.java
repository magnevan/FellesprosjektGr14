package client.model;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import client.ModelCacher;

public class NotificationModel extends TransferableModel {

	
	
	public final static String REGARDS_MEETING_PROPERTY = "regards_meeting";
	public final static String TYPE_PROPERTY = "type";
	public final static String TIME_PROPERTY = "time";
	public final static String GIVEN_TO_PROPERTY = "given_to";
	public final static String REGARDS_USER_PROPERTY = "regards_user";
	public final static String READ_PROPERTY = "read";
	
	
	protected int id = -1;
	private MeetingModel regards_meeting;
	private NotificationType type;
	private Calendar time;
	private UserModel given_to, regards_user;
	private boolean read;
	
	private PropertyChangeSupport changeSupport;
	
	public NotificationModel() {
		changeSupport = new PropertyChangeSupport(this);
	}
	
	/**
	 * Create a notification with timestamp set to NOW and read to false
	 * 
	 * @param type
	 * @param given_to
	 * @param regards_meeting
	 */
	public NotificationModel(NotificationType type, UserModel given_to, MeetingModel regards_meeting) {
		this(type, given_to, regards_meeting, null);
	}

	/**
	 * Create a notification with timestamp set to NOW and read to false
	 * 
	 * @param type
	 * @param given_to
	 * @param regards_meeting
	 * @param regards_user
	 */
	public NotificationModel(NotificationType type, UserModel given_to, MeetingModel regards_meeting, 
			UserModel regards_user) {
		this(type, given_to, regards_meeting, regards_user, Calendar.getInstance(), false);
	}
	
	/**
	 * Create a new notification 
	 * 
	 * @param type
	 * @param given_to
	 * @param regards_meeting
	 * @param regards_user
	 * @param time
	 * @param read
	 */
	public NotificationModel(NotificationType type, UserModel given_to, MeetingModel regards_meeting,
			UserModel regards_user, Calendar time, boolean read) {
		
		this();
		this.regards_meeting = regards_meeting;
		this.type = type;
		this.time = time;
		this.given_to = given_to;
		this.regards_user = regards_user;
		this.read = read;
	}
	
	
	
	public MeetingModel getRegardsMeeting() {
		return regards_meeting;
	}
	public void setRegardsMeeting(MeetingModel regards_meeting) {
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
	
	public UserModel getGivenTo() {
		return given_to;
	}
	public void setGivenTo(UserModel given_to) {
		UserModel oldValue = this.given_to;
		this.given_to = given_to;
		changeSupport.firePropertyChange(GIVEN_TO_PROPERTY, oldValue, given_to);
	}
	
	public UserModel getRegardsUser() {
		return regards_user;
	}
	public void setRegardsUser(UserModel regards_user) {
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

	@Override
	public void fromStream(BufferedReader reader) throws IOException {

		DateFormat df = DateFormat.getDateTimeInstance();
		
		id = Integer.parseInt(reader.readLine());
		type = NotificationType.valueOf(reader.readLine());
		time = Calendar.getInstance();		
		try {
			time.setTime(df.parse(reader.readLine()));
		} catch(ParseException e) {
			e.printStackTrace();
		}
		
		given_to = new UserModel();
		given_to.fromStream(reader);
		given_to = (UserModel) ModelCacher.cache(given_to);
		
		if(!reader.readLine().equals("")) {
			regards_meeting = new MeetingModel();
			regards_meeting.fromStream(reader);
			regards_meeting = (MeetingModel) ModelCacher.cache(regards_meeting);
		}
		if(!reader.readLine().equals("")) {
			regards_user = new UserModel();
			regards_user.fromStream(reader);
			regards_user = (UserModel) ModelCacher.cache(regards_user);
		}
		
	}

	@Override
	public void toStream(BufferedWriter writer) throws IOException {
		StringBuilder sb = new StringBuilder();
		DateFormat df = DateFormat.getDateTimeInstance();
		
		sb.append(getId() + "\r\n");
		sb.append(getType() + "\r\n");
		sb.append(df.format(getTime().getTime()) + "\r\n");
		writer.write(sb.toString());		
		getGivenTo().toStream(writer);
		
		if(getRegardsMeeting() != null) {
			writer.write("1\r\n");
			getRegardsMeeting().toStream(writer);
		} else {
			writer.write("\r\n");
		}
			
		if(getRegardsUser() != null) {
			writer.write("1\r\n");
			getRegardsUser().toStream(writer);
		} else {
			writer.write("\r\n");
		}
	}

	@Override
	protected Object getMID() {
		if(id == -1)
			return null;
		return id;
	}
	
}

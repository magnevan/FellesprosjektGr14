package client.model;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.util.Date;

import client.ServerConnection;
/**
 * A model for the meetings in the calendar
 * 
 * @author peterringset
 *
 */
public class MeetingModel extends Model {
	
	public final static String TIME_FROM_PROPERTY = "timeFrom";
    public final static String TIME_TO_PROPERTY = "timeTo";
    public final static String NAME_PROPERTY = "name";
    public final static String DATE_PROPERTY = "date";
    public final static String ROOM_PROPERTY = "room";
    public final static String LOCATION_PROPERTY = "location";
	
	private Date date;
	private Time timeFrom, timeTo;
	private String name, description, location;
	private MeetingRoomModel room;
	
	private ServerConnection serverConnection;
	private boolean active;
	private UserModel owner;
	
	private PropertyChangeSupport changeSupport;
	
	/**
	 * Construct a new meeting model
	 * Note that timeTo should be after timeFrom
	 * 
	 * @param date
	 * @param timeFrom
	 * @param timeTo
	 * @param owner
	 * @throws IllegalArgumentException if timeFrom is after timeTo
	 */
	public MeetingModel(Date date, Time timeFrom, Time timeTo, UserModel owner) {
		
		changeSupport = new PropertyChangeSupport(this);
		
		this.date = date;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.owner = owner;
		if(timeFrom.before(timeTo)) {
			throw new IllegalArgumentException("MeetingModel: From-time is after to-time");
		}
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		Date oldValue = this.date;
		this.date = date;
		changeSupport.firePropertyChange(DATE_PROPERTY, oldValue, date);
	}

	public Time getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Time timeFrom) {
		Time oldValue = this.timeFrom;
		this.timeFrom = timeFrom;
		changeSupport.firePropertyChange(TIME_FROM_PROPERTY, oldValue, timeFrom);
	}

	public Time getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Time timeTo) {
		Time oldValue = this.timeTo;
		this.timeTo = timeTo;
		changeSupport.firePropertyChange(TIME_TO_PROPERTY, oldValue, timeTo);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MeetingRoomModel getRoom() {
		return room;
	}

	public void setRoom(MeetingRoomModel room) {
		this.room = room;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public ServerConnection getServerConnection() {
		return serverConnection;
	}

	public void setServerConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UserModel getOwner() {
		return owner;
	}

	
	@Override
	public void fromStream(BufferedReader stream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toStream(BufferedWriter stream) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}

package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import client.ServerConnection;
/**
 * A model for the meetings in the calendar
 * 
 * @author peterringset
 *
 */
public class MeetingModel extends Model{
	
	private Calendar timeFrom, timeTo;
	private String name, description;
	private MeetingRoomModel room;
	private ServerConnection serverConnection;
	private boolean active;
	private UserModel owner;
	
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
	public MeetingModel(Calendar timeFrom, Calendar timeTo, UserModel owner) {
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.owner = owner;
		if(timeFrom.before(timeTo)) {
			throw new IllegalArgumentException("MeetingModel: From-time is after to-time");
		}
	}
	
	public Calendar getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Calendar timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Calendar getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Calendar timeTo) {
		this.timeTo = timeTo;
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

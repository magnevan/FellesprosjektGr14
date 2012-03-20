package client.model;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;

import server.ModelEnvelope;
import client.ModelCacher;

/**
 * A model for the meetings in the calendar
 * 
 * @author peterringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class MeetingModel extends TransferableModel {
	
	public final static String TIME_FROM_PROPERTY = "timeFrom";
    public final static String TIME_TO_PROPERTY = "timeTo";
    public final static String NAME_PROPERTY = "name";
    public final static String ROOM_PROPERTY = "room";
    public final static String LOCATION_PROPERTY = "location";
    public final static String DESCRIPTION_PROPERTY = "description";
    public final static String ACTIVE_PROPERTY = "active";

	protected int id;
	protected Calendar timeFrom, timeTo;
	protected String name, description, location;
	protected MeetingRoomModel room;
	protected boolean active;
	protected UserModel owner;	
	private PropertyChangeSupport changeSupport;
	protected ArrayList<InvitationModel> invitations;
	
	/**
	 * Construct a new meeting model
	 * Note that timeTo should be after timeFrom
	 * 
	 * @param timeFrom
	 * @param timeTo
	 * @param owner
	 * @throws IllegalArgumentException if timeFrom is after timeTo
	 */
	public MeetingModel(Calendar timeFrom, Calendar timeTo, UserModel owner) {
		changeSupport = new PropertyChangeSupport(this);
		id = -1;
		invitations = new ArrayList<InvitationModel>();	
		
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.owner = owner;
		if(!timeFrom.before(timeTo)) {
			throw new IllegalArgumentException("MeetingModel: From-time is after to-time");
		}
	}
	
	/**
	 * Construct MeetingModel from stream
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public MeetingModel(BufferedReader reader, 
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		
		id = Integer.parseInt(reader.readLine());
		name = reader.readLine();
		String l;
		while(!(l = reader.readLine()).equals("\0")) 
			description += l+"\r\n";
		
		DateFormat df = DateFormat.getDateTimeInstance();
		
		timeFrom = Calendar.getInstance();
		timeTo = Calendar.getInstance();
		try {
			timeFrom.setTime(df.parse(reader.readLine()));
			timeTo.setTime(df.parse(reader.readLine()));
		} catch (ParseException e) {
			throw new IOException(e.toString());
		}
		
		location = reader.readLine();
		owner = (UserModel) modelBuff.get(reader.readLine());
		
		l = reader.readLine();
		if(!l.equals("")) 
			room = (MeetingRoomModel) modelBuff.get(l);
		
		int invitations = Integer.parseInt(reader.readLine());
		for( ; invitations > 0; invitations--) {
			InvitationModel i = (InvitationModel) modelBuff.get(reader.readLine());
			
			// We've got a circular denpendency here, if we're sending a meeting
			// the invitation will have a null meeting
			if(i.getMeeting() == null)
				i.setMeeting(this);
		}
	}
	
	/**
	 * Get id
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get a ID that uniquely identifies that model
	 */
	@Override
	public String getUMID() {
		if(getId() != -1)
			return "meeting_"+getId();
		return null;
	}
	
	/**
	 * @return start time of meeting
	 */
	public Calendar getTimeFrom() {
		return timeFrom;
	}

	/**
	 * Set start meeting start time
	 * 
	 * @param timeFrom
	 */
	public void setTimeFrom(Calendar timeFrom) {
		Calendar oldValue = this.timeFrom;
		this.timeFrom = timeFrom;
		changeSupport.firePropertyChange(TIME_FROM_PROPERTY, oldValue, timeFrom);
	}

	/**
	 * @return meeting end time
	 */
	public Calendar getTimeTo() {
		return timeTo;
	}

	/**
	 * Set end time
	 * 
	 * @param timeTo
	 */
	public void setTimeTo(Calendar timeTo) {
		Calendar oldValue = this.timeTo;
		this.timeTo = timeTo;
		changeSupport.firePropertyChange(TIME_TO_PROPERTY, oldValue, timeTo);
	}

	/**
	 * Get meeting name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set meeting name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		changeSupport.firePropertyChange(TIME_FROM_PROPERTY, oldValue, name);
	}

	/**
	 * @return meeting description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set meeting description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		String oldValue = this.description;
		this.description = description;
		changeSupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, description);
	}

	/**
	 * @return room model if there is a reserved meeting room
	 */
	public MeetingRoomModel getRoom() {
		return room;
	}

	/**
	 * Reserve a meeting room
	 * 
	 * @param room
	 */
	public void setRoom(MeetingRoomModel room) {
		MeetingRoomModel oldValue = this.room;
		this.room = room;
		changeSupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, room);
	}
	
	/**
	 * @return alternative location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Set alternative location
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		String oldValue = this.location;
		this.location = location;
		changeSupport.firePropertyChange(LOCATION_PROPERTY, oldValue, location);
	}

	/**
	 * @return is active meeting
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set active boolean
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		boolean oldValue = this.active;
		this.active = active;
		changeSupport.firePropertyChange(ACTIVE_PROPERTY, oldValue, active);
	}

	/**
	 * @return owner user
	 */
	public UserModel getOwner() {
		return owner;
	}
	
	public String toString() {
		return getName() + "(" + timeFrom.getTime() + " - " + timeTo.getTime() + ")";
	}
	
	/**
	 * Get all invitations for the meeting
	 * 
	 * @return
	 */
	public ArrayList<InvitationModel> getInvitations() {
		return invitations;
	}
	
	/**
	 * Check if this meeting is actually a meeting and not a appointment
	 * 
	 * @return
	 */
	public boolean isMeeting() {
		return getInvitations().size() != 0;
	}
	
	/**
	 * True if the given user has been invited to this meeting
	 * 
	 * @param user
	 * @return
	 */
	public boolean isInvited(UserModel user) {
		return getInvitation(user) != null;
	}
	
	/**
	 * If this user has been invited to this meeting this will return
	 * the invitation model representing that invitation
	 * 
	 * @param user
	 * @return
	 */
	public InvitationModel getInvitation(UserModel user) {
		for(InvitationModel invitation : getInvitations()) {
			if(invitation.getUser().getUsername().equals(user.getUsername())) {
				return invitation;
			}
		}
		return null;
		
	}
			
	/**
	 * Add a attendee to the meeting
	 * 
	 * @param user
	 */
	public void addAttendee(UserModel user) {
		addAttendee(new UserModel[]{user});
	}
		
	/**
	 * Add a array of attendees to the meeting
	 * 
	 * @param users
	 */
	public void addAttendee(UserModel[] users) {
		for(UserModel user : users) {
			if(!isInvited(user)) {
				invitations.add(new InvitationModel(user, this));
			}
		}		
	}	
	
	public static final Comparator<MeetingModel> timeFromComparator = 
			new Comparator<MeetingModel>() {
				@Override
				public int compare(MeetingModel A, MeetingModel B) {					
					return A.getTimeFrom().compareTo(B.getTimeFrom());
				}
			};
			
	public static final Comparator<MeetingModel> timeToComparator = 
			new Comparator<MeetingModel>() {
				@Override
				public int compare(MeetingModel A, MeetingModel B) {					
					return A.getTimeTo().compareTo(B.getTimeTo());
				}
			};

	/**
	 * Register sub models
	 */
	@Override
	public void addSubModels(ModelEnvelope envelope) {
		envelope.addModel(getOwner());
		if(getRoom() != null)
			envelope.addModel(getRoom());
		for(InvitationModel i : getInvitations())
			envelope.addModel(i);
	}

	/**
	 * Dump model to StringBuilder
	 */
	@Override
	public void toStringBuilder(StringBuilder sb) {
		DateFormat df = DateFormat.getDateTimeInstance();
		
		sb.append(getId() + "\r\n");
		sb.append(getName() + "\r\n");
		if(getDescription() != null) 
			sb.append(getDescription().trim() + "\r\n");
		sb.append("\0\r\n");
		sb.append(df.format(getTimeFrom().getTime()) + "\r\n");
		sb.append(df.format(getTimeTo().getTime()) + "\r\n");
		sb.append(getLocation() + "\r\n");
		
		sb.append(getOwner().getUMID() + "\r\n");
		if(getRoom() != null)
			sb.append(getRoom().getUMID());
		sb.append("\r\n");
		
		sb.append(getInvitations().size()+"\r\n");
		for(InvitationModel i : getInvitations())
			sb.append(i.getUMID()+"\r\n");
		
	}	
			
}

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

import client.ModelCacher;

/**
 * A model for the meetings in the calendar
 * 
 * @author peterringset
 *
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
		this();

		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.owner = owner;
		if(!timeFrom.before(timeTo)) {
			throw new IllegalArgumentException("MeetingModel: From-time is after to-time");
		}
	}
	
	public MeetingModel() {
		changeSupport = new PropertyChangeSupport(this);
		id = -1;
		invitations = new ArrayList<InvitationModel>();
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Get a ID that will identify a MeetingModel
	 */
	@Override
	protected Object getMID() {
		if(getId() != -1)
			return getId();
		return null;
	}
	
	public Calendar getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Calendar timeFrom) {
		Calendar oldValue = this.timeFrom;
		this.timeFrom = timeFrom;
		changeSupport.firePropertyChange(TIME_FROM_PROPERTY, oldValue, timeFrom);
	}

	public Calendar getTimeTo() {
		return timeTo;
	}


	public void setTimeTo(Calendar timeTo) {
		Calendar oldValue = this.timeTo;
		this.timeTo = timeTo;
		changeSupport.firePropertyChange(TIME_TO_PROPERTY, oldValue, timeTo);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		changeSupport.firePropertyChange(TIME_FROM_PROPERTY, oldValue, name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		String oldValue = this.description;
		this.description = description;
		changeSupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, description);
	}

	public MeetingRoomModel getRoom() {
		return room;
	}

	public void setRoom(MeetingRoomModel room) {
		MeetingRoomModel oldValue = this.room;
		this.room = room;
		changeSupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, room);
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		String oldValue = this.location;
		this.location = location;
		changeSupport.firePropertyChange(LOCATION_PROPERTY, oldValue, location);
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		boolean oldValue = this.active;
		this.active = active;
		changeSupport.firePropertyChange(ACTIVE_PROPERTY, oldValue, active);
	}

	public UserModel getOwner() {
		return owner;
	}
	
	public String toString() {
		return getName() + "(" + timeFrom + " - " + timeTo + ")";
	}
	
	public ArrayList<InvitationModel> getInvitations() {
		return invitations;
	}

	/**
	 * Read model from stream
	 */
	@Override
	public void fromStream(BufferedReader reader) throws IOException {
		id = Integer.parseInt(reader.readLine());
		setName(reader.readLine());
		StringBuilder desc = new StringBuilder();
		String line;
		while(!(line = reader.readLine()).equals("\0"))
			desc.append(line+"\r\n");
		setDescription(desc.toString());
		
		DateFormat df = DateFormat.getDateTimeInstance();		
		try {
			Calendar timeFrom = Calendar.getInstance();
			timeFrom.setTime(df.parse(reader.readLine()));
			setTimeFrom(timeFrom);
			
			Calendar timeTo = Calendar.getInstance();
			timeTo.setTime(df.parse(reader.readLine()));			
			setTimeFrom(timeFrom);
			setTimeTo(Calendar.getInstance());
			
		} catch(ParseException e) {
			e.printStackTrace();
		}		
		
		//reader.readLine(); // Class name		
		owner = new UserModel();
		owner.fromStream(reader);
		owner = (UserModel) ModelCacher.cache(owner);
		
		int no = Integer.parseInt(reader.readLine());
		for( ; no > 0 ; no-- ) {
			//reader.readLine(); // Class name
			InvitationModel i = new InvitationModel();
			i.fromStream(reader);
			i = (InvitationModel) ModelCacher.cache(i);
			invitations.add(i);
		}
	}

	/**
	 * Dump the model to stream
	 * 
	 */
	@Override
	public void toStream(BufferedWriter writer) throws IOException {
		StringBuilder sb = new StringBuilder();
		DateFormat df = DateFormat.getDateTimeInstance();
		
		sb.append(getId() + "\r\n");
		sb.append(getName() + "\r\n");
		if(getDescription() != null) 
			sb.append(getDescription().trim() + "\r\n");
		sb.append("\0\r\n");
		sb.append(df.format(getTimeFrom().getTime()) + "\r\n");
		sb.append(df.format(getTimeTo().getTime()) + "\r\n");
		writer.write(sb.toString());		
		
		getOwner().toStream(writer);
		
		writer.write(getInvitations().size()+"\r\n");
		for(InvitationModel invitation : getInvitations()) {
			invitation.toStream(writer);
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
			if(invitation.getUser().equals(user)) {
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
			
}

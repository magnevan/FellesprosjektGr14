package client.model;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import server.ModelEnvelope;

/**
 * A class for modelling a meeting room
 * 
 * @author peterringset
 *
 */
public class MeetingRoomModel extends TransferableModel {
	
	private PropertyChangeSupport pcs;
	
	private String roomNumber;
	private String name;
	private int places;
	
	private final static String ROOMNUMBER_CHANGED = "ROOMNUMBER_CHANGED";
	private final static String NAME_CHANGED = "NAME_CHANGED";
	private final static String PLACES_CHANGED = "PLACES_CHANGED";
	
	/**
	 * Construct the meeting room model
	 * @param roomNumber
	 */
	public MeetingRoomModel(String roomNumber, String name, int places) {
		pcs = new PropertyChangeSupport(this);
		this.roomNumber = roomNumber;
		this.name = name;
		this.places = places;
	}
	
	/**
	 * Create model from stream
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public MeetingRoomModel(BufferedReader reader, 
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		
		this(reader.readLine(), reader.readLine(), 
				Integer.parseInt(reader.readLine()));
	}

	/**
	 * Get unique model ID
	 */
	@Override
	public String getUMID() {
		return "meeting_room_"+roomNumber;
	}
	
	/**
	 * @return room number
	 */
	public String getRoomNumber() {
		return roomNumber;
	}
	
	/**
	 * @return room name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name set room name
	 */
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		pcs.firePropertyChange(NAME_CHANGED, oldValue, name);
	}
	
	/**
	 * @return number of places in the meeting room
	 */
	public int getNoPlaces() {
		return places;
	}
	
	/**
	 * Set number of places
	 * 
	 * @param places
	 */
	public void setNoPlaces(int places) {
		int oldValue  = this.places;
		this.places = places;
		pcs.firePropertyChange(PLACES_CHANGED, oldValue, places);
	}

	/**
	 * Set meeting room number
	 * 
	 * @param roomNumber
	 */
	public void setRoomNumber(String roomNumber) {
		String oldValue = this.roomNumber;
		
		this.roomNumber = roomNumber;
		
		pcs.firePropertyChange(ROOMNUMBER_CHANGED, oldValue, roomNumber);
	}

	/**
	 * Unused, no sub models
	 */
	@Override
	public void addSubModels(ModelEnvelope envelope) {}

	/**
	 * Dump model to the string buffer
	 * 
	 */
	@Override
	public void toStringBuilder(StringBuilder sb) {
		sb.append(getRoomNumber()+"\r\n");
		sb.append(getName()+"\r\n");
		sb.append(getNoPlaces()+"\r\n");		
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}

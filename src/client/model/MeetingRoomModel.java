package client.model;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * A class for modelling a meeting room
 * 
 * @author peterringset
 *
 */
public class MeetingRoomModel extends TransferableModel {
	
	PropertyChangeSupport pcs;
	
	private String roomNumber;
	private String name;
	private int places;
	
	private final static String ROOMNUMBER_CHANGED = "ROOMNUMBER_CHANGED";
	
	/**
	 * Construct the meeting room model
	 * @param roomNumber
	 */
	public MeetingRoomModel(String roomNumber) {
		this();
		this.roomNumber = roomNumber;		
	}
	
	/**
	 * Empty constructor for stream transfer support
	 */
	public MeetingRoomModel() {
		pcs = new PropertyChangeSupport(this);
		name = "";
		places = 0;
	}

	/**
	 * Get unique ID
	 */
	@Override
	protected Object getMID() {
		return roomNumber;
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNoPlaces() {
		return places;
	}
	
	public void setNoPlaces(int places) {
		this.places = places;
	}

	public void setRoomNumber(String roomNumber) {
		String oldValue = this.roomNumber;
		
		this.roomNumber = roomNumber;
		
		pcs.firePropertyChange(ROOMNUMBER_CHANGED, oldValue, roomNumber);
	}
	
	/**
	 * Read model from stream
	 * 
	 */
	@Override
	public void fromStream(BufferedReader reader) throws IOException {
		setRoomNumber(reader.readLine());
		setName(reader.readLine());
		setNoPlaces(Integer.parseInt(reader.readLine()));		
	}

	/**
	 * Dump model to stream
	 * 
	 */
	@Override
	public void toStream(BufferedWriter writer) throws IOException {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getRoomNumber()+"\r\n");
		sb.append(getName()+"\r\n");
		sb.append(getNoPlaces()+"\r\n");		
		writer.write(sb.toString());
		
	}
}

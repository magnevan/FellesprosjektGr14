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
public class MeetingRoomModel extends Model {
	
	PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private String roomNumber;
	
	private final static String ROOMNUMBER_CHANGED = "ROOMNUMBER_CHANGED";
	
	/**
	 * Construct the meeting room model
	 * @param roomNumber
	 */
	public MeetingRoomModel(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		String oldValue = this.roomNumber;
		
		this.roomNumber = roomNumber;
		
		pcs.firePropertyChange(ROOMNUMBER_CHANGED, oldValue, roomNumber);
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

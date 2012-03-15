package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A class for modelling a meeting room
 * 
 * @author peterringset
 *
 */
public class MeetingRoomModel extends AbstractModel {
	
	private String roomNumber;
	
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
		this.roomNumber = roomNumber;
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

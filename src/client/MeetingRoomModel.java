package client;

import java.io.OutputStream;

/**
 * A class for modelling a meeting room
 * 
 * @author peterringset
 *
 */
public class MeetingRoomModel implements Model {
	
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
	public void toStream(OutputStream os) {
	}
}

package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import server.DBConnection;
import client.model.MeetingRoomModel;
import client.model.TransferableModel;

/**
 * Server side version of the MeetingRoomModel
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerMeetingRoomModel extends MeetingRoomModel {

	
	/**
	 * Construct a MeetingRoom model from a ResultSet
	 * 
	 * @param rs
	 */
	public ServerMeetingRoomModel(ResultSet rs) throws SQLException {
		super(rs.getString("room_number"), rs.getString("name"), 
				rs.getInt("no_places"));
	}
	
	/**
	 * Create model from stream
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ServerMeetingRoomModel(BufferedReader reader) throws IOException {		
		super(reader);
	}
	
	/**
	 * Find all available rooms within a given time period
	 * 
	 * @param from
	 * @param to
	 * @param db
	 * @return
	 */
	public static ArrayList<ServerMeetingRoomModel> findAvailableRooms(
			Calendar from, Calendar to, DBConnection db) {

		if(from.after(to)) {
			throw new IllegalArgumentException("From must not be after to");
		}		
		ArrayList<ServerMeetingRoomModel> ret = new ArrayList<ServerMeetingRoomModel>();
		try {			
			ResultSet rs = db.performQuery(
					"SELECT * FROM meeting_room WHERE room_number NOT IN " +
					"(SELECT DISTINCT meeting_room_number FROM appointment AS a " +
					"INNER JOIN meeting_room_booking AS mrb ON mrb.appointment_id = a.id " +
					"WHERE (a.start_date <= '"+getFormattedDate(from)+"' AND a.end_date > '"+getFormattedDate(from)+"') " +
					"OR (a.start_date >= '"+getFormattedDate(from)+"' AND a.start_date < '"+getFormattedDate(to)+"'));");
			while (rs.next()) {
				ret.add(new ServerMeetingRoomModel(rs));
			}
			rs.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 * Find the room thats reserved for the given meeting, if any
	 * 
	 * @param id
	 * @param db
	 * @return
	 */
	public static ServerMeetingRoomModel findReservedRoom(int id, DBConnection db) {
		try {
			ResultSet rs = db.performQuery(
					"SELECT * FROM meeting_room AS mr " +
					"LEFT JOIN meeting_room_booking AS mrb ON mr.room_number = mrb.meeting_room_number " +
					"WHERE mrb.appointment_id = " + id + ";");
			
			if(rs.next()) {
				return new ServerMeetingRoomModel(rs);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/**
	 * Format a Calendar for MySQL's DATETIME field
	 * 
	 * @param c
	 * @return
	 */
	private static String getFormattedDate(Calendar c) {
		return String.format(
				"%d-%d-%d %d:%d:%d", 
				c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)				
		);
	}
}

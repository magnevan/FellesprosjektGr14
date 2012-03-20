package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

import server.DBConnection;
import server.ServerMain;
import client.model.MeetingModel;
import client.model.NotificationModel;
import client.model.NotificationType;
import client.model.TransferableModel;
import client.model.UserModel;

public class ServerNotificationModel extends NotificationModel implements IServerModel {

	/**
	 * Construct a new Notification
	 * 
	 * @param type
	 * @param given_to
	 * @param regards_meeting
	 */
	public ServerNotificationModel(NotificationType type, UserModel given_to, MeetingModel regards_meeting) {
		super(type, given_to, regards_meeting);
	}
	
	/**
	 * Construct NotificationModel from reader
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ServerNotificationModel(BufferedReader reader, 
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		super(reader, modelBuff);
	}
	
	/**
	 * Store notifications
	 * 
	 */
	@Override
	public void store(DBConnection db) {
		
		try {
			// Insert notification
			if(getId() == -1) {
				Statement st = db.createStatement();
				st.executeUpdate(String.format("INSERT INTO notification(`type`, `time`, " +
						"`given_to`, `read`, `regards_appointment`, `regards_user`) " +
						"VALUES ('%s', '%s', '%s', %s, %s, %s)",
						getType(), getFormattedDate(getTime()), getGivenTo().getUsername(),
						(isRead() ? "TRUE" : "FALSE"), 
						(getRegardsMeeting() != null ? getRegardsMeeting().getId() : "NULL"),
						(getRegardsUser() != null ? "'"+getRegardsUser().getUsername()+"'" : "NULL")
					), Statement.RETURN_GENERATED_KEYS);
				
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				id = rs.getInt(1);
				rs.close();
				
				// Send the notification to the user right away
				ServerMain.ccl.broadcastModel(this, getGivenTo().getUsername());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
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

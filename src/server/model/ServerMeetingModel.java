package server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import server.ServerMain;
import client.model.MeetingModel;
import client.model.UserModel;

/**
 * Server Side MeetingModel with methods for reading and writing to server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerMeetingModel extends MeetingModel implements IServerModel {

	public ServerMeetingModel() {
		super();
	}
	
	/**
	 * Get user model
	 * 
	 * Server side this may not yet be loaded, do JIT loading
	 */
	@Override
	public UserModel getOwner() {
		if(owner == null) {
			owner = ServerUserModel.findByUsername(ownerId, ServerMain.dbConnection);
		}
		return owner;
	}
	
	/**
	 * Store model to database
	 */
	@Override
	public void store() {
		try {
			if(id == -1) {
				// Insert
				Statement st = ServerMain.dbConnection.createStatement();

				st.executeUpdate(String.format(
						"INSERT INTO appointment(title, start_date, end_date, description, owner)"
						+" VALUES('%s', '%s', '%s', '%s', '%s')",						
						getName(), getFormattedDate(getTimeFrom()), getFormattedDate(getTimeTo()),
						getDescription(), getOwner().getUsername()), Statement.RETURN_GENERATED_KEYS);
				
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				id = rs.getInt(1);
			} else {
				// TODO Update
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getFormattedDate(Calendar c) {
		return String.format(
				"%d-%d-%d %d:%d:%d", 
				c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)				
		);
	}

}

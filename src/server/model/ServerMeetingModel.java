package server.model;

import java.sql.SQLException;
import java.sql.Statement;

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
				
				// TODO Date 
				st.executeUpdate(String.format(
						"INSERT INTO appointment(title, start_date, end_date, description, owner)"
						+" VALUES('%s', '%s', '%s', '%s', '%s')",						
						getName(), "", "", getDescription(), getOwner()));
				
				
			} else {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

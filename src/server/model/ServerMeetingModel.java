package server.model;

import java.sql.SQLException;
import java.sql.Statement;

import server.ServerMain;
import client.model.MeetingModel;

/**
 * Server Side MeetingModel with methods for reading and writing to server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerMeetingModel extends MeetingModel implements IServerModel {

	public ServerMeetingModel() {
		super();
	}
	
	@Override
	public IServerModel store() {
		try {
			if(id == -1) {
				// Insert
				Statement st = ServerMain.dbConnection.createStatement();
				
				st.executeUpdate("INSERT INTO appo ");
				
				
			} else {
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}

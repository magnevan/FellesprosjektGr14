package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import server.ServerMain;
import client.model.ActiveUserModel;
import client.model.NotificationModel;
import client.model.TransferableModel;

public class ServerActiveUserModel extends ActiveUserModel {

	/**
	 * Create a new ActiveUserModel from the given UserModel
	 * 
	 * @param user
	 */
	public ServerActiveUserModel(ServerUserModel user) {
		super(user.getUsername(), user.getEmail(), user.getFullName());
		notifications = null;
	}
	
	/**
	 * Read the model off stream
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ServerActiveUserModel(BufferedReader reader) throws IOException {
		super(reader);
	}
	
	/**
	 * Get notifications
	 * 
	 * Do JIT pull in
	 * 
	 */
	@Override
	public ArrayList<NotificationModel> getNotifications() {
		if(notifications == null) {	
			System.out.println("here");
			notifications = new ArrayList<NotificationModel>();
			
			try {
				ResultSet rs = ServerMain.dbConnection.preformQuery(String.format(
					"SELECT count(*) FROM notification WHERE `read`=0 AND given_to='%s';",
					getUsername()));
			
				rs.next();
				int count = Math.max(rs.getInt(1), 10);
				
				rs = ServerMain.dbConnection.preformQuery(String.format(
					"SELECT * FROM notification WHERE given_to='%s' " +
					"ORDER BY `read` ASC LIMIT %d",
					getUsername(), count
						));
				
				while(rs.next()) {
					notifications.add(new ServerNotificationModel(rs, ServerMain.dbConnection));
				}
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}			
		return notifications;
	}
	
}

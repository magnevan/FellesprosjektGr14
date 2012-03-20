package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	}
	
	/**
	 * Read the model off stream
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ServerActiveUserModel(BufferedReader reader,
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		super(reader, modelBuff);
	}
	
	/**
	 * Get notifications
	 * 
	 * Do JIT pull in
	 * 
	 */
	@Override
	public ArrayList<NotificationModel> getNotifications() {
		if(notifications == null)
			notifications = new ArrayList<NotificationModel>(); // TODO
		return notifications;
	}
	
}

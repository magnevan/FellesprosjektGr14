package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import server.ModelEnvelope;

/**
 * Class repsenting the currently logged on user
 * 
 * @author endre
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ActiveUserModel extends UserModel {

	public final static String NOTIFICATIONS_PROPERTY = "notifications";
	
	protected ArrayList<NotificationModel> notifications;
	
	/**
	 * Create a new ActiveUserModel 
	 * 
	 * @param username
	 * @param email
	 * @param fullName
	 */
	public ActiveUserModel(String username, String email, String fullName) {
		super(username, email, fullName);
		notifications = new ArrayList<NotificationModel>();
	}
	
	/**
	 * Create model from reader
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ActiveUserModel(BufferedReader reader, 
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		super(reader, modelBuff);
		notifications = new ArrayList<NotificationModel>();
		
		int n = Integer.parseInt(reader.readLine());
		for( ; n > 0; n-- )
			notifications.add((NotificationModel) modelBuff.get(reader.readLine())); 
	}

	/**
	 * Append a notification to the list
	 * 
	 * @param notification
	 */
	public void addNotification(NotificationModel notification) {
		notifications.add(notification);
		pcs.firePropertyChange(NOTIFICATIONS_PROPERTY, null, notification);		
	}

	/**
	 * Get all notifications
	 * 
	 * @return
	 */
	public ArrayList<NotificationModel> getNotifications() {
		return notifications;
	}
	
	/**
	 * Add dependant sub models
	 */
	@Override
	public void addSubModels(ModelEnvelope envelope) {
		super.addSubModels(envelope);
		for(NotificationModel n : getNotifications()) {
			envelope.addModel(n);
		}
	}
	
	/**
	 * Dump the content of the model to the StringBuffer
	 */
	@Override
	public void toStringBuilder(StringBuilder sb) {
		super.toStringBuilder(sb);
		sb.append(notifications.size() + "\r\n");
		for(NotificationModel n : notifications)
			sb.append(n.getUMID() + "\r\n");
	}

}

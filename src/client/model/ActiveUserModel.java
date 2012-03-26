package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import server.ModelEnvelope;

/**
 * Class repsenting the currently logged on user
 * 
 * @author endre
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ActiveUserModel extends UserModel {

	public final static String NOTIFICATIONS_PROPERTY = "notifications";
	public final static String NEW_STALKEE = "new stalked";
	public final static String REMOVED_STALKEE = "removed stalked";
	private ArrayList<UserModel> stalkingList;
	
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
		stalkingList = new ArrayList<UserModel>();
	}
		
	/**
	 * Create model from reader
	 * 
	 * @param reader
	 * @param modelBuff
	 * @throws IOException
	 */
	public ActiveUserModel(BufferedReader reader) throws IOException {
		super(reader);
		notifications = new ArrayList<NotificationModel>();
		int n = Integer.parseInt(reader.readLine());
		notificationsUMIDs = new String[n];
		for( ; n > 0; n-- ) {
			notificationsUMIDs[n-1] = reader.readLine();
		}
		stalkingList = new ArrayList<UserModel>();
	}

	private String[] notificationsUMIDs;
	
	/**
	 * Pull in sub models
	 */
	@Override
	public void registerSubModels(ModelEnvelope envelope) {
		if(notificationsUMIDs != null) {
			for(int i = notificationsUMIDs.length-1; i >= 0; i--) {
				notifications.add((NotificationModel) envelope.getFromBuffer(notificationsUMIDs[i]));
			}
		}
		notificationsUMIDs = null;
	}

	/**
	 * Append a notification to the list
	 * 
	 * @param notification
	 */
	public void addNotification(NotificationModel notification) {
		notifications.add(notification);
		//getCalendarModel().add(notification.getRegardsMeeting());
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
	
	/**
	 * Add a user to be stalked
	 * @param stalkee the user you want to stalk
	 */
	public void addToStalkingList(UserModel stalkee) {
		stalkingList.add(stalkee);
		pcs.firePropertyChange(NEW_STALKEE, null, stalkee);
	}
	
	/**
	 * Get a list of all the users you stalk
	 * @return ArrayList<UserModel> containing all the users you stalk
	 */
	public ArrayList<UserModel> getStalkingList() {
		return stalkingList;
	}
	
	/**
	 * Quit stalking a user
	 * @param stalkee the user you do not want to stalk anymore
	 */
	public void removeFromStalkingList(UserModel stalkee) {
		stalkingList.remove(stalkee);
		pcs.firePropertyChange(REMOVED_STALKEE, null, stalkee);
	}
}

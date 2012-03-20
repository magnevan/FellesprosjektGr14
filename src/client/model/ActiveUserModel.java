package client.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;


public class ActiveUserModel extends UserModel {

	public final static String NOTIFICATIONS_PROPERTY = "notifications";
	
	protected ArrayList<NotificationModel> notifications;
	protected PropertyChangeSupport changeSupport;
	
	public ActiveUserModel(String username, String email, String fullName) {
		super(username, email, fullName);
		changeSupport = new PropertyChangeSupport(this);
		notifications = new ArrayList<NotificationModel>();
	}
	
	public ActiveUserModel(UserModel model) {
		this(model.username,  model.email, model.fullName);
	}
	

	public void addNotification(NotificationModel notification) {

		notifications.add(notification);
		changeSupport.firePropertyChange(NOTIFICATIONS_PROPERTY, null, notification);
		
	}

	public ArrayList<NotificationModel> getNotifications() {
		return notifications;
	}

}

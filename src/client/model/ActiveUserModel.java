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

	public void addNotification(NotificationModel notification) {
		ArrayList<NotificationModel> oldValue = notifications;
		notifications.add(notification);
		
		//Ikke sikker på om dette blir riktig
		changeSupport.firePropertyChange(NOTIFICATIONS_PROPERTY, oldValue, this.notifications);
		
	}

	public ArrayList<NotificationModel> getNotifications() {
		return notifications;
	}

	
	
	
	
	
}

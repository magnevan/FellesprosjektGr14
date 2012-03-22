package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.*;

import client.ClientMain;
import client.gui.VerticalLayout;
import client.model.ActiveUserModel;
import client.model.MeetingModel;
import client.model.NotificationModel;
import client.model.NotificationType;

/**
 * The panel for displaying and handling notifications
 * 
 * @author Peter Ringset
 * @author Susanne Gustavesen
 *
 */
public class VarselPanel extends JPanel implements PropertyChangeListener {
	/*
	 * TODO: Legg til click-listener i lista
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4184187489458007088L;

	private JButton newAppointmentButton; //TODO legg denne til grafisk
	private NotificationList notificationList;
	private PropertyChangeSupport pcs;
	
	public VarselPanel(){
		super(new VerticalLayout(1, VerticalLayout.LEFT));		

		pcs = new PropertyChangeSupport(this);
		
		// Top content, the person label
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setPreferredSize(new Dimension(310, 50));
		PersonLabel personLabel = new PersonLabel();
		topPanel.add(personLabel);
		this.add(topPanel);
		
		
		// Center content, the notification list
		JLabel notifications = new JLabel("Varsler:");
		notifications.setAlignmentX(LEFT_ALIGNMENT);
		notifications.setAlignmentY(TOP_ALIGNMENT);
		this.add(notifications);
		notificationList = new NotificationList();
		notificationList.addPropertyChangeListener(this);
		notificationList.setPreferredSize(new Dimension(310, 485));
		this.add(notificationList);
		notifications.setLabelFor(notificationList);
	
		// Create a small space
		this.add(Box.createVerticalStrut(20));
		
		// Button at bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(308, 100));
		newAppointmentButton = new JButton("Opprett en avtale/møte");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		this.add(bottomPanel);
		
		ClientMain.client().getActiveUser().addPropertyChangeListener(this);
	}
	
	/**
	 * Receive notifications, put them in the notifications list
	 * @param model
	 */
	public void initializeList(ArrayList<NotificationModel> models) {
		notificationList.initializeList(models);
	}
	
	public void receiveNotification(NotificationModel model) {
		notificationList.addElement(model);
	}
	
	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ActiveUserModel.NOTIFICATIONS_PROPERTY) {
			receiveNotification((NotificationModel)evt.getNewValue());
		} else if (evt.getPropertyName() == NotificationList.NOTIFICATION_COUNT) {
			pcs.firePropertyChange(evt);
		} else if (evt.getPropertyName() == NotificationList.NOTIFICATION_CLICKED) {
			NotificationModel notification = (NotificationModel) evt.getNewValue();
			if (!notification.isRead()) {
				notification.setRead(true);
				if (notification.getType() != NotificationType.A_CANCELED) {
					MeetingModel meetingModel = notification.getRegardsMeeting();
					// TODO: fire a property change message to notify MainPanel to open the appointment
				} else {
					notification.setRead(true);
				}
			}
		}
	}
}

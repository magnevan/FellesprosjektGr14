package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.*;

import client.ClientMain;
import client.gui.VerticalLayout;
import client.model.ActiveUserModel;
import client.model.NotificationModel;

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
	
	public VarselPanel(){
		super(new VerticalLayout(1, VerticalLayout.LEFT));		

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
		
		initializeList(ClientMain.client().getActiveUser().getNotifications());
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

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName() == ActiveUserModel.NOTIFICATIONS_PROPERTY) {
			receiveNotification((NotificationModel)pce.getNewValue());
		}
	}
	
//	public static void main(String[] args) {		
//		JFrame frame = new JFrame("Varsel testing");
//		
//		VarselPanel panel = new VarselPanel();
//		
//		UserModel peter = new UserModel("peter", "peter@example.com", "Peter Ringset");
//		UserModel magne = new UserModel("magne", "magne@example.com", "Magne Magnesen");
//		
//		Calendar kl11 = Calendar.getInstance();
//		kl11.set(Calendar.HOUR_OF_DAY, 11);
//		kl11.set(Calendar.MINUTE, 0);
//		Calendar kl10 = Calendar.getInstance();
//		kl10.set(Calendar.HOUR_OF_DAY, 10);
//		kl10.set(Calendar.MINUTE, 0);
//		Calendar kl9 = Calendar.getInstance();
//		kl9.set(Calendar.HOUR_OF_DAY, 9);
//		kl9.set(Calendar.MINUTE, 0);
//		Calendar kl8 = Calendar.getInstance();
//		kl8.set(Calendar.HOUR_OF_DAY, 8);
//		kl8.set(Calendar.MINUTE, 0);
//		
//		MeetingModel m1 = new MeetingModel(kl8, kl11, magne);
//		m1.setName("Tidlig lunsj");
//		NotificationModel n1 = new NotificationModel(NotificationType.A_CANCELED, // type
//				peter, // given to
//				m1, // regards meeting
//				magne, // regards user
//				kl9, // time
//				true); // read
//		NotificationModel n2 = new NotificationModel(NotificationType.A_EDITED, // type
//				peter, // given to
//				m1, // regards meeting
//				magne, // regards user
//				kl8, // time
//				false); // read
//		NotificationModel n3 = new NotificationModel(NotificationType.A_INVITATION, // type
//				peter, // given to
//				m1, // regards meeting
//				magne, // regards user
//				kl10, // time
//				false); // read
//		NotificationModel n4 = new NotificationModel(NotificationType.A_USER_DENIED, // type
//				peter, // given to
//				m1, // regards meeting
//				magne, // regards user
//				kl11, // time
//				false); // read
//		
//		ArrayList<NotificationModel> existing = new ArrayList<NotificationModel>();
//		existing.add(n1);
//		existing.add(n2);
//
//		panel.initializeList(existing);
//		
//		frame.add(panel);
//		frame.pack();
//		frame.setVisible(true);
//		
//		panel.receiveNotification(n3);
//		panel.receiveNotification(n4);
//		
//	}
}

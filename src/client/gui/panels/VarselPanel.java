package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.*;

import client.gui.VerticalLayout;
import client.model.MeetingModel;
import client.model.NotificationModel;
import client.model.NotificationType;
import client.model.UserModel;

/**
 * The panel for displaying and handling notifications
 * 
 * @author Peter Ringset
 * @author Susanne Gustavesen
 *
 */
public class VarselPanel extends JPanel{
	/*
	 * TODO: Legg til click-listener i lista
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4184187489458007088L;

	private JButton newAppointmentButton; //TODO legg denne til grafisk
	private NotificationList notificationList;
	private JPanel centerPanel;
	
	public VarselPanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		notificationList = new NotificationList();
		
		//top
		JPanel topPanel = new JPanel();
		//PersonLabel personLabel = new PersonLabel();
		//topPanel.add(personLabel);
		
		JLabel notifications = new JLabel();
		notifications.setText("Varsler: ");
		
		//center over center
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(100,100));
		
		//Center
		centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(270,200));
		centerPanel.add(notificationList);
		
		//Bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(270,100));
		newAppointmentButton = new JButton("Opprett en avtale/møte");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		
		
		//add elements
		this.add(topPanel);
		this.add(label);
		this.add(notifications);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel);
		
		
	}
	
	/**
	 * Receive notifications, put them in the notifications list
	 * @param model
	 */
	public void receiveNotification(NotificationModel model) {
		notificationList.addElement(model);
	}
	
	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}
	
	public static void main(String[] args) {		
		JFrame frame = new JFrame("Varsel testing");
		
		VarselPanel panel = new VarselPanel();
		
		UserModel peter = new UserModel("peter", "peter@example.com", "Peter Ringset");
		UserModel magne = new UserModel("magne", "magne@example.com", "Magne Magnesen");
		
		Calendar kl11 = Calendar.getInstance();
		kl11.set(Calendar.HOUR_OF_DAY, 11);
		kl11.set(Calendar.MINUTE, 0);
		Calendar kl10 = Calendar.getInstance();
		kl10.set(Calendar.HOUR_OF_DAY, 10);
		kl10.set(Calendar.MINUTE, 30);
		
		MeetingModel lunch = new MeetingModel(	kl10,
												kl11,
												magne);
		lunch.setName("Tidlig lunsj");
		NotificationModel m1 = new NotificationModel(NotificationType.A_CANCELED, // type
				peter, // given to
				lunch, // regards meeting
				magne, // regards user
				Calendar.getInstance(), // time
				false); // read
		NotificationModel m2 = new NotificationModel(NotificationType.A_EDITED, // type
				peter, // given to
				lunch, // regards meeting
				magne, // regards user
				Calendar.getInstance(), // time
				true); // read
		NotificationModel m3 = new NotificationModel(NotificationType.A_INVITATION, // type
				peter, // given to
				lunch, // regards meeting
				magne, // regards user
				Calendar.getInstance(), // time
				true); // read
		NotificationModel m4 = new NotificationModel(NotificationType.A_USER_DENIED, // type
				peter, // given to
				lunch, // regards meeting
				magne, // regards user
				Calendar.getInstance(), // time
				true); // read
		
		panel.receiveNotification(m1);
		panel.receiveNotification(m2);
		panel.receiveNotification(m3);
		panel.receiveNotification(m4);
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}

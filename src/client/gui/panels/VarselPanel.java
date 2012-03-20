package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
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
	
	//private final JLabel nameLabel;
	private JList noteList;
	private DefaultListModel noteListModel;
	private JButton newAppointmentButton; //TODO legg denne til grafisk
	
	private ArrayList<NotificationModel> newNotifications, oldNotifications;
	
	public VarselPanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		// Instantiate lists for holding new and old notifications
		newNotifications = new ArrayList<NotificationModel>();
		oldNotifications = new ArrayList<NotificationModel>();
		
		//top
		JPanel topPanel = new JPanel();
		//PersonLabel personLabel = new PersonLabel();
		//topPanel.add(personLabel);
		
		JLabel notifications = new JLabel();
		notifications.setText("Varsler: ");
		
		//center over center
		JLabel label = new JLabel();
//		label.setPreferredSize(new Dimension(100,100));
		
		//Center
		JPanel centerPanel = new JPanel(new BorderLayout());
		noteList = new JList();
		noteListModel = new DefaultListModel();
		noteList.setModel(noteListModel);
//		centerPanel.setPreferredSize(new Dimension(270,200));
		centerPanel.add(noteList);
		JScrollPane scroll = new JScrollPane(noteList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
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
	 * Receive notifications and handle size of notifications list
	 * @param model
	 */
	public void receiveNotification(NotificationModel model) {
		if(model.isRead()) oldNotifications.add(model);
		else newNotifications.add(model);
		
		StringBuilder sb = new StringBuilder();
		sb.append(model.getRegardsUser().getFullName());
		noteListModel.addElement(model);
		
		int totalSize = oldNotifications.size() + newNotifications.size();
		while (totalSize > 10 && oldNotifications.size() > 0) {
			totalSize--;
			oldNotifications.remove(0);
		}
	}
	
	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}
	
	public static void main(String[] args) {		
		JFrame frame = new JFrame("Varsel testing");
		
		VarselPanel panel = new VarselPanel();
		
		UserModel peter = new UserModel("peter", "peter@example.com", "Peter Ringset");
		UserModel endre = new UserModel("endre", "endre@example.com", "Endre Endresen");
		UserModel magne = new UserModel("magne", "magne@example.com", "Magne Magnesen");
		
		Calendar kl11 = Calendar.getInstance();
		kl11.set(Calendar.HOUR, 11);
		Calendar kl10 = Calendar.getInstance();
		kl10.set(Calendar.HOUR, 10);
		
		MeetingModel lunch = new MeetingModel(	kl10,
												kl11,
												magne);
		
		NotificationModel m1 = new NotificationModel(NotificationType.A_EDITED, // type
													peter, // given to
													lunch, // regards meeting
													magne, // regards user
													Calendar.getInstance(), // time
													false); // read
		
		panel.receiveNotification(m1);
		
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}

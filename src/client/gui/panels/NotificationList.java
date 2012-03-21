package client.gui.panels;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import client.model.NotificationModel;

/**
 * Display NotificationListElem in a vertical list w/ scroll bar
 * 
 * @author Peter Ringset
 *
 */
public class NotificationList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2045270300264712032L;
	private static final int MAX_SIZE = 3;
	private JScrollPane scrollPane;
	private JList list;
	private DefaultListModel listModel;
	private ArrayList<NotificationModel> unread, read;

	public NotificationList() {
		unread = new ArrayList<NotificationModel>();
		read = new ArrayList<NotificationModel>();
		list = new JList();
		listModel = new DefaultListModel();
		list.setModel(listModel);
		list.setCellRenderer(new NotificationListCellRenderer());
		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(270,200));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane);
	}

	/**
	 * Initialize the list
	 * @param models
	 * 				An ArrayList containing all the NotificationModels
	 * 				that the user owns at login time
	 */
	public void initializeList(ArrayList<NotificationModel> models) {
		Collections.sort(models);
		for (NotificationModel notificationModel : models) {
			listModel.addElement(notificationModel);
			if (notificationModel.isRead()) read.add(notificationModel);
			else unread.add(notificationModel);
		}
	}
	
	/**
	 * Add a new notification to the list
	 * This routine will see to that the number of elements in the list
	 * is always less than MAX_SIZE as long as the number of unread notifications
	 * is less than MAX_SIZE.
	 * @param newNotification
	 * 			the notification to be added. It is presumed that this
	 * 			is an unread notification and that the notification's time stamp
	 * 			is newer than that of all existing notifications 
	 */
	public void addElement(NotificationModel newNotification) {
		if (unread.size() + read.size() >= MAX_SIZE && read.size() > 0) {
			for (int i = listModel.size() - 1; i >= 0; i--) {
				NotificationModel extract;
				extract = (NotificationModel) listModel.getElementAt(i);
				if (extract.isRead()) {
					listModel.removeElementAt(i);
					read.remove(extract);
				}
			}
		}
		listModel.add(0, newNotification);
		unread.add(newNotification);
	}
}

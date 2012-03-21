package client.gui.panels;

import java.awt.Dimension;
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
	private static final int MAX_SIZE = 10;
	private JScrollPane scrollPane;
	private JList list;
	private DefaultListModel listModel;
	private int unread, read;
	
	public NotificationList() {
		unread = 0;
		read = 0;
		list = new JList();
		listModel = new DefaultListModel();
		list.setModel(listModel);
		list.setCellRenderer(new NotificationListCellRenderer());
		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(270,200));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane);
	}
	
	public void addElement(NotificationModel newNotification) {
		if (listModel.size() == 0) listModel.addElement(newNotification);
		else {
			NotificationModel lastEntry = (NotificationModel) listModel.get(listModel.size()-1); 
			if (lastEntry.isRead()) {
				listModel.removeElement(lastEntry);
			}
			for (int i = 0; i < listModel.size(); i++) {
				if (newNotification.)
			}
		}
	}
}

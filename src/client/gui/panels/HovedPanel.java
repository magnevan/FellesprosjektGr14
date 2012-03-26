package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.ClientMain;
import client.gui.CheckListManager;
import client.gui.VerticalLayout;
import client.gui.panels.NotificationList.ListClickedListener;
import client.model.MeetingModel;
import client.model.NotificationModel;
import client.model.UserModel;

/**
 * Panel for the "Hoved" tab
 * @author Magne og Susanne
 *
 */
public class HovedPanel extends JPanel{
	
	private JList appointmentList;
	private final JButton newAppointmentButton;
	JLabel label = new JLabel();
	MeetingModel model;
	Calendar timeTo;
	Calendar timeFrom;
	
	public HovedPanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));
		// Top panel, the users icon, name and the logout button
		JPanel topPanel = new JPanel();
		PersonLabel personLabel = new PersonLabel();
		personLabel.setPreferredSize(new Dimension(310, 50));
		topPanel.add(personLabel);
		
		//Center panel
		final DefaultListModel lol = new DefaultListModel();
		List<MeetingModel> meetings = new ArrayList<MeetingModel>(
				ClientMain.getActiveUser().getCalendarModel().getMeetingsInDay(Calendar.getInstance())
				);
		Collections.sort(meetings, MeetingModel.timeFromComparator);
		for (MeetingModel m : meetings)
			lol.addElement(m);
		
		appointmentList = new JList(lol);
		
		System.out.println("ANTALL M¯TER I DAG " + lol.size());
		appointmentList.setCellRenderer(new MeetingModelRenderer());
		
		// Center content, a label, todays date and the list containing todays meetings
		JPanel centerContent = new JPanel(new VerticalLayout(5, SwingConstants.LEFT));
		centerContent.setPreferredSize(new Dimension(310, 503));
		
		centerContent.add(new JLabel("Dagens aktiviteter "));
		
		JLabel todaysDate = new JLabel();
		String dateString = now();
		todaysDate.setText(dateString);
		todaysDate.setFont(new Font("", Font.BOLD, 18));
		todaysDate.setForeground(Color.BLUE);
		centerContent.add(todaysDate);
		
		JScrollPane appointmentListScrollPane = new JScrollPane(appointmentList);
		appointmentListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		appointmentListScrollPane.setPreferredSize(new Dimension(310, 450));
		centerContent.add(appointmentListScrollPane);
		
		// Bottom panel, the button for creating a new meeting
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(306,100));
		newAppointmentButton = new JButton("Opprett en avtale/m¿te");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		
		//Today date
		JLabel label1 = new JLabel();
		String lol1 = now();
		label1.setText(lol1);
		label1.setFont(new Font("", Font.BOLD, 18));
		label1.setForeground(Color.BLUE);
		
		
		//adding elements
		this.add(topPanel);
		this.add(centerContent);
		this.add(bottomPanel);
	}

	public static final String DATE_FORMAT_NOW = "dd.MM.yyyy";
	
	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}
	class ListClickedListener implements MouseListener {
		
		int test;
		@Override
		public void mouseClicked(MouseEvent arg0) {
			appointmentList.getSelectedValue();
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
	
	}
	

}

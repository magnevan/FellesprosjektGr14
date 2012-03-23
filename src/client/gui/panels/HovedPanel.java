package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import javax.swing.SwingConstants;

import client.gui.VerticalLayout;
import client.model.MeetingModel;
import client.model.UserModel;

/**
 * Panel for the "Hoved" tab
 * @author Magne og Susanne
 *
 */
public class HovedPanel extends JPanel{
	
	private final JList appointmentList;
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
		
		DefaultListModel appointmentListModel = new DefaultListModel();		
		appointmentListModel.addElement(createMeetingModel(timeFrom, timeTo, "Styrem¿te"));
		appointmentListModel.addElement(createMeetingModel(timeFrom, timeTo, "Lunsjavtale"));
		appointmentListModel.addElement(createMeetingModel(timeFrom, timeTo, "Verksted"));
		
		appointmentList = new JList(appointmentListModel);
		appointmentList.setCellRenderer(new MeetingModelRenderer());
		
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
				
		// Add the panels
		this.add(topPanel);
		this.add(centerContent);
		this.add(bottomPanel);
	}
	
	public DefaultListModel addMettingModel(){
		return null;
	}
	
	public static MeetingModel createMeetingModel(Calendar timeFrom,Calendar timeTo, String name){
		MeetingModel model = new MeetingModel();
		
		model.setName(name);
		model.setTimeFrom(timeFrom);
		model.setTimeTo(timeTo);
		return model;
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
}

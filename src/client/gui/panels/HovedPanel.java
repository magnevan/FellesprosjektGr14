package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
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
		 
		//Top panel
		JPanel topPanel = new JPanel();
		PersonLabel personLabel = new PersonLabel();
		topPanel.add(personLabel);
		
		//Center panel
		JPanel centerPanel = new JPanel(new BorderLayout());
		DefaultListModel test = new DefaultListModel();
		
		test.addElement(createMeetingModel(timeFrom, timeTo, "Styrem¿te"));
		test.addElement(createMeetingModel(timeFrom, timeTo, "Lunsjavtale"));
		test.addElement(createMeetingModel(timeFrom, timeTo, "Verksted"));
		
		appointmentList = new JList(test);
		appointmentList.setCellRenderer(new MeetingModelRenderer());
		
		centerPanel.setPreferredSize(new Dimension(270,404));
		centerPanel.add(appointmentList);
		JScrollPane scroll = new JScrollPane(appointmentList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
		//Bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(270,100));
		newAppointmentButton = new JButton("Opprett en avtale/m¿te");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		
		//Today date
		JLabel label1 = new JLabel();
		String lol = now();
		label1.setText(lol);
		label1.setFont(new Font("", Font.BOLD, 18));
		label1.setForeground(Color.BLUE);
		
		//adding panels
		this.add(topPanel);
		this.add(new JLabel("Dagens aktiviteter "));
		this.add(label1);
		this.add(centerPanel);
		this.add(bottomPanel);
	}
	
	public DefaultListModel addMettingModel(){
		return null;
	}
	
	public static void main(String []args){
		JFrame frame = new JFrame();
		
		frame.setVisible(true);
		frame.add(new HovedPanel());
		frame.pack();
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
	return sdf.format(cal.getTime());}

	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}

	
}


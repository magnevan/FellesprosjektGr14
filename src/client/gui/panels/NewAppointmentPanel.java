package client.gui.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.gui.JDefaultTextArea;
import client.gui.JDefaultTextField;
import client.gui.JTimePicker;
import client.gui.VerticalLayout;
import client.gui.participantstatus.ParticipantStatusList;
import client.gui.usersearch.FilteredUserList;
import client.model.FilteredUserListModel;
import client.model.MeetingModel;

import com.toedter.calendar.JDateChooser;

public class NewAppointmentPanel extends JPanel {
	
	private final MeetingModel 			model;
	private final JTextField 			tittelText;
	private final JDateChooser 			dateChooser;
	private final JTimePicker 			fromTime, 
										toTime;
	private final JComboBox 			moteromComboBox;
	private final JTextField 			moteromText;
	private final JTextArea 			beskrivelseTextArea;
	private final FilteredUserList 		filteredUserList;
	private final JButton 				addEmployeeButton, 
										removeEmployeeButton;
	private final ParticipantStatusList participantList;
	private final JButton 				storeButton,
										deleteButton;
	
	public NewAppointmentPanel(MeetingModel meetingModel) {
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		this.model = meetingModel;
		
		//Tittel
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(model.getName(),26);
		this.add(tittelText);
		
		//Tid
		this.add(new JLabel("Tid"));
		JPanel tidPanel = new JPanel();
		dateChooser = new JDateChooser(model.getTimeFrom().getTime(), "dd. MMMM YYYY");
		dateChooser.setPreferredSize(new Dimension(130,20));
		tidPanel.add(dateChooser);
		
		fromTime = new JTimePicker(model.getTimeFrom());
		toTime = new JTimePicker(model.getTimeTo());
		
		tidPanel.add(fromTime);
		tidPanel.add(new JLabel(" - "));
		tidPanel.add(toTime);
		
		this.add(tidPanel);
		
		//Moterom
		this.add(new JLabel("Møterom"));
		JPanel moteromPanel = new JPanel();
		moteromComboBox = new JComboBox(new String[]{"","P15 rom 436","Torget","Hell","Oslo"});
		moteromText = new JDefaultTextField("Skriv møteplass...", 15);
		moteromPanel.add(moteromComboBox);
		moteromPanel.add(moteromText);
		
		this.add(moteromPanel);
		
		//Beskrivelse
		this.add(new JLabel("Beskrivelse:"));
		beskrivelseTextArea = new JDefaultTextArea("Skriv inn beskrivelse...", 4, 26);
		beskrivelseTextArea.setLineWrap(true);
		beskrivelseTextArea.setText(model.getDescription());
		this.add(beskrivelseTextArea);
		JScrollPane beskrivelseScroll = new JScrollPane(beskrivelseTextArea);
		beskrivelseScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(beskrivelseScroll);
		
		//Ansatte
		this.add(new JLabel("Ansatte:"));
		filteredUserList = new FilteredUserList(new FilteredUserListModel());
		filteredUserList.setPreferredSize(new Dimension(
					this.getPreferredSize().width,
					150
				));
		this.add(filteredUserList);
		
		//Legg til fjern knapper
		addEmployeeButton = new JButton("Legg til");
		removeEmployeeButton = new JButton("Fjern");
		
		JPanel addRemovePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		addRemovePanel.add(addEmployeeButton);
		addRemovePanel.add(Box.createHorizontalStrut(40));
		addRemovePanel.add(removeEmployeeButton);
		
		addRemovePanel.setPreferredSize(new Dimension(
					this.getPreferredSize().width,
					addRemovePanel.getPreferredSize().height
				));
		
		this.add(addRemovePanel);
		
		//Deltakere
		participantList = new ParticipantStatusList(meetingModel);
		participantList.setPreferredSize(new Dimension(
					this.getPreferredSize().width,
					150
				));
		this.add(participantList);
		
		//Lagre / Slett
		JPanel storeDelPane = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
		storeButton = new JButton("Lagre endringer");
		deleteButton = new JButton("Slett avtale");
		storeDelPane.add(storeButton);
		storeDelPane.add(deleteButton);
		
		this.add(storeDelPane);
		
		
	}

}

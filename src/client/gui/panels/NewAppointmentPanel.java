package client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.IServerResponseListener;
import client.ServerConnection;
import client.gui.JDefaultTextArea;
import client.gui.JDefaultTextField;
import client.gui.JTimePicker;
import client.gui.VerticalLayout;
import client.gui.participantstatus.ParticipantStatusList;
import client.gui.usersearch.FilteredUserList;
import client.model.FilteredUserListModel;
import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;
import client.model.UserModel;

import com.toedter.calendar.JDateChooser;

public class NewAppointmentPanel extends JPanel implements IServerResponseListener{
	
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
	private final FilteredUserListModel filteredUserListModel;
	
	private int meetingRoomReqID;
	
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
		moteromComboBox = new JComboBox();
		moteromText = new JDefaultTextField("Skriv mï¿½teplass...", 15);
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
		filteredUserListModel = new FilteredUserListModel();
		filteredUserList = new FilteredUserList(filteredUserListModel);
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
		
		
		//Listeners
		timeChangedListener listener = new timeChangedListener();
		dateChooser.addPropertyChangeListener(listener);
		fromTime.addActionListener(listener);
		toTime.addActionListener(listener);
		
		storeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {storeMeeting();}});
		deleteButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {deleteMeeting();}});
		
		addEmployeeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {addEmployee();}});
		removeEmployeeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {removeEmployee();}});
		
	}
	
	/**
	 * Prepare for garbage collection
	 */
	public void close() {
		participantList.close();
	}
	
	/**
	 * Checks if the time entered is valid, returns true and colors the font black if it's valid
	 * colors the font red if it's invalid.
	 * @return boolean
	 */
	private boolean isTimeValid() {
		if (toTime.compareTo(fromTime) != 1) {
			fromTime.setForeground(Color.RED);
			toTime.setForeground(Color.RED);
			return false;
		} else {
			fromTime.setForeground(Color.BLACK);
			toTime.setForeground(Color.BLACK);
			return true;
		}
	}
	
	private Calendar getFromTime() {
		Calendar from = Calendar.getInstance();
		from.setTime(dateChooser.getJCalendar().getDate());
		from.set(Calendar.HOUR_OF_DAY, fromTime.getHour());
		from.set(Calendar.MINUTE, fromTime.getMinute());
		from.set(Calendar.SECOND, 0);
		System.out.printf("Fromtime (%s)\n", from.getTime());
		return from;
	}
	
	private Calendar getToTime() {
		Calendar to = Calendar.getInstance();
		to.setTime(dateChooser.getJCalendar().getDate());
		to.set(Calendar.HOUR_OF_DAY, toTime.getHour());
		to.set(Calendar.MINUTE, toTime.getMinute());
		to.set(Calendar.SECOND, 0);
		return to;
	}
	
	private void requestMeetingRooms() {
		if (!isTimeValid()) return;
		
		Calendar 	from = getFromTime(), 
					to = getToTime();
		
		meetingRoomReqID = ServerConnection.instance().requestAvailableRooms(this, from, to);
	}
	
	private boolean isDataValid() {
		//Name
		if (tittelText.getText().length() == 0)
			return false;
		//Time
		if (!isTimeValid())
			return false;
		
		//Moteplass
		if (moteromComboBox.getSelectedIndex() != -1 && moteromText.getText() != "")
			return false;
			
		return true;
	}
	
	private void storeMeeting() {
		if (!isDataValid()) return;
		System.out.println("Store");
		//Name
		model.setName(tittelText.getText());
		//Date+time
		model.setTimeFrom(this.getFromTime());
		model.setTimeTo(this.getToTime());
		//Mï¿½teplass
		model.setRoom((MeetingRoomModel)moteromComboBox.getSelectedItem());
		model.setLocation(moteromText.getText());
		//Beskrivelse
		model.setDescription(beskrivelseTextArea.getText());
		
		//Invitasjoner
		model.commitInvitations();
		
		try {
			model.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void deleteMeeting() {
		if(model.getId() != -1) {
			try {
				model.delete();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		//throw new UnsupportedOperationException("Delete mï¿½te er ikke laget enda"); //TODO Hva skal denne gjï¿½re dersom mï¿½tet enda ikke er lagret?
	}
	
	private void addEmployee() {
		UserModel[] selUsers = filteredUserList.getSelectedUsers();
		filteredUserListModel.addUsersToBlacklist(selUsers);
		
		for (UserModel user : selUsers)
			model.addAttendee(user);
	}
	
	private void removeEmployee() {
		// filteredUserListModel.removeUsersFromBlacklist()
		throw new UnsupportedOperationException("");
	}
	
	class timeChangedListener implements ActionListener, PropertyChangeListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			requestMeetingRooms();
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			requestMeetingRooms();
		}
	}

	@Override
	public void onServerResponse(int requestId, Object data) {
		if (requestId == meetingRoomReqID) {
			List<MeetingRoomModel> rooms = (List<MeetingRoomModel>) data;
			
			MeetingRoomModel selectedRoom = (MeetingRoomModel) moteromComboBox.getSelectedItem();
			
			moteromComboBox.removeAllItems();
			
			moteromComboBox.addItem(null); //Et blankt valg om man ï¿½nsker ï¿½ heller sette lokasjon som tekst
			for (MeetingRoomModel room : rooms)
				moteromComboBox.addItem(room);
			
			if (selectedRoom == null) return;
			
			for (int i = 1; i < moteromComboBox.getItemCount(); i++) {
				MeetingRoomModel roomi = (MeetingRoomModel)moteromComboBox.getItemAt(i);
				
				if (roomi.getRoomNumber() == selectedRoom.getRoomNumber()) {
					moteromComboBox.setSelectedIndex(i);
					return;
				}
			}
				
		}
	}

}

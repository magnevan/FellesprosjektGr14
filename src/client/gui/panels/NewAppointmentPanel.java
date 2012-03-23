package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ClientMain;
import client.IServerResponseListener;
import client.ServerConnection;
import client.gui.JDefaultTextArea;
import client.gui.JDefaultTextField;
import client.gui.JTimePicker;
import client.gui.participantstatus.ParticipantStatusList;
import client.gui.usersearch.FilteredUserList;
import client.model.FilteredUserListModel;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;
import client.model.UserModel;

import com.toedter.calendar.JDateChooser;

public class NewAppointmentPanel extends JPanel implements IServerResponseListener{
	
	private /*final*/ MeetingModel 			model;
	private /*final*/ JTextField 			tittelText;
	private /*final*/ JDateChooser 			dateChooser;
	private /*final*/ JTimePicker 			fromTime, 
										toTime;
	private /*final*/ JComboBox 			moteromComboBox;
	private /*final*/ JTextField 			moteromText;
	private /*final*/ JTextArea 			beskrivelseTextArea;
	private       FilteredUserList 		filteredUserList;
	private       JButton 				addEmployeeButton, 
										removeEmployeeButton;
	private /*final*/ ParticipantStatusList participantList;
	private       JButton 				storeButton,
										deleteButton;
	private		  JButton				AcceptButton,
										DeclineButton,
										DeleteFromCalendarButton;
	
	
	private       FilteredUserListModel filteredUserListModel;
	
	private /*final*/ boolean isOwner;
	
	private int meetingRoomReqID;
	
	private MeetingRoomModel selectedRoom;
	
	private GridBagConstraints createConstraints(int gridx, int gridy) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx;
		c.gridy = gridy;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipadx = 5;
		c.ipady = 5;
		
		return c;
	}
	
	public NewAppointmentPanel(MeetingModel meetingModel) {
//		super(new VerticalLayout(5,SwingConstants.LEFT));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		int y = 0;
		this.setBackground(Color.GREEN);
		
		this.model = meetingModel;
		this.isOwner = meetingModel.getOwner() == ClientMain.getActiveUser();
		
		//Tittel
		c = createConstraints(0, y++);
		JLabel lbl = new JLabel("Tittel:", JLabel.LEFT);
		this.add(lbl, c);
		
		tittelText = new JTextField(model.getName(),26);
		tittelText.setEditable(isOwner);
		
		c = createConstraints(0, y++);
		c.weightx = 1.0;
		c.gridwidth = 4;
		this.add(tittelText, c);
		
		
		
		//Tid
		c = createConstraints(0, y++);
		this.add(new JLabel("Tid"), c);
		
		dateChooser = new JDateChooser(model.getTimeFrom().getTime(), "dd. MMMM YYYY");
		
		fromTime = new JTimePicker(model.getTimeFrom());
		toTime = new JTimePicker(model.getTimeTo());
		
		if (!isOwner) fromTime.setEditable(false);
		if (!isOwner) toTime.setEditable(false);
		
		c = createConstraints(0, y);
		c.weightx = 1.0;
		this.add(dateChooser, 			c);
		this.add(fromTime,				createConstraints(1, y));
		this.add(new JLabel(" - "),		createConstraints(2, y));
		this.add(toTime,				createConstraints(3, y++));
		
		
		//Moterom
		this.add(new JLabel("Møterom"), createConstraints(0, y++));
		moteromComboBox = new JComboBox();
		selectedRoom = model.getRoom();
		moteromComboBox.setSelectedItem(selectedRoom);
		moteromComboBox.setPreferredSize(new Dimension(
					100,
					moteromComboBox.getPreferredSize().height
				));
		
		moteromText = new JDefaultTextField("Skriv møteplass...", 15);
		moteromText.setText(model.getLocation());
		
		moteromComboBox.setEnabled(isOwner);
		moteromText.setEditable(isOwner);
		
		c = createConstraints(0, y);
		this.add(moteromComboBox, c);
		c = createConstraints(1, y);
		c.gridwidth = 3;
		this.add(moteromText,     c);
		
		
		/*
		//Beskrivelse
		this.add(new JLabel("Beskrivelse:"));
		beskrivelseTextArea = new JDefaultTextArea("Skriv inn beskrivelse...", 4, 26);
		beskrivelseTextArea.setLineWrap(true);
		beskrivelseTextArea.setText(model.getDescription());
//		this.add(beskrivelseTextArea);
		JScrollPane beskrivelseScroll = new JScrollPane(beskrivelseTextArea);
		beskrivelseScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		beskrivelseTextArea.setEditable(isOwner);
		
		this.add(beskrivelseScroll);
		
		//Ansatte
		if (isOwner) {
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
		}
		
		//Deltakere
		participantList = new ParticipantStatusList(meetingModel);
		participantList.setPreferredSize(new Dimension(
					this.getPreferredSize().width,
					150
				));
		this.add(participantList);
		
		//Lagre / Slett
		if (isOwner) {
			JPanel storeDelPane = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
			storeButton = new JButton("Lagre endringer");
			deleteButton = new JButton("Slett avtale");
			storeDelPane.add(storeButton);
			storeDelPane.add(deleteButton);
			
			this.add(storeDelPane);
		}
		
		//Godkjenn / avslå
		if (!isOwner) {
			JPanel buttonPane = new JPanel(new BorderLayout());
			AcceptButton = new JButton("Godkjenn");
			DeclineButton = new JButton("Avslå");
			DeleteFromCalendarButton = new JButton("Slett møte fra min kalender");
			
			JPanel AcceptDeclinePane = new JPanel(new BorderLayout());
			AcceptDeclinePane.add(AcceptButton, BorderLayout.WEST);
			AcceptDeclinePane.add(DeclineButton, BorderLayout.EAST);
			DeleteFromCalendarButton.setPreferredSize(new Dimension(
						AcceptButton.getPreferredSize().width + DeclineButton.getPreferredSize().width,
						DeleteFromCalendarButton.getPreferredSize().height
					));
			
			buttonPane.add(AcceptDeclinePane, BorderLayout.CENTER);
			buttonPane.add(DeleteFromCalendarButton, BorderLayout.SOUTH);
			
			buttonPane.setPreferredSize(new Dimension(
						(int)this.getPreferredSize().getWidth(),
						(int)buttonPane.getPreferredSize().getHeight()
					));
			
			this.add(buttonPane);
		}
		
		
		//Listeners
		timeChangedListener listener = new timeChangedListener();
		dateChooser.addPropertyChangeListener(listener);
		fromTime.addActionListener(listener);
		toTime.addActionListener(listener);
		
		if (isOwner) {
			storeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {storeMeeting();}});
			deleteButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {deleteMeeting();}});
		
			addEmployeeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {addEmployee();}});
			removeEmployeeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {removeEmployee();}});
		} else {
			//TODO legge til listeners for godkjenn, avslå og slett fra kalender
		}
		
		*/
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
		//Name
		model.setName(tittelText.getText());
		//Date+time
		model.setTimeFrom(this.getFromTime());
		model.setTimeTo(this.getToTime());
		//Møteplass
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
			
			selectedRoom = (MeetingRoomModel) moteromComboBox.getSelectedItem();
			
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

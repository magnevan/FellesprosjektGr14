package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
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
import javax.swing.SwingConstants;

import client.ClientMain;
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
import client.model.InvitationStatus;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;
import client.model.UserModel;

import com.toedter.calendar.JDateChooser;

public class NewAppointmentPanel extends JPanel 
	implements IServerResponseListener, PropertyChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6937333587055621358L;
	private final MeetingModel 			model;
	private final InvitationModel 		invitation;
	private final JTextField 			tittelText;
	private final JButton				closeButton;
	private final JDateChooser 			dateChooser;
	private final JTimePicker 			fromTime, 
										toTime;
	private final JComboBox 			moteromComboBox;
	private final JTextField 			moteromText;
	private final JTextArea 			beskrivelseTextArea;
	private       FilteredUserList 		filteredUserList;
	private       JButton 				addEmployeeButton, 
										removeEmployeeButton;
	private final ParticipantStatusList participantList;
	private       JButton 				storeButton,
										deleteButton;
	private		  JButton				acceptButton,
										declineButton,
										deleteFromCalendarButton;
	
	
	private       FilteredUserListModel filteredUserListModel;
	
	private final boolean isOwner;
	
	private int meetingRoomReqID;
	
	private MeetingRoomModel selectedRoom;
	
	public final static String CLOSE = "close";
	
	public NewAppointmentPanel(MeetingModel meetingModel) {
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		model = meetingModel;
		
		isOwner = meetingModel.getOwner().equals(ClientMain.getActiveUser());
		
		// Find invitation if we're not the owner
		if(!isOwner) {
			invitation = model.getInvitation(ClientMain.getActiveUser());
		} else {
			invitation = null;
		}
		
		//Tittel
		this.add(new JLabel("Tittel"));
		
		JPanel tittelPanel = new JPanel(new BorderLayout());
		tittelPanel.setPreferredSize(new Dimension(310, 30));
		tittelText = new JTextField(model.getName());
		tittelText.setEditable(isOwner);
		
		closeButton = new JButton("Close");
		
		tittelPanel.add(tittelText, BorderLayout.CENTER);
		tittelPanel.add(closeButton, BorderLayout.EAST);
		this.add(tittelPanel);
		
		//Tid
		this.add(new JLabel("Tid"));
		JPanel tidPanel = new JPanel();
		tidPanel.setLayout(new BoxLayout(tidPanel, BoxLayout.X_AXIS));
		tidPanel.setPreferredSize(new Dimension(310, 70));
		
		JPanel dateChooserPanel = new JPanel();
		dateChooser = new JDateChooser(model.getTimeFrom().getTime(), "dd. MMMM YYYY");
		dateChooser.setPreferredSize(new Dimension(130,20));
		dateChooser.setEnabled(isOwner);
		dateChooserPanel.add(dateChooser);

		JPanel fromToPanel = new JPanel();
		fromToPanel.setLayout(new BoxLayout(fromToPanel, BoxLayout.Y_AXIS));
		
		fromTime = new JTimePicker(model.getTimeFrom());
		fromTime.setAlignmentX(RIGHT_ALIGNMENT);
		JPanel fromPanel = new JPanel();
		fromPanel.setLayout(new BoxLayout(fromPanel, BoxLayout.X_AXIS));
		fromPanel.add(new JLabel("Fra kl "));
		fromPanel.add(fromTime);
		fromToPanel.add(fromPanel);
		
		
		toTime = new JTimePicker(model.getTimeTo());
		toTime.setAlignmentX(RIGHT_ALIGNMENT);
		JPanel toPanel = new JPanel();
		toPanel.setLayout(new BoxLayout(toPanel, BoxLayout.X_AXIS));
		toPanel.add(new JLabel("til kl "));
		toPanel.add(toTime);
		fromToPanel.add(toPanel);
		
		
		if (!isOwner) fromTime.setEnabled(false);
		if (!isOwner) toTime.setEnabled(false);
		
		tidPanel.add(dateChooserPanel);
		tidPanel.add(Box.createHorizontalGlue());
		tidPanel.add(fromToPanel);
		
		this.add(tidPanel);
		
		//Moterom
		this.add(new JLabel("M¿terom"));
		JPanel moteromPanel = new JPanel();
		moteromPanel.setPreferredSize(new Dimension(310, 30));
		moteromComboBox = new JComboBox();
		selectedRoom = model.getRoom();
		moteromComboBox.setSelectedItem(selectedRoom);
		moteromComboBox.setPreferredSize(new Dimension(
					150,
					moteromComboBox.getPreferredSize().height
				));
		
		moteromText = new JDefaultTextField("Skriv mï¿½teplass...", 11);
		moteromText.setText(model.getLocation());
		
		moteromComboBox.setEnabled(isOwner);
		moteromText.setEditable(isOwner);
		
		moteromPanel.add(moteromComboBox);
		moteromPanel.add(moteromText);
		
		this.add(moteromPanel);
		
		//Beskrivelse
		this.add(new JLabel("Beskrivelse:"));
		beskrivelseTextArea = new JDefaultTextArea("Skriv inn beskrivelse...", 2, 25);
		
		beskrivelseTextArea.setLineWrap(true);
		beskrivelseTextArea.setText(model.getDescription());
		JScrollPane beskrivelseScroll = new JScrollPane(beskrivelseTextArea);
		beskrivelseScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//beskrivelseTextArea.setEditable(isOwner);
		beskrivelseTextArea.setEnabled(isOwner);
		
		this.add(beskrivelseScroll);
		
		//Ansatte
		if (isOwner) {
			this.add(new JLabel("Ansatte:"));
			filteredUserListModel = new FilteredUserListModel();
			filteredUserListModel.addUsersToBlacklist(new UserModel[]{ClientMain.getActiveUser()});
			filteredUserList = new FilteredUserList(filteredUserListModel);
			filteredUserList.setPreferredSize(new Dimension(310, 150));
			this.add(filteredUserList);
			
			//Legg til fjern knapper
			addEmployeeButton = new JButton("Legg til");
			removeEmployeeButton = new JButton("Fjern");
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
			buttonPanel.setPreferredSize(new Dimension(310,30));
			addEmployeeButton = new JButton("Legg til");
			removeEmployeeButton = new JButton("Fjern");
			buttonPanel.add(Box.createHorizontalGlue());
			addEmployeeButton.setAlignmentX(LEFT_ALIGNMENT);
			buttonPanel.add(addEmployeeButton);
			removeEmployeeButton.setAlignmentX(RIGHT_ALIGNMENT);
			buttonPanel.add(removeEmployeeButton);
			buttonPanel.add(Box.createHorizontalGlue());
			this.add(buttonPanel);
		}
		
		//Deltakere
		participantList = new ParticipantStatusList(meetingModel);
		participantList.setPreferredSize(new Dimension(310, 150));
		this.add(participantList);
		
		//Lagre / Slett
		if (isOwner) {
			JPanel storeDelPane = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
			storeButton = new JButton("Lagre endringer");
			deleteButton = new JButton("Slett avtale");
			storeDelPane.add(storeButton);
			storeDelPane.add(deleteButton);
			
			this.add(storeDelPane);
		} else {
			// Accept/Decline
			JPanel acceptDeclinePanel = new JPanel();
			acceptDeclinePanel.setLayout(new BoxLayout(acceptDeclinePanel, BoxLayout.X_AXIS));
			acceptDeclinePanel.setPreferredSize(new Dimension(310, 30));
			acceptButton = new JButton("Godkjenn");
			declineButton = new JButton("AvslŒ");
			acceptDeclinePanel.add(Box.createHorizontalGlue());
			acceptButton.setAlignmentX(LEFT_ALIGNMENT);
			acceptDeclinePanel.add(acceptButton);
			declineButton.setAlignmentX(RIGHT_ALIGNMENT);
			acceptDeclinePanel.add(declineButton);
			acceptDeclinePanel.add(Box.createHorizontalGlue());
			
			deleteFromCalendarButton = new JButton("Slett m¿te fra min kalender");
			this.add(deleteFromCalendarButton);
			
			if(invitation.getStatus() == InvitationStatus.ACCEPTED) {
				acceptButton.setEnabled(false);
			}
			if(invitation.getStatus() == InvitationStatus.DECLINED) {
				declineButton.setEnabled(false);
			}
			
			JPanel acceptDeclineDeletePanel = new JPanel(new BorderLayout());
			acceptDeclineDeletePanel.add(acceptDeclinePanel, BorderLayout.NORTH);
			deleteFromCalendarButton.setPreferredSize(new Dimension(
						acceptButton.getPreferredSize().width + declineButton.getPreferredSize().width,
						deleteFromCalendarButton.getPreferredSize().height
					));
			
			acceptDeclineDeletePanel.add(deleteFromCalendarButton, BorderLayout.SOUTH);
			
			acceptDeclinePanel.setPreferredSize(new Dimension(
						(int)this.getPreferredSize().getWidth(),
						(int)acceptDeclinePanel.getPreferredSize().getHeight()
					));
			this.add(acceptDeclineDeletePanel);
		}
		
		
		//Listeners
		model.addPropertyChangeListener(this);
		if (invitation != null) invitation.addPropertyChangeListener(this);
		
		closeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {close();}});
		timeChangedListener listener = new timeChangedListener();
		dateChooser.addPropertyChangeListener(listener);
		fromTime.addActionListener(listener);
		toTime.addActionListener(listener);
		
		if (isOwner) {
			storeButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) { storeMeeting(); }});
			deleteButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) { deleteMeeting(); }});
			addEmployeeButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) { addEmployee(); }});
 			removeEmployeeButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) {removeEmployee(); }});
		} else {
			acceptButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) { changeInvitationStatus(InvitationStatus.ACCEPTED); }});
			declineButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) { changeInvitationStatus(InvitationStatus.DECLINED); }});
			deleteFromCalendarButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {deleteInvitation(); }});
		}
	}

	/**
	 * Delete an invitation
	 */
	private void deleteInvitation(){
		try {
			invitation.removePropertyChangeListener(this);
			invitation.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prepare for garbage collection
	 */
	public void close() {
		participantList.close();
		model.removePropertyChangeListener(this);
		if (invitation != null) invitation.removePropertyChangeListener(this);
		
		this.firePropertyChange(CLOSE, null, null);
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
			Toolkit.getDefaultToolkit().beep();
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
		if (tittelText.getText().length() == 0) {
			Toolkit.getDefaultToolkit().beep();
			System.out.println("Invalid title");
			return false;
		}
		//Time
		if (!isTimeValid()) {
			Toolkit.getDefaultToolkit().beep();
			System.out.println("Invalid time");
			return false;
		}
		
		//Moteplass
		if (moteromComboBox.getSelectedItem() != null && !moteromText.getText().equals("")) {
			Toolkit.getDefaultToolkit().beep();
			System.out.printf("Only use a single meeting room field, text is '%s'\n", moteromText.getText());
			return false;
		}
			
		return true;
	}
	
	private void storeMeeting() {
		if (!isDataValid()) return;

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
		
		System.out.println("Store!");
		try {
			model.store();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Delete a meeting
	 */
	private void deleteMeeting() {
		if(model.getId() != -1) {
			try {
				model.delete();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			model.setActive(false);
		}
	}
	
	/**
	 * Accept meeting invitation
	 */
	private void changeInvitationStatus(InvitationStatus status) {
		if(invitation != null) {
			invitation.setStatus(status);
			try {
				invitation.store();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Add employees to invitation list
	 * 
	 */
	private void addEmployee() {
		UserModel[] selUsers = filteredUserList.getSelectedUsers();
		filteredUserListModel.addUsersToBlacklist(selUsers);
		
		for (UserModel user : selUsers)
			model.addAttendee(user);
	}
	
	private void removeEmployee() {
		UserModel[] users = participantList.getSelectedUsers();		
		filteredUserListModel.removeUsersFromBlacklist(users);
		model.removeAttendees(users);
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

	@SuppressWarnings("unchecked")
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

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if(e.getPropertyName() == InvitationModel.STATUS_CHANGED) {
			if(invitation.getStatus() == InvitationStatus.ACCEPTED) {
				acceptButton.setEnabled(false);
				declineButton.setEnabled(true);
			}
			if(invitation.getStatus() == InvitationStatus.DECLINED) {
				acceptButton.setEnabled(true);
				declineButton.setEnabled(false);
			}
		}
		
	}

}

package client.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import client.gui.CheckListManager;
import client.gui.VerticalLayout;
import client.model.FilteredUserListModel;
import client.model.UserModel;
import client.gui.usersearch.FilteredUserList;


/**
 * Panel for the "Andre Kalendere" tab
 * @author Susanne
 * @author Peter Ringset
 *
 */
public class AndrePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5571042635925738029L;
	private final JList activeCalendersList;
	private final JButton upButton, downButton, newAppointmentButton;
	private final DefaultListModel activeCalendarsListModel;
	@SuppressWarnings("unused")
	private final CheckListManager checkListManager; 
	private final FilteredUserList filteredUserList;

	public AndrePanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		// Top panel, the users icon, name and the logout button
		JPanel topPanel = new JPanel();
		PersonLabel personLabel = new PersonLabel();
		personLabel.setPreferredSize(new Dimension(310, 50));
		topPanel.add(personLabel);
		
		// Center content, a label, the filtered user list, the add/remove-buttons and the list of active calendars
		JPanel centerContent = new JPanel(new VerticalLayout(5, SwingConstants.LEFT));
		centerContent.setPreferredSize(new Dimension(310, 503));

		JLabel employeesLabel = new JLabel();
		employeesLabel.setText("Ansatte");
		centerContent.add(employeesLabel);
		
		
		filteredUserList = new FilteredUserList(new FilteredUserListModel());
		filteredUserList.setPreferredSize(new Dimension(307,200));
		centerContent.add(filteredUserList);
				
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setPreferredSize(new Dimension(310,30));
		ButtonClickListener buttonClickListener = new ButtonClickListener();
		upButton = new JButton("Legg til");
		upButton.addActionListener(buttonClickListener);
		downButton = new JButton("Fjern");
		downButton.addActionListener(buttonClickListener);
		buttonPanel.add(Box.createHorizontalGlue());
		upButton.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.add(upButton);
		downButton.setAlignmentX(RIGHT_ALIGNMENT);
		buttonPanel.add(downButton);
		buttonPanel.add(Box.createHorizontalGlue());
		centerContent.add(buttonPanel);
		
		JLabel activeCalendarsLabel = new JLabel();
		activeCalendarsLabel.setText("Aktive kalendere");
		centerContent.add(activeCalendarsLabel);
		
		activeCalendarsListModel = new DefaultListModel();
		activeCalendersList = new JList(activeCalendarsListModel);
		checkListManager = new CheckListManager(activeCalendersList);
		activeCalendersList.setForeground(Color.black);
		JScrollPane activeCalendarsScrollPane = new JScrollPane(activeCalendersList);
		activeCalendarsScrollPane.setPreferredSize(new Dimension(310, 200));
		activeCalendarsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerContent.add(activeCalendarsScrollPane);
		 
		// Button at bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(306, 100));
		newAppointmentButton = new JButton("Opprett en avtale/m¿te");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		
		//add elements

		this.add(topPanel);
		this.add(centerContent);
		this.add(bottomPanel);


	}
	
	public static UserModel createUser(String userName, String email){
		
		UserModel person = new UserModel();
		return person;
	}
		
	public static UserModel createUser(String userName, String email, String fullName){
		
		UserModel person = new UserModel(userName, email, fullName);
		return person;
		
	}
	
	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}
	
	class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == upButton) {
				for (UserModel model : filteredUserList.getSelectedUsers())
					activeCalendarsListModel.addElement(model);
			} else if (evt.getSource() == downButton) {
				activeCalendarsListModel.removeElement(activeCalendersList.getSelectedValue());
			}
		}
		
	}
}

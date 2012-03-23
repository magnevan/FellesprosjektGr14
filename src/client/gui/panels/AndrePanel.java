package client.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;


import javax.swing.*;

import client.gui.JDefaultTextField;
import client.gui.VerticalLayout;
import client.model.FilteredUserListModel;
import client.model.UserModel;
import client.gui.usersearch.FilteredUserList;
import client.gui.usersearch.IFilteredUserListModel;

public class AndrePanel extends JPanel {
	
	//private final JLabel nameLabel;
	private final FilteredUserList employeeList;
	private final JList activeCalenders;
	private final JButton upButton, downButton, newAppointmentButton;
	private ListSelectionModel selectionModel = new DefaultListSelectionModel();
	final PersonLabel personLabel;
	UserModel person;
	ListModel lm;
	
	public AndrePanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		// Top content, the person label
		personLabel = new PersonLabel();
		personLabel.setPreferredSize(new Dimension(310, 50));
		this.add(personLabel);
		
		// Employees label
		JLabel ansatte = new JLabel();
		ansatte.setText("Ansatte");
		this.add(ansatte);
		
		// The employees list w/ search field
		FilteredUserListModel employeesModel = new FilteredUserListModel();
		employeeList = new FilteredUserList(employeesModel);
		employeeList.setPreferredSize(new Dimension(310, 150));
		this.add(employeeList);
		
//		person = new UserModel();
		
		//button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setPreferredSize(new Dimension(310,30));
		upButton = new JButton("Legg til");
		downButton = new JButton("Fjern");
		buttonPanel.add(Box.createHorizontalGlue());
		upButton.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.add(upButton);
		downButton.setAlignmentX(RIGHT_ALIGNMENT);
		buttonPanel.add(downButton);
		buttonPanel.add(Box.createHorizontalGlue());
		this.add(buttonPanel);
		
		//active calenders center
		JLabel aktiveKalendere = new JLabel();
		aktiveKalendere.setText("Aktive kalendere");
		
		//bottomPanel
		final JPanel bottomPanel = new JPanel(new BorderLayout());
		final DefaultListModel model2 = new DefaultListModel();
		activeCalenders = new JList(model2);
		activeCalenders.setCellRenderer(new CheckListCellRenderer(activeCalenders.getCellRenderer(), selectionModel));
		bottomPanel.setPreferredSize(new Dimension(270,100));
		bottomPanel.add(activeCalenders);
		JScrollPane scroll2 = new JScrollPane(activeCalenders);
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		bottomPanel.add(scroll2);
		
		JPanel addMeetingPanel = new JPanel(new BorderLayout());
		addMeetingPanel.setPreferredSize(new Dimension(270,100));
		newAppointmentButton = new JButton("Opprett en avtale/m�te");
		newAppointmentButton.setOpaque(true);
		addMeetingPanel.add(newAppointmentButton);
		
		
//		upButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				//model.addElement(new UserModel());
//				//model.addElement(createUser(person.getUsername(), person.getEmail()));
//				model2.addElement(employeeList.getSelectedValue()); 
//			}
//		});
//		
//		downButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				//int index = activeCalenders.getSelectedIndex();
//				model2.removeElement(activeCalenders.getSelectedValue());
// 			}
//		});
		
		
		//add elements
		
//		this.add(Box.createVerticalStrut(10));
//		this.add(buttonPanel);
//		this.add(Box.createVerticalStrut(2));
//		this.add(aktiveKalendere);
//		this.add(bottomPanel);
//		this.add(addMeetingPanel);
		
	}
	
	public ListSelectionModel getSelectionModel(){ 
        return selectionModel; 
  }
	
	public static UserModel createUser(String userName, String email, String fullName){
		
		UserModel person = new UserModel(userName, email, fullName);
		
		return person;
		
	}
	
	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}

}

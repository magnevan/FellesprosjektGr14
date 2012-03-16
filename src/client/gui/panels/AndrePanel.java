package client.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;


import javax.swing.*;

import client.gui.JDefaultTextField;
import client.gui.VerticalLayout;
import client.model.UserModel;
import client.gui.usersearch.FilteredUserList;
import client.gui.usersearch.IFilteredUserListModel;

public class AndrePanel extends JPanel {
	
	//private final JLabel nameLabel;
	private final JList employeeList;
	private final JList activeCalenders;
	private final JButton upButton, downButton, newAppointmentButton;
	private ListSelectionModel selectionModel = new DefaultListSelectionModel();
	final PersonLabel personLabel;
	final UserModel person;
	ListModel lm;
	
	public AndrePanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		

		//top
		JPanel topPanel = new JPanel();
		personLabel = new PersonLabel();
		topPanel.add(personLabel);
		
		//employee center
		JLabel ansatte = new JLabel();
		ansatte.setText("Ansatte");
		JDefaultTextField inputEmployee = new JDefaultTextField("Skriv navn eller epost til ansatt...", 21);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		final DefaultListModel model = new DefaultListModel();
		
		//adding some test persons
		model.addElement(createUser("Susanngu","susanngu@stud.ntnu.no"));
		model.addElement(createUser("Test", "test@test.no"));
		model.addElement(createUser("Test2","Test2@test.no"));
		
		person = new UserModel();
		employeeList = new JList(model);
		centerPanel.setPreferredSize(new Dimension(270,100));
		centerPanel.add(employeeList);
		JScrollPane scroll = new JScrollPane(employeeList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
		//button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setPreferredSize(new Dimension(250,60));
		upButton = new JButton("Legg til");
		downButton = new JButton("Fjern");
		buttonPanel.add(upButton);
		buttonPanel.add(Box.createHorizontalStrut(40));
		buttonPanel.add(downButton);
		
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
		newAppointmentButton = new JButton("Opprett en avtale/m¿te");
		newAppointmentButton.setOpaque(true);
		addMeetingPanel.add(newAppointmentButton);
		
		
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//model.addElement(new UserModel());
				//model.addElement(createUser(person.getUsername(), person.getEmail()));
				model2.addElement(employeeList.getSelectedValue()); 
			}
		});
		
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//int index = activeCalenders.getSelectedIndex();
				model2.removeElement(activeCalenders.getSelectedValue());
 			}
		});
		
		
		//add elements
		this.add(topPanel);
		this.add(Box.createVerticalStrut(30));
		this.add(ansatte);
		this.add(inputEmployee);
		this.add(centerPanel);
		this.add(Box.createVerticalStrut(10));
		this.add(buttonPanel);
		this.add(Box.createVerticalStrut(2));
		this.add(aktiveKalendere);
		this.add(bottomPanel);
		this.add(addMeetingPanel);
		
	}
	
	public ListSelectionModel getSelectionModel(){ 
        return selectionModel; 
  }
	
	public static UserModel createUser(String userName, String email){
		
		UserModel person = new UserModel();
		
		
		return person;
		
	}

}

package client.gui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.gui.CheckListManager;
import client.gui.JDefaultTextField;
import client.gui.VerticalLayout;
import client.gui.usersearch.FilteredUserList;
import client.model.FilteredUserListModel;
import client.model.UserModel;

/**
 * Panel for the "Andre Kalendere" tab
 * @author Susanne
 *
 */
public class AndrePanel extends JPanel{
	
	private final JList employeeList;
	private final JList activeCalenders;
	private final JButton upButton, downButton, newAppointmentButton;
	final PersonLabel personLabel;
	final UserModel person;
	static JCheckBox checkBox;
	 // make your JList as check list 
	CheckListManager checkListManager; 
	FilteredUserList search;
	
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
		
		//search panel
		search = new FilteredUserList(new FilteredUserListModel());
		search.setPreferredSize(new Dimension(270,100));
		person = new UserModel();
		
		//old employeelist panel
		JPanel centerPanel = new JPanel(new BorderLayout());
		final DefaultListModel model = new DefaultListModel();
		
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
		 
		 // to get checked items 
		// checkListManager.getSelectionModel().isSelectedIndex(index);
		
		//private final FilteredUserList search;
		
		final DefaultListModel lol = new DefaultListModel();
		activeCalenders = new JList(lol);
		checkListManager = new CheckListManager(activeCalenders);
		bottomPanel.setPreferredSize(new Dimension(270,100));
		//activeCalenders.setEnabled(false);
		activeCalenders.setForeground(Color.black);
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
				for (UserModel model : search.getSelectedUsers())
					lol.addElement(model);
				
				//lol.addElement(employeeList.getSelectedValue());
				
			}
		});
		
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//int index = activeCalenders.getSelectedIndex();

				lol.removeElement(activeCalenders.getSelectedValue());

				//model2.removeElement(activeCalenders.getSelectedValue());

 			}
		});
		
		//add elements
		this.add(topPanel);
		this.add(Box.createVerticalStrut(30));
		this.add(ansatte);
		this.add(search);
		//this.add(centerPanel);
		this.add(Box.createVerticalStrut(10));
		this.add(buttonPanel);
		this.add(Box.createVerticalStrut(2));
		this.add(aktiveKalendere);
		this.add(bottomPanel);
		this.add(addMeetingPanel);
		
	}
	

	public static UserModel createUser(String userName, String email){
		
		UserModel person = new UserModel();
		//userName = "Susanne";
		//email = "lool";
		//person.setName(userName);
		//person.setEmail(email);
		
		return person;
		
	}
		
		/*
		 * 
=======
	public ListSelectionModel getSelectionModel(){ 
        return selectionModel; 
  }
  */
	
	public static UserModel createUser(String userName, String email, String fullName){
		
		UserModel person = new UserModel(userName, email, fullName);
		
		return person;
		
	}
	
		 
}

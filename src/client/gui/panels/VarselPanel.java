package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import client.gui.VerticalLayout;

public class VarselPanel extends JPanel{
	
	//private final JLabel nameLabel;
	private final JList noteList;
	private JButton newAppointmentButton; //TODO legg denne til grafisk
	
	public VarselPanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		//top
		JPanel topPanel = new JPanel();
		PersonLabel personLabel = new PersonLabel();
		topPanel.add(personLabel);
		
		JLabel notifications = new JLabel();
		notifications.setText("Varsler: ");
		
		//center over center
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(100,100));
		
		//Center
		JPanel centerPanel = new JPanel(new BorderLayout());
		noteList = new JList(new String[] {"ingen varsler.. "});
		centerPanel.setPreferredSize(new Dimension(270,200));
		centerPanel.add(noteList);
		JScrollPane scroll = new JScrollPane(noteList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
		//Bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(270,100));
		newAppointmentButton = new JButton("Opprett en avtale/møte");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		
		
		//add elements
		this.add(topPanel);
		this.add(label);
		this.add(notifications);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel);
		
		
	}
	
	public JButton getNewAppointmentButton() {
		return newAppointmentButton;
	}
	
	

}

package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import client.gui.VerticalLayout;

public class VarselPanel extends JPanel{
	
	//private final JLabel nameLabel;
	private final JList noteList;
	
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
		
		
		//add elements
		this.add(topPanel);
		this.add(label);
		this.add(notifications);
		this.add(centerPanel, BorderLayout.CENTER);
		
		
	}
	
	
	
	

}

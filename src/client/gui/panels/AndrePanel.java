package client.gui.panels;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

import client.gui.VerticalLayout;

public class AndrePanel extends JPanel {
	
	private final JLabel nameLabel;
	private final JList employeeList;
	private final JList activeCalenders;
	private final JButton upButton, downButton;
	
	public AndrePanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		//top
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		//employee center
		JLabel ansatte = new JLabel();
		ansatte.setText("Ansatte");
		JTextField inputEmployee = new JTextField(21);
		inputEmployee.setText("Skriv navn eller epost til ansatt...");
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		employeeList = new JList(new String[] {"Ola Nordmann","Kari Hansen","Kari Larsen"});
		centerPanel.setPreferredSize(new Dimension(270,100));
		centerPanel.add(employeeList);
		JScrollPane scroll = new JScrollPane(employeeList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
		//buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setPreferredSize(new Dimension(250,60));
		upButton = new JButton("^");
		downButton = new JButton("v");
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		
		
		//active calenders center
		JLabel aktiveKalendere = new JLabel();
		aktiveKalendere.setText("Aktive kalendere");
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		
		activeCalenders = new JList(new String[] {"Ola Nordmann"});
		bottomPanel.setPreferredSize(new Dimension(270,100));
		bottomPanel.add(activeCalenders);
		JScrollPane scroll2 = new JScrollPane(activeCalenders);
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		bottomPanel.add(scroll2);
		
		
		
		//add elements
		this.add(nameLabel);
		this.add(Box.createVerticalStrut(30));
		this.add(ansatte);
		this.add(inputEmployee);
		this.add(centerPanel);
		this.add(buttonPanel);
		this.add(Box.createVerticalStrut(2));
		this.add(aktiveKalendere);
		this.add(bottomPanel);
		
		
	}

}

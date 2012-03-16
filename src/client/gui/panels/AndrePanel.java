package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.gui.JDefaultTextField;
import client.gui.VerticalLayout;

public class AndrePanel extends JPanel {
	
	private final JLabel nameLabel;
	private final JList employeeList;
	private final JList activeCalenders;
	private final JButton upButton, downButton;
	private ListSelectionModel selectionModel = new DefaultListSelectionModel();
	
	public AndrePanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		//top
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		//employee center
		JLabel ansatte = new JLabel();
		ansatte.setText("Ansatte");
		JDefaultTextField inputEmployee = new JDefaultTextField("Skriv navn eller epost til ansatt...", 21);
		//inputEmployee.setText("Skriv navn eller epost til ansatt...");
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		employeeList = new JList(new String[] {"Ola Nordmann","Kari Hansen","Kari Larsen"});
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
		
		final JPanel bottomPanel = new JPanel(new BorderLayout());
		
		activeCalenders = new JList(new String[] {"Ola Nordmann", "Kari Larsen", "Kari Hansen"});
		activeCalenders.setCellRenderer(new CheckListCellRenderer(activeCalenders.getCellRenderer(), selectionModel));
		bottomPanel.setPreferredSize(new Dimension(270,100));
		bottomPanel.add(activeCalenders);
		JScrollPane scroll2 = new JScrollPane(activeCalenders);
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		bottomPanel.add(scroll2);
		
		//Actions
		final JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(200,200));
		
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				label.setText("Ny person");
			}
		});
		
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				label.setText("");
			}
		});
		
		//add elements
		this.add(nameLabel);
		this.add(Box.createVerticalStrut(30));
		this.add(ansatte);
		this.add(inputEmployee);
		this.add(centerPanel);
		this.add(Box.createVerticalStrut(10));
		this.add(buttonPanel);
		this.add(Box.createVerticalStrut(2));
		this.add(aktiveKalendere);
		this.add(bottomPanel);
		this.add(label);
		
	}
	
	public ListSelectionModel getSelectionModel(){ 
        return selectionModel; 
  }

}

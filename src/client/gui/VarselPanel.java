package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

public class VarselPanel extends JPanel{
	
	private final JLabel nameLabel;
	private final JList noteList;
	
	public VarselPanel(){
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		//top
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
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
		this.add(nameLabel);
		this.add(label);
		this.add(notifications);
		this.add(centerPanel, BorderLayout.CENTER);
		
		
	}
	
	
	
	

}

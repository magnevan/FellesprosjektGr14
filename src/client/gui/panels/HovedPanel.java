package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import client.gui.VerticalLayout;

/**
 * Panel for the "Hoved" tab
 * @author Magne
 *
 */
public class HovedPanel extends JPanel{
	
	//private final JLabel nameLabel;
	//private final JButton logoutButton;
	private final JList appointmentList;
	private final JButton newAppointmentButton;
	
	public HovedPanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		//Top
		JPanel topPanel = new JPanel();
		PersonLabel personLabel = new PersonLabel();
		topPanel.add(personLabel);
		
		//Center
		JPanel centerPanel = new JPanel(new BorderLayout());
		appointmentList = new JList(new String[] {"testdata1","testdata2","testdata3","testdata4"});
		centerPanel.setPreferredSize(new Dimension(270,404));
		centerPanel.add(appointmentList);
		JScrollPane scroll = new JScrollPane(appointmentList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
		//Bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(270,100));
		newAppointmentButton = new JButton("Opprett en avtale/møte");
		newAppointmentButton.setOpaque(true);
		bottomPanel.add(newAppointmentButton);
		
		//adding panels
		this.add(topPanel);
		this.add(centerPanel);
		this.add(bottomPanel);
		
	}
	
}

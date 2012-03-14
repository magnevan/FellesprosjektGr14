package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * Panel for the "Hoved" tab
 * @author Magne
 *
 */
public class HovedPanel extends JPanel{
	
	private final JLabel nameLabel;
	private final JButton logoutButton;
	private final JList appointmentList;
	private final JButton newAppointmentButton;
	
	public HovedPanel() {
		super(new BorderLayout(0,10));
		
		//Top
		ImageIcon icon = new ImageIcon("src/resources/man_silhouette_clip_art_alt.png");
		nameLabel = new JLabel("Ola Nordmann", icon, SwingConstants.LEFT);
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		logoutButton = new JButton("Logg ut");
		JPanel logoutButtonPanel = new JPanel(); //this panel is here to avoid that the button stretches vertically
		logoutButtonPanel.add(logoutButton);
		
		JPanel topPanel = new JPanel(new BorderLayout(30,0));
		topPanel.add(nameLabel, BorderLayout.WEST);
		topPanel.add(logoutButtonPanel, BorderLayout.EAST);
		
		//Center
		JPanel centerPanel = new JPanel(new BorderLayout());
		appointmentList = new JList(new String[] {"testdata1","testdata2","testdata3","testdata4"});
		centerPanel.add(appointmentList, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(appointmentList);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);
		
		//Bottom
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(200,100));
		newAppointmentButton = new JButton("Opprett en avtale/møte");
		bottomPanel.add(newAppointmentButton, BorderLayout.CENTER);
		
		this.add(topPanel,    BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
}

package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WeekView extends JPanel {
	
	private static final long serialVersionUID = -8533878088518459485L;
	
	private JPanel[] days;
	private final Dimension dayDimension;
	
	public WeekView() {
		
		dayDimension = new Dimension(100,600);
		
		this.setLayout(new GridLayout(1,8,5,5));
		
		days = new JPanel[7];
		String[] weekdays = new String[]{"Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"};
		
		
		for (int i = 0; i < 7; i++) {
			days[i] = new JPanel();
			days[i].setPreferredSize(dayDimension);
			
			JLabel label = new JLabel(weekdays[i]);
			Font font = new Font("Courier New", Font.PLAIN, 12);
			label.setFont(font);
			
			days[i].add(label);
			days[i].add(new HourCell(100,50));
			if (i%2 == 0) days[i].setBackground(new Color(0x00E7E7E7));
			this.add(days[i]);
		}
	}
	
}

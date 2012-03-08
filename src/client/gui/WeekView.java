package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class WeekView extends JPanel{
	
	private JPanel[] days;
	private final Dimension dayDimension;
	
	public WeekView() {
		
		dayDimension = new Dimension(100,600);
		
		this.setLayout(new GridLayout(1,7,5,5));
		days = new JPanel[7];
		
		for (int i = 0; i < 7; i++) {
			days[i] = new JPanel();
			days[i].setPreferredSize(dayDimension);
//			days[i].setMinimumSize(dayDimension);
			if (i%2 == 0) days[i].setBackground(new Color(0x00C1C1C1));
			this.add(days[i]);
		}
	}
	
}

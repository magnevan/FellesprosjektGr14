package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class WeekViewInternal extends JPanel {

	private static final long serialVersionUID = 8940695225869678479L;

	public WeekViewInternal() {
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		JPanel hourColumn = new JPanel(new GridLayout(24,1));
		
		for (int hour = 0; hour < 24; hour++) {
			String txt = (hour < 10 ? "0" : "") + Integer.toString(hour) + ":00";
			JLabel label = new JLabel(txt,JLabel.CENTER);
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(35,50));
			p.add(label, BorderLayout.CENTER);
			hourColumn.add(p);
		}
		
		JPanel hourCellPanel = new JPanel();
		hourCellPanel.setLayout(new GridLayout(24,8));
		
		for (int i = 0; i < 24*7; i++)
			hourCellPanel.add(new HourCell(100,50));
		
		this.add(hourColumn);
		this.add(hourCellPanel);
		
	}
	
}

/*
String txt = (hour < 10 ? "0" : "") + Integer.toString(hour) + ":00";
JLabel label = new JLabel(txt,JLabel.CENTER);
label.setPreferredSize(new Dimension(50,10));
hour++;
*/
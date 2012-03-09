package client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class WeekViewInternal extends JPanel {

	private static final long serialVersionUID = 8940695225869678479L;

	public WeekViewInternal() {
		this.setLayout(new GridLayout(24,7));
		for (int i = 0; i < 24*7; i++)
			this.add(new HourCell(100,50));
		
		
	}
	
}

package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WeekView extends JPanel {
	
	private static final long serialVersionUID = -8533878088518459485L;
	
	private Calendar date;
	
	public WeekView() {
		
		date = Calendar.getInstance(); //Sets the default week to view as the current week
		
		this.setLayout(new BorderLayout());
		
		JPanel dayPanel = getDayPanel(date);
		JPanel dayPanelWithPadding = new JPanel();
		JPanel padding = new JPanel();
		padding.setPreferredSize(new Dimension(12,25));
		dayPanelWithPadding.add(padding);
		dayPanelWithPadding.add(dayPanel);
		WeekViewInternal wvi = new WeekViewInternal();
		
		this.add(dayPanelWithPadding, BorderLayout.NORTH);
		this.add(wvi, BorderLayout.CENTER);
		
		JScrollPane weekScroll = new JScrollPane(wvi);
		weekScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		weekScroll.setPreferredSize(new Dimension(755,600));
		this.add(weekScroll);
		
	}
	
	
	//Creates a JPanel containing 7 lables, writing out the days of the week given as input. e.g "Monday 9.jan Tuesday 10.jan ..."
	private JPanel getDayPanel(Calendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd.MMM");
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,8));
				
		//Setter dayOfWeek til å være datoen den første dagen i uken, bruker den til å loope og skrive dato
		Calendar dayOfWeek = (Calendar)date.clone();
		dayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		for (int i = 0; i < 7; i++) {
			JLabel label = new JLabel(sdf.format(dayOfWeek.getTime()), JLabel.LEFT);
			JPanel pLabel = new JPanel();
			pLabel.add(label);
			pLabel.setPreferredSize(new Dimension(100,25));
			
			dayOfWeek.add(Calendar.DAY_OF_WEEK, 1);
			p.add(pLabel);
		}
		
		return p;
	}
	
}

package client.gui;

import java.awt.BorderLayout;
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
		WeekViewInternal wvi = new WeekViewInternal();
		
		this.add(dayPanel, BorderLayout.NORTH);
		this.add(wvi, BorderLayout.CENTER);
		
		JScrollPane weekScroll = new JScrollPane(wvi);
		weekScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		weekScroll.setPreferredSize(new Dimension(718 + weekScroll.WIDTH,600));
		this.add(weekScroll);
		
	}
	
	
	//Creates a JPanel containing 7 lables, writing out the days of the week given as input. e.g "Monday 9.jan Tuesday 10.jan ..."
	private JPanel getDayPanel(Calendar date) {
		String[] weekdays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM");
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,7));
		
		//Setter dayOfWeek til å være datoen den første dagen i uken, bruker den til å loope og skrive dato
		Calendar dayOfWeek = (Calendar)date.clone();
		dayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		for (String day : weekdays) {
			JLabel label = new JLabel(day + " " + sdf.format(dayOfWeek.getTime()), JLabel.CENTER);
			label.setPreferredSize(new Dimension(100,25)); //TODO fjerne en del "magic numbers"
			
			dayOfWeek.add(Calendar.DAY_OF_WEEK, 1);
			p.add(label);
		}
		
		return p;
	}
	
}

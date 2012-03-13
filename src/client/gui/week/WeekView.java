package client.gui.week;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


/**
 * @author Magne
 */
public class WeekView extends JPanel {
	
	private static final long serialVersionUID = -8533878088518459485L;
	
	public static final int 
		HOURHEIGHT = 50,
		HOURWIDTH = 100,
		SHOWHOURS = 10;
	
	
	private final Calendar date;
	private final JScrollPane weekScroll;
	
	public WeekView() {
		
		date = Calendar.getInstance(); //Sets the default week to view as the current week
		
		this.setLayout(new BorderLayout());
		
		JPanel dayPanel = createDayPanel(date);
		JPanel dayPanelWithPadding = new JPanel(); //Because of the field on the left side that contains the times, e.g. "13:00", we need some extra padding.
		JPanel padding = new JPanel();
		padding.setPreferredSize(new Dimension(12,25));
		dayPanelWithPadding.add(padding);
		dayPanelWithPadding.add(dayPanel);
		JPanel wvi = createWeekViewInternal();
		
		this.add(dayPanelWithPadding, BorderLayout.NORTH);
		this.add(wvi, BorderLayout.CENTER);
		
		weekScroll = new JScrollPane(wvi);
		weekScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		weekScroll.setPreferredSize(new Dimension(HOURWIDTH*7 + 55,HOURHEIGHT*SHOWHOURS));
		this.add(weekScroll);
	}
	
	
	/**
	 * 
	 * @param hour The hour it should focus on, between 0 and 23 inclusive.
	 * @throws Exception
	 */
	public void focusOnHour(int hour) {
		if (hour > 23 || hour < 0) return;
		
		final JScrollBar vs = weekScroll.getVerticalScrollBar();
		
		if (hour < SHOWHOURS/2) hour = SHOWHOURS/2;
		if (hour > (23-SHOWHOURS/2)) hour = (23-SHOWHOURS/2);
		hour -= SHOWHOURS/2;
		
		System.out.println(hour);
		
//		It seems like getMaximum returns only half of the maximum. Dividing by two to compensate
		vs.setValue(
				hour * vs.getMaximum() / 2 / SHOWHOURS
				);
	}
	
	/**
	 * Creates a JPanel containing 7 labels, writing out the days of the week given as input. e.g "Monday 9.jan Tuesday 10.jan ..."
	 * @param date A date which you want the JPanel to focus around. Note only the input week is relevant. Tuesday the 6th as input is the same as Thursday the 8th of the same week.
	 * @return
	 */
	private JPanel createDayPanel(Calendar date) {
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
			pLabel.setPreferredSize(new Dimension(HOURWIDTH,HOURHEIGHT/2));
			
			dayOfWeek.add(Calendar.DAY_OF_WEEK, 1);
			p.add(pLabel);
		}
		
		return p;
	}
	
	/**
	 * Creates a JPanel filled with HourCells and timestamps to the left.
	 * @return
	 */
	private JPanel createWeekViewInternal() {
		JPanel weekViewInternal = new JPanel();
		
		weekViewInternal.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		JPanel hourColumn = new JPanel(new GridLayout(24,1));
		
		for (int hour = 0; hour < 24; hour++) {
			String txt = (hour < 10 ? "0" : "") + Integer.toString(hour) + ":00";
			JLabel label = new JLabel(txt,JLabel.CENTER);
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(35,HOURHEIGHT));
			p.add(label, BorderLayout.NORTH);
			hourColumn.add(p);
		}
		
		JPanel hourCellPanel = new JPanel();
		hourCellPanel.setLayout(new GridLayout(24,8));
		
		for (int i = 0; i < 24*7; i++) {
			HourCell hc = new HourCell(i / 7, HOURWIDTH,HOURHEIGHT);
			hourCellPanel.add(hc);
		}
		
		weekViewInternal.add(hourColumn);
		weekViewInternal.add(hourCellPanel);
		
		return weekViewInternal;
	}
}

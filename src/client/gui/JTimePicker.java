package client.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.JComboBox;

public class JTimePicker extends JComboBox implements Comparable<JTimePicker>{
	
	private static final long serialVersionUID = -536854656805235663L;
	
	private final HashSet<String> contains;
	
	private JTimePicker(String startTime) {
		
		contains = new HashSet<String>();
		
		for (int h = 0; h < 24; h++) {
			String e = format(h);
			addItem(e + ":00");
			addItem(e + ":15");
			addItem(e + ":30");
			addItem(e + ":45");
		}
		
		if (contains.contains(startTime)) {
			setSelectedItem(startTime);
		} else {
			setSelectedItem("08:00");
		}
	}
	
	public JTimePicker(Calendar time) {
		this(format(time.get(Calendar.HOUR_OF_DAY)) + ":" + format((int)(time.get(Calendar.MINUTE) / 15) * 15));
	}
	

	public JTimePicker() {
		this(fixTime(Calendar.getInstance()));
	}
	
	public int getHour() {
		String[] val = ((String)this.getSelectedItem()).split(":");
		return Integer.parseInt(val[0]);
	}
	
	public int getMinute() {
		String[] val = ((String)this.getSelectedItem()).split(":");
		return Integer.parseInt(val[1]);
	}
	
	private static String format(int i) {
		return (i < 10 ? "0" : "") + Integer.toString(i);
	}
	
	private static Calendar fixTime(Calendar time) {
		int hour = Math.round(time.get(Calendar.HOUR_OF_DAY) / 15) * 15;
		time.set(Calendar.HOUR_OF_DAY, hour);
		return time;
	}
	
	@Override
	public void addItem(Object item) {
		super.addItem(item);
		contains.add((String) item);
	}

	@Override
	public int compareTo(JTimePicker B) {
		JTimePicker A = this;
		
		int hourA = A.getHour();
		int minA = A.getMinute();
		int hourB = B.getHour();
		int minB = B.getMinute();
		
		if 			(hourA < hourB) {
			return -1;
		} else if 	(hourA > hourB) {
			return 1;
		}
			
		if 			(minA < minB) {
			return -1;
		} else if 	(minA > minB) {
			return 1;
		}
		
		return 0;
	}

}

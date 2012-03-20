package client.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.JComboBox;

public class JTimePicker extends JComboBox {
	
	private static final long serialVersionUID = -536854656805235663L;
	
	private final HashSet<String> contains;
	
	public JTimePicker(String startTime) {
		
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
		this(format(time.get(Calendar.HOUR)) + ":" + format(time.get(Calendar.MINUTE)));
	}
	

	public JTimePicker() {
		this("08:00");
	}
	
	private static String format(int i) {
		return (i < 10 ? "0" : "") + Integer.toString(i);
	}
	
	@Override
	public void addItem(Object item) {
		super.addItem(item);
		contains.add((String) item);
	}

}

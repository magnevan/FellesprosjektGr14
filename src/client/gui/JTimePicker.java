package client.gui;

import java.util.HashSet;

import javax.swing.JComboBox;

public class JTimePicker extends JComboBox<String> {
	
	private static final long serialVersionUID = -536854656805235663L;
	
	private final HashSet<String> contains;

	public JTimePicker(String startTime) {
		
		contains = new HashSet<String>();
		
		for (int h = 0; h < 24; h++) {
			String e = (h < 10 ? "0" : "") + Integer.toString(h);
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
	
	public JTimePicker() {
		this("08:00");
	}
	
	@Override
	public void addItem(String item) {
		super.addItem(item);
		contains.add(item);
	}

}

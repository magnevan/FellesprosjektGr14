package client.gui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import client.model.MeetingModel;

public class MeetingModelRenderer extends DefaultListCellRenderer implements ListCellRenderer {
	
//	public static final String HTML_1 = "<html><body style='width: ";
//	public static final String HTML_2 = "px'>";
//	public static final String HTML_4 = "</br>";
//	public static final String HTML_3 = "</html>";
	
	JLabel label3, label2;
	MeetingModel model;
	//JTextArea area;
	
	public MeetingModelRenderer(){
//		label3 = new JLabel();
		label2 = new JLabel();
//		area = new JTextArea();
		this.add(label2);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		MeetingModel model = (MeetingModel)value;
		
		label2 = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if(model != null)
			setModel(model);
		setModel((MeetingModel)value);
		
		
//		String text = HTML_1 + String.valueOf(50) + HTML_2 + value.toString()
//	            + HTML_3;
//
//		return super.getListCellRendererComponent(list, text, index, isSelected,
//	            cellHasFocus);
		
		return this;
		
	}
	
	public void setModel(MeetingModel model){
		
		this.model = model;
		String timeFrom;
		String timeTo = now();
		timeFrom = now();
		//label3.setText();
		label2.setText(timeFrom + " - " + timeTo +"      "+ this.model.getName());
		//area.add(label3);
		//area.add(label2);
		
		label2.setPreferredSize(new Dimension(150,30));
		//label3.setFont(new Font("", Font.PLAIN,12));
		
		//label.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.white));
		//label.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public static final String DATE_FORMAT_NOW = "HH:mm";
	
	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		
		return sdf.format(cal.getTime());}
}



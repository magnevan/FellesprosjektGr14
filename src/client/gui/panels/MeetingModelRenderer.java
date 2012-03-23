package client.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;

import client.model.CalendarModel;
import client.model.MeetingModel;

public class MeetingModelRenderer extends DefaultListCellRenderer {
	
	JLabel label3, label2;
	private CalendarModel calendarModel;
	MeetingModel model;
	
	public MeetingModelRenderer(){
		label2 = new JLabel();
		add(label2);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		
		label2 = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		//if(model != null)
		setModel(calendarModel);
		setModel((CalendarModel)value);
		
		return this;
	}
	
	public void setModel(CalendarModel calendarModel){
		this.calendarModel = calendarModel;
		
		Calendar fromTime = model.getTimeFrom();
		Calendar toTime = model.getTimeTo();
		
		label2.setText((calendarModel.getMeetingInterval(fromTime , toTime)) + "     " + model.getName());
		label2.setVisible(true);
		//label2.setPreferredSize(new Dimension(150,30));
		
		//label.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.white));
		//label2.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	//lager og returnerer et stringformat til start og slutt av avtalen
		private String timeToString(Calendar S, Calendar E){
			String tempString= "";
			
			SimpleDateFormat  sdf = new SimpleDateFormat ("HH:mm");
			if (S != null && E != null) {
				tempString = sdf.format(S.getTime()) + " - " + sdf.format(E.getTime());
			}

			return tempString;
		}
}



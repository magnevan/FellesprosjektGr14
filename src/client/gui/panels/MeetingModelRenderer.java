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
import client.model.NotificationModel;

public class MeetingModelRenderer extends DefaultListCellRenderer {
	
	
	public MeetingModelRenderer(){
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		MeetingModel model = (MeetingModel)value;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel timeStamp = new JLabel(sdf.format(model.getTimeFrom().getTime()) + " -  " + sdf.format(model.getTimeTo().getTime())+"     " + model.getName() );
		
		panel.add(timeStamp);
		
		return panel;
	}
	
	public void setModel(CalendarModel calendarModel){
		//label2.setPreferredSize(new Dimension(150,30));
		
		//label.setBorder(BorderFactory.createEtchedBorder(Color.black, Color.white));
		//label2.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	//lager og returnerer et stringformat til start og slutt av avtalen
}



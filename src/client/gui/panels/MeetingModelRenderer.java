package client.gui.panels;

import java.awt.BorderLayout;
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
		
		//Color bgColor = isSelected ? new Color(100, 100, 255) : Color.WHITE;
		
		MeetingModel model = (MeetingModel)value;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JLabel timeStamp = new JLabel(sdf.format(model.getTimeFrom().getTime()) + " - " + sdf.format(model.getTimeTo().getTime())+"     " + model.getName() );
		timeStamp.setPreferredSize(new Dimension(100,40));
		panel.add(timeStamp, BorderLayout.CENTER);
		
		return panel;
	}
	
}



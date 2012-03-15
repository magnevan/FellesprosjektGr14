package client.gui.avtale;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import client.gui.week.WeekView;

public class AppointmentPanel extends JPanel {

	private String name, ownerName, location;
	private GridBagConstraints c;
	private Calendar start, end;
	private JLabel nameLabel, ownerLabel, timeLabel, iconLabel, locationLabel;
	private Color color;
	private ImageIcon typeIcon;
	//private Avtale model;
	
	
	public AppointmentPanel(){
		
		//model = null;
		
		//Foreløpig testverdier
		ownerName= "Ola Nordmann";
		start = Calendar.getInstance();
		start.set(2012, 1, 9, 10, 0);
		end = Calendar.getInstance();
		end.set(2012, 1, 9, 11, 0);
		location = "Rom 101";
		typeIcon = new ImageIcon("src/resources/avtaleMini.png");
		name = "Ny avtale";
		
		color=new Color(0,100,255);
		setBackground(color);
		
		setLayout(new GridBagLayout());
    	c = new GridBagConstraints();
    	
    	c.gridx = 0;
    	c.gridy = 0;
    	c.fill = c.HORIZONTAL;
	    nameLabel = new JLabel(name);
	    nameLabel.setFont(new Font("Dialog", 1, 17));
		add(nameLabel,c);
		
		c.gridy = 1;
	    locationLabel = new JLabel(location);
		add (locationLabel,c);
		
		c.gridy = 2;
	    timeLabel = new JLabel(timeToString(start,end));
		add (timeLabel,c);
		
		
		
		c.gridy = 3;
	    iconLabel = new JLabel();
	    iconLabel.setIcon(typeIcon);
		add(iconLabel,c);
		
		
		c.gridy = 4;
	    ownerLabel = new JLabel(ownerName);
		add(ownerLabel,c);
		
		setAppointmentTime(start, end);

	}
	
	private String timeToString(Calendar S, Calendar E){
		String tempString= "";
		
		SimpleDateFormat  sdf = new SimpleDateFormat ("HH:mm");
		if (S != null && E != null) {
			tempString = sdf.format(S.getTime()) + " - " + sdf.format(E.getTime());
		}

		return tempString;
	}
	
	private void setFont(String font, int size){
		nameLabel.setFont(new Font(font, 1, size + 3));
		ownerLabel.setFont(new Font("Dialog", 1, size));
		timeLabel.setFont(new Font("Dialog", 1, size));
		iconLabel.setFont(new Font("Dialog", 1, size));
		locationLabel.setFont(new Font("Dialog", 1, size));
	}
	
	private void setAppointmentTime(Calendar S, Calendar E){
		
		long SMilli = S.getTimeInMillis();
		long EMilli = E.getTimeInMillis();
		
		int minutes = (int)((EMilli-SMilli)/60000);
		
		System.out.println(minutes);
		
		int AppointmentLength = (minutes/60)*WeekView.HOURHEIGHT;
		
		System.out.println(AppointmentLength);
		
		this.setPreferredSize(new Dimension(WeekView.HOURWIDTH - 5, AppointmentLength));
		this.setMinimumSize(new Dimension(WeekView.HOURWIDTH - 5, AppointmentLength));
		this.setMaximumSize(new Dimension(WeekView.HOURWIDTH - 5, AppointmentLength));

		
//Kjapp test av hvordan justering av avtale kan gjøres LOL!
		if(minutes == 15){
			setView(true, false, false,false, false, 10);
		}
		else if(minutes ==30){
			setView(true, true, false,false, false, 10);
		}
		else if(minutes == 45){
			setView(true, true, false,true, false, 10);

		}
		else if(minutes == 60 && minutes == 75){
			setView(true, true, true,true, false, 10);
		}
		else if(minutes == 90){
			setView(true, true, true,true, true, 10);
		}
		else{
			setView(true, true, true,true, true, 13);
		}
		

	}
	
	private void setView(boolean name, boolean location, boolean time, boolean icon ,boolean owner, int fontSize){
		nameLabel.setVisible(name);
		locationLabel.setVisible(location);
		timeLabel.setVisible(time);
		iconLabel.setVisible(icon);
		ownerLabel.setVisible(owner);
		setFont("Times New Roman",fontSize);
	}
	
//	public void setModel(Avtale a){
//		model = a;
//	}
//	
//	public Avtale getModel(){
//		return model;
//	}
	
	public static void main (String args[]) { 
        JFrame frame = new JFrame(""); 
        AppointmentPanel ap = new AppointmentPanel();
        frame.getContentPane().add(ap); 
        frame.pack();  
        frame.setVisible(true);   
    }
}

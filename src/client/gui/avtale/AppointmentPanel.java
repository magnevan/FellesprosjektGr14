package client.gui.avtale;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import client.gui.week.WeekView;
import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;

public class AppointmentPanel extends JPanel implements PropertyChangeListener {


	private GridBagConstraints c;
	private JLabel nameLabel, ownerLabel, timeLabel, iconLabel, locationLabel;
	private Color color;
	private MeetingModel model;
	
	
	public AppointmentPanel(MeetingModel MM){
		
		
		color=new Color(0,100,255);
		setBackground(color);
		
		setLayout(new GridBagLayout());
    	c = new GridBagConstraints();
    	
    	c.gridx = 0;
    	c.gridy = 0;
    	//c.fill = c.HORIZONTAL;
	    nameLabel = new JLabel();
		add(nameLabel,c);
		
		c.gridy = 1;
	    locationLabel = new JLabel();
		add (locationLabel,c);
		
		c.gridy = 2;
	    timeLabel = new JLabel();
		add (timeLabel,c);
		
		c.gridy = 3;
	    iconLabel = new JLabel();
		add(iconLabel,c);
		
		
		c.gridy = 4;
	    ownerLabel = new JLabel();
		add(ownerLabel,c);
		
		setModel(MM);

	}
	

	public void setModel(MeetingModel model){
		this.model = model;
		
		//Sletting av avtale
		if(!model.isActive()){
			//trenger en metode som fjerner AppointmentPanel
			
			return;
		}
		
		nameLabel.setText(model.getName());

		if(!model.getLocation().equals("Annet")){locationLabel.setText(model.getLocation());}
		else{locationLabel.setText(model.getRoom().getRoomNumber());}
			
		timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
		iconLabel.setIcon(getIcon(model.getInvitations()));
		ownerLabel.setText(model.getOwner().getFullName());
		
		setAppointmentTime(model.getTimeFrom(), model.getTimeTo());
		
	}
		
	public MeetingModel getModel(){
		return model;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		final String name = event.getPropertyName();
		
		if (name == MeetingModel.NAME_PROPERTY){
			nameLabel.setText(model.getName());
		}
		if (name == MeetingModel.ROOM_PROPERTY){
			locationLabel.setText(model.getRoom().getRoomNumber());
		}
		if (name == MeetingModel.LOCATION_PROPERTY){
			locationLabel.setText(model.getLocation());
		}
		if (name == MeetingModel.TIME_FROM_PROPERTY){
			timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
			setAppointmentTime(model.getTimeFrom(), model.getTimeTo());
		}
		if (name == MeetingModel.TIME_TO_PROPERTY){
			timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
			setAppointmentTime(model.getTimeFrom(), model.getTimeTo());
		}
		//Må ha en for status til møte
		
		
	}
	
	
	//En ikke fungerende funksjon for å sette ikonet i avtalen
	private ImageIcon getIcon(ArrayList<InvitationModel> invitations){
		ImageIcon typeIcon;
		
		if(invitations.isEmpty()){
			typeIcon = new ImageIcon("src/resources/avtaleMini.png");
		}
		else if(invitations.contains("Venter")){
			typeIcon = new ImageIcon("src/resources/venterMini.png");
		}
		else if(invitations.contains("Avslått")){
			typeIcon = new ImageIcon("src/resources/avslattMini.png");
		}
		else{
			typeIcon = new ImageIcon("src/resources/godkjentMini.png");
		}
		
		return typeIcon;
	}

	
	//lager og returnerer et stringformat av start og slutt av avtalen
	private String timeToString(Calendar S, Calendar E){
		String tempString= "";
		
		SimpleDateFormat  sdf = new SimpleDateFormat ("HH:mm");
		if (S != null && E != null) {
			tempString = sdf.format(S.getTime()) + " - " + sdf.format(E.getTime());
		}

		return tempString;
	}
	
	
	
	//Setter størrelse på panel + hva som skal vises
	private void setAppointmentTime(Calendar S, Calendar E){
		
		long SMilli = S.getTimeInMillis();
		long EMilli = E.getTimeInMillis();
		
		int minutes = (int)((EMilli-SMilli)/60000);
		
		int AppointmentLength = (minutes*WeekView.HOURHEIGHT)/60;

		this.setPreferredSize(new Dimension(WeekView.HOURWIDTH - 5, AppointmentLength));
		this.setMinimumSize(new Dimension(WeekView.HOURWIDTH - 5, AppointmentLength));
		this.setMaximumSize(new Dimension(WeekView.HOURWIDTH - 5, AppointmentLength));

		
		//Kjapp test av hvordan justering av avtale kan gjøres
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
	
	//bestemmer hva som skal vises i panelet + størrelse på skriften
	private void setView(boolean name, boolean location, boolean time, boolean icon ,boolean owner, int fontSize){
		nameLabel.setVisible(name);
		locationLabel.setVisible(location);
		timeLabel.setVisible(time);
		iconLabel.setVisible(icon);
		ownerLabel.setVisible(owner);
		setFont("Times New Roman",fontSize);
	}
	
	//Setter font og størrelse på alle labelene i panelet
	private void setFont(String font, int size){
		//NameLabel vill alltid være 3 hakk større enn de andre labelene (Overskrift)
		nameLabel.setFont(new Font(font, 1, size + 3));
		ownerLabel.setFont(new Font("Dialog", 1, size));
		timeLabel.setFont(new Font("Dialog", 1, size));
		iconLabel.setFont(new Font("Dialog", 1, size));
		locationLabel.setFont(new Font("Dialog", 1, size));
	}
	
//	public static void main (String args[]) { 
//        JFrame frame = new JFrame(""); 
//        AppointmentPanel ap = new AppointmentPanel();
//        frame.getContentPane().add(ap); 
//        frame.pack();  
//        frame.setVisible(true);   
//    }
}

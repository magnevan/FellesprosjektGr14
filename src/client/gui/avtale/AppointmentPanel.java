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
import client.model.InvitationStatus;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;
import client.model.UserModel;

public class AppointmentPanel extends JPanel {


	private GridBagConstraints c;
	private JLabel nameLabel, ownerLabel, timeLabel, iconLabel, locationLabel;
	private Color color;
	private MeetingModel model;
	int AppointmentLength;
	
	
	public AppointmentPanel(MeetingModel MM){
		
		model = MM;
		
		
		color=new Color(0,100,255);
		setBackground(color);
		
		setLayout(new GridBagLayout());
    	c = new GridBagConstraints();
    	
    	c.gridx = 0;
    	c.gridy = 0;
    	c.fill = c.HORIZONTAL;
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
		
		setPanel();

	}
	
	private void setPanel(){
		nameLabel.setText(model.getName());

		//Dette må nok endres på når jeg vet hvordan logikken bak location og møterom
		if(model.getLocation() != null)locationLabel.setText(model.getLocation());
		else locationLabel.setText(model.getRoom().getName());
			
		timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
		iconLabel.setIcon(getIcon(model.getInvitations()));
		ownerLabel.setText(model.getOwner().getFullName());
		
		setView(model.getTimeFrom(), model.getTimeTo());
	}
	

	//Denne er bare et foreløpig test får logikken bak dette er helt klar
	private ImageIcon getIcon(ArrayList<InvitationModel> invitations){
		ImageIcon typeIcon;
		
		if(invitations.isEmpty()){
			typeIcon = new ImageIcon("src/resources/avtaleMini.png");
		}
		else if(invitations.contains(InvitationStatus.INVITED)){
			typeIcon = new ImageIcon("src/resources/venterMini.png");
		}
		else if(invitations.contains(InvitationStatus.DECLINED)){
			typeIcon = new ImageIcon("src/resources/avslattMini.png");
		}
		else{
			typeIcon = new ImageIcon("src/resources/godkjentMini.png");
		}
		
		return typeIcon;
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
	
	
	

	
	private void setView(Calendar S, Calendar E){
		
		long SMilli = S.getTimeInMillis();
		long EMilli = E.getTimeInMillis();
		
		int minutes = (int)((EMilli-SMilli)/60000);
		
		AppointmentLength = (minutes*WeekView.HOURHEIGHT)/60;
		//Kjapp test av hvordan justering av avtale kan gjøres
		if(minutes == 15){
			showComponents(true, false, false,false, false, 10);
		}
		else if(minutes ==30){
			showComponents(true, true, false,false, false, 10);
		}
		else if(minutes == 45){
			showComponents(true, true, false,true, false, 10);
		}
		else if(minutes == 60 || minutes == 75){
			showComponents(true, true, false,true, false, 10);
		}
		else if(minutes == 90){
			showComponents(true, true, true,true, true, 10);
		}
		else{
			showComponents(true, true, true,true, true, 13);
		}
	}
	
	//bestemmer hva som skal vises i panelet + størrelse på skriften
	private void showComponents(boolean name, boolean location, boolean time, boolean icon ,boolean owner, int fontSize){
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
		ownerLabel.setFont(new Font(font, 1, size));
		timeLabel.setFont(new Font(font, 1, size));
		iconLabel.setFont(new Font(font, 1, size));
		locationLabel.setFont(new Font(font, 1, size));
	}
	
	public int getWidth(){
//		if(overlapp) return (WeekView.HOURWIDTH - 1)/2;		
		return (WeekView.HOURWIDTH - 1);
	}
	
	public int getLength(){
		return AppointmentLength -1;
	}
	
	public int getX(){
		//Calender.DAY_OF_WEEK hadde torsdag som startdag, lagde derfor dette arrayet
		int[] dayOfWeek = new int[] {3,4,5,6,0,1,2};
		int padding = 32;
		int day = dayOfWeek[(model.getTimeFrom().get(Calendar.DAY_OF_WEEK)-1)];
		return padding + ((day)*(WeekView.HOURWIDTH - 1)) + day;
	}
	
	public int getY(){
		int padding = 1;

		//Skaffer tiden fra midnatt
		Calendar c = (Calendar)model.getTimeFrom().clone();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long passed = now - c.getTimeInMillis();
		
		//Gjør om tiden til riktig pixel avstand
		final int minutes = (int)(((passed)/60000));
		int time = ((minutes*WeekView.HOURHEIGHT)/60);

		//if(overlapp) something something
		return padding + time;
	}
	
//	
//	public static void main (String args[]) { 
//        JFrame frame = new JFrame("");
//        Calendar from =  Calendar.getInstance();
//        from.set(2012, 10, 3, 12, 0);
//        Calendar to = Calendar.getInstance();
//        to.set(2012, 10, 3, 15, 0);
//        UserModel testPerson = new UserModel("testbruker", "test@hotmail.com", "Test Etternavn");
//        MeetingModel  MM = new MeetingModel(from, to, testPerson);
//        MM.setName("Viktig avtale");
//        MM.setLocation("spisesalen");
//        MM.setActive(true);
//        AppointmentPanel ap = new AppointmentPanel(MM);
//        
//        frame.getContentPane().add(ap); 
//        frame.pack();  
//        frame.setVisible(true);   
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//	

//	public void setModel(MeetingModel model){
//		this.model = model;
//		
//		//Sletting av avtale
//		if(!model.isActive()){
//			//trenger en metode som fjerner AppointmentPanel
//			
//			return;
//		}
//		
//		nameLabel.setText(model.getName());
//
//		if(!model.getLocation().equals("Annet")){locationLabel.setText(model.getLocation());}
//		else{locationLabel.setText(model.getRoom().getRoomNumber());}
//			
//		timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
//		iconLabel.setIcon(getIcon(model.getInvitations()));
//		ownerLabel.setText(model.getOwner().getFullName());
//		
//		setAppointmentTime(model.getTimeFrom(), model.getTimeTo());
//		
//	}
//		
//	public MeetingModel getModel(){
//		return model;
//	}
//	
//	@Override
//	public void propertyChange(PropertyChangeEvent event) {
//		final String name = event.getPropertyName();
//		
//		if (name == MeetingModel.NAME_PROPERTY){
//			nameLabel.setText(model.getName());
//		}
//		if (name == MeetingModel.ROOM_PROPERTY){
//			locationLabel.setText(model.getRoom().getRoomNumber());
//		}
//		if (name == MeetingModel.LOCATION_PROPERTY){
//			locationLabel.setText(model.getLocation());
//		}
//		if (name == MeetingModel.TIME_FROM_PROPERTY){
//			timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
//			setAppointmentTime(model.getTimeFrom(), model.getTimeTo());
//		}
//		if (name == MeetingModel.TIME_TO_PROPERTY){
//			timeLabel.setText((timeToString(model.getTimeFrom(), model.getTimeTo())));
//			setAppointmentTime(model.getTimeFrom(), model.getTimeTo());
//		}
//		//Må ha en for status til møte
//		
//		
//	}	
	
	
	
//	//Setter størrelse på panel + hva som skal vises
//	private void setAppointmentTime(Calendar S, Calendar E){
//		
//		long SMilli = S.getTimeInMillis();
//		long EMilli = E.getTimeInMillis();
//		
//		int minutes = (int)((EMilli-SMilli)/60000);
//		
//		AppointmentLength = (minutes*WeekView.HOURHEIGHT)/60;
//		
//		//Kjapp test av hvordan justering av avtale kan gjøres
//		if(minutes == 15){
//			showComponents(true, false, false,false, false, 10);
//		}
//		else if(minutes ==30){
//			showComponents(true, true, false,false, false, 10);
//		}
//		else if(minutes == 45){
//			showComponents(true, true, false,true, false, 10);
//
//		}
//		else if(minutes == 60 && minutes == 75){
//			showComponents(true, true, true,true, false, 10);
//		}
//		else if(minutes == 90){
//			showComponents(true, true, true,true, true, 10);
//		}
//		else{
//			showComponents(true, true, true,true, true, 13);
//		}
//		
//
//	}
}

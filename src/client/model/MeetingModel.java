package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
/**
 * A model for the meetings in the calendar
 * 
 * @author peterringset
 *
 */
public class MeetingModel extends Model {
	
	private Date date;
	private Time timeFrom, timeTo;
	private String name, description;
	private MeetingRoomModel room;
	private boolean active;
	private UserModel owner;
	private String ownerId;
	private ArrayList<UserModel> antendees;
	
	/**
	 * Construct a new meeting model
	 * Note that timeTo should be after timeFrom
	 * 
	 * @param date
	 * @param timeFrom
	 * @param timeTo
	 * @param owner
	 * @throws IllegalArgumentException if timeFrom is after timeTo
	 */
	public MeetingModel(Date date, Time timeFrom, Time timeTo, UserModel owner) {
		this.date = date;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.owner = owner;
		if(!timeFrom.before(timeTo)) {
			throw new IllegalArgumentException("MeetingModel: From-time is after to-time");
		}
	}
	
	public MeetingModel() {}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Time timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Time getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Time timeTo) {
		this.timeTo = timeTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MeetingRoomModel getRoom() {
		return room;
	}

	public void setRoom(MeetingRoomModel room) {
		this.room = room;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UserModel getOwner() {
		return owner;
	}
	
	public String toString() {
		return getName() + "(" + date + " " + timeFrom + " - " + timeTo + ")";
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public void fromStream(BufferedReader reader) throws IOException {
		setName(reader.readLine());
		StringBuilder desc = new StringBuilder();
		String line;
		while(!(line = reader.readLine()).equals("\0"))
			desc.append(line+"\r\n");
		setDescription(desc.toString());
		
		Date d;	
		DateFormat df = DateFormat.getDateTimeInstance();		
		try {
			line = reader.readLine();
			d = df.parse(line);
			setTimeFrom(new Time(d.getHours(), d.getMinutes(), 0));
			setDate(new Date(d.getYear(), d.getMonth(), d.getDay()));
	
			line = reader.readLine();
			d = df.parse(line);
			setTimeTo(new Time(d.getHours(), d.getMinutes(), 0));
		} catch(ParseException e) {
			e.printStackTrace();
		}		
		ownerId = reader.readLine();
	}

	/**
	 * Dump the model to stream
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void toStream(BufferedWriter writer) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("MeetingModel\r\n");
		sb.append(getName() + "\r\n");
		sb.append(getDescription().trim() + "\r\n\0\r\n");
		
		Date from = new Date(date.getYear(), date.getMonth(), date.getDay(), 
				timeFrom.getHours(), timeFrom.getMinutes(), 0);		
		sb.append(DateFormat.getDateTimeInstance().format(from) + "\r\n");
		
		Date to = new Date(date.getYear(), date.getMonth(), date.getDay(), 
				timeTo.getHours(), timeTo.getMinutes(), 0);	
		sb.append(DateFormat.getDateTimeInstance().format(to) + "\r\n");
		
		sb.append(owner.getUsername()+"\r\n");
		
		writer.write(sb.toString());
	}
	
}

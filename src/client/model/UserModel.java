package client.model;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import server.ModelEnvelope;

/**
 * A model for the users in the calendar system
 * 
 * @author Peter Ringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 * @author Magne vikjord
 */
public class UserModel implements TransferableModel {
	
	protected String	username,
						email,
						fullName;
	
	private Color color;
	
	private CalendarModel calendar;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String NAME_CHANGE = "name change";
	
	//These variables control which colors are given to new user calendars.
	private static final Color[] availiableColors = new Color[]{
		new Color(0xE78649),
		new Color(0x5E72AC),
		new Color(0x646BD0),
		new Color(0x223AF8),
		new Color(0x3C57FA),
		new Color(0x3775FF),
		new Color(0x46ADFF),
		new Color(0x92D642),
		new Color(0x65A716),
		new Color(0x48D17B),
		new Color(0x6CDCB3),
		new Color(0x83E9FB),
		new Color(0x65D1FA),
		new Color(0xC0E192),
		new Color(0xE7E19F),
		new Color(0xE7C69F),
		new Color(0xFF9C9A),
		new Color(0xFF9AB9)
	};
	private static int nextColor = 0;
	
	/**
	 * Create a user object
	 * 
	 * @param username
	 * @param email
	 * @param fullName
	 */
	public UserModel(String username, String email, String fullName) {
		this.username = username;
		this.email = email;
		this.fullName = fullName;
		this.color = UserModel.getNextColor();
		this.calendar = new CalendarModel(this);
	}
	
	/**
	 * Create a user model from stream
	 * 
	 * @param reader
	 * @param modelBuff
	 */
	public UserModel(BufferedReader reader, 
			HashMap<String, TransferableModel> modelBuff) throws IOException {
		this(reader.readLine(), reader.readLine(), reader.readLine());
	}
	
	
	/**
	 * Get username
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	

	/**
	 * Get unique ID
	 */
	@Override
	public String getUMID() {
		if(username == null)
			return null;
		return "user_"+username;
	}

	/**
	 * Get full name
	 * 
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}
	
	/**
	 * Get email
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	public CalendarModel getCalendarModel() {
		return this.calendar;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.username + " | " + this.email;
	}

	/**
	 * Unused, UserModel contains no other models
	 * @param envelope
	 */
	@Override
	public void addSubModels(ModelEnvelope envelope) {}

	/**
	 * Write the model to a StringBuffer
	 */
	@Override
	public void toStringBuilder(StringBuilder sb) {
		sb.append(getUsername() + "\r\n");
		sb.append(getEmail() + "\r\n");
		sb.append(getFullName() + "\r\n");
		
	}
	
	public Color getColor() {
		return this.color;
	}
	
	private static Color getNextColor() {
		Color c = availiableColors[nextColor];
		nextColor = (nextColor + 1) % availiableColors.length;
		return c;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	public void clearListeners() {
		for (PropertyChangeListener listener : pcs.getPropertyChangeListeners())
			pcs.removePropertyChangeListener(listener);
	}
}

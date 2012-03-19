package client.model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * A model for the users in the calendar system
 * 
 * @author Peter Ringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 * @author Magne vikjord
 */
public class UserModel extends TransferableModel {
	
	protected String	username,
						//password,
						email,
						fullName;
	
	private Color color;
	
	//These variables control which colors are given to new user calendars.
	private static final Color[] availiableColors = new Color[]{
		new Color(0x00E78649),
		new Color(0x005E72AC),
		new Color(0x00646BD0),
		new Color(0x00223AF8),
		new Color(0x003C57FA),
		new Color(0x003775FF),
		new Color(0x0046ADFF),
		new Color(0x0092D642),
		new Color(0x0065A716),
		new Color(0x0048D17B),
		new Color(0x006CDCB3),
		new Color(0x0083E9FB),
		new Color(0x0065D1FA),
		new Color(0x00C0E192),
		new Color(0x00E7E19F),
		new Color(0x00E7C69F),
		new Color(0x00FF9C9A),
		new Color(0x00FF9AB9)
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
	}
	
	/**
	 * Empty constructor 
	 * 
	 * Should only be used in combination with fromStream() otherwise the object
	 * will be unusable
	 */
	public UserModel() {}
	
	
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
	protected Object getMID() {
		return username;
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

	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.username + " | " + this.email;
	}

	/**
	 * Dump the fields of the user model to a stream
	 */
	@Override
	public void toStream(BufferedWriter os) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("UserModel\r\n");
		sb.append(getUsername() + "\r\n");
		//sb.append(getPassword() + "\r\n");
		sb.append(getEmail() + "\r\n");
		sb.append(getFullName() + "\r\n");
		
		os.write(sb.toString());
	}
	
	/**
	 * Read the fields of the user model from a stream
	 */
	@Override
	public void fromStream(BufferedReader in) throws IOException {
		username = in.readLine();
		email = in.readLine();
		fullName = in.readLine();
	}
	
	public Color getColor() {
		return this.color;
	}
	
	private static Color getNextColor() {
		Color c = availiableColors[nextColor];
		nextColor = (nextColor + 1) % availiableColors.length;
		return c;
	}
}

package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * A model for the users in the calendar system
 * 
 * @author Peter Ringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class UserModel extends TransferableModel {
	
	protected String	username,
						//password,
						email,
						fullName;
	
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
}

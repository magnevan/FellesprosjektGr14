package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import server.ModelEnvelope;

/**
 * A model for the users in the calendar system
 * 
 * @author Peter Ringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class UserModel implements TransferableModel {
	
	protected String	username,
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
}

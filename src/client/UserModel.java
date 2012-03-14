package client;

import java.io.OutputStream;

/**
 * A model for the users in the calendar system
 * 
 * @author Peter Ringset
 * 
 */
public class UserModel implements Model{
	
	protected String	username,
						password,
						email,
						fullName;
	
	/**
	 * Create a user object
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @param fullName
	 */
	public UserModel(String username, String password, String email, String fullName) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
	}

	@Override
	public void toStream(OutputStream os) {
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}
	
	@Override
	public String toString() {
		return this.fullName + " | " + this.email;
	}
}

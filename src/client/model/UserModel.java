package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * A model for the users in the calendar system
 * 
 * @author Peter Ringset
 * 
 */
public class UserModel extends Model {
	
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
	
	public UserModel() {}
	
	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}
	
	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return this.fullName + " | " + this.email;
	}

	/**
	 * Dump the fields of the user model to a stream
	 */
	@Override
	public void toStream(BufferedWriter os) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("client.UserModel\r\n");
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

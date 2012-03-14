package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Static methods for searching database with different search criteria
 * 
 * @author Peter Ringset
 *
 */
public class ServerUserModel extends client.UserModel {
	
	public String getPassword() {
		return this.password;
	}

	public ServerUserModel(String username, String password, String email, String fullName) {
		super(username, password, email, fullName);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Look-up in database for a specific user name
	 * 
	 * @param usr
	 * @param db
	 * @return the user
	 * @throws IllegalArgumentException if user name is an empty string
	 */
	public static ServerUserModel findByUsername(String usr, DBConnection db) {
		if (usr.length() == 0) {
			throw new IllegalArgumentException("ServerUserModel: user name can not be an empty string");
		}
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE username = '" + usr + "';");
			String 	dbusername = rs.getString("username"),
					dbemail = rs.getString("email"),
					dbfull_name = rs.getString("full_name"),
					dbpassword = rs.getString("password");
			return new ServerUserModel(dbusername, dbemail, dbfull_name, dbpassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Search in database for user by user name
	 * 
	 * @param usr
	 * @param db
	 * @return a list of matching user names
	 */
	public static ArrayList<ServerUserModel> searchByUsername(String usr, DBConnection db) {
		ArrayList<ServerUserModel> ret = new ArrayList<ServerUserModel>();
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE username LIKE '%" + usr + "%';");
			while (!rs.isAfterLast()) {
				String 	dbusername = rs.getString("username"),
						dbemail = rs.getString("email"),
						dbfull_name = rs.getString("full_name"),
						dbpassword = rs.getString("password");
				ret.add(new ServerUserModel(dbusername, dbemail, dbfull_name, dbpassword));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Search in database for user by email
	 * 
	 * @param em
	 * @param db
	 * @return a list of matching user names
	 */
	public static ArrayList<ServerUserModel> searchByEmail(String em, DBConnection db) {
		ArrayList<ServerUserModel> ret = new ArrayList<ServerUserModel>();
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE email LIKE '%" + em + "%';");
			while (!rs.isAfterLast()) {
				String 	dbusername = rs.getString("username"),
						dbemail = rs.getString("email"),
						dbfull_name = rs.getString("full_name"),
						dbpassword = rs.getString("password");
				ret.add(new ServerUserModel(dbusername, dbemail, dbfull_name, dbpassword));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Search in database for user by email and user name
	 * 
	 * @param usr
	 * @param em
	 * @param db
	 * @return a list of matching user names
	 */
	public static ArrayList<ServerUserModel> searchByUsernameAndEmail(String usr, String em, DBConnection db) {
		ArrayList<ServerUserModel> ret = new ArrayList<ServerUserModel>();
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE username LIKE '%" + usr + "%' AND email LIKE '%" + em + "%';");
			while (!rs.isAfterLast()) {
				String 	dbusername = rs.getString("username"),
						dbemail = rs.getString("email"),
						dbfull_name = rs.getString("full_name"),
						dbpassword = rs.getString("password");
				ret.add(new ServerUserModel(dbusername, dbemail, dbfull_name, dbpassword));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}

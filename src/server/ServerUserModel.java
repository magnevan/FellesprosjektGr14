package server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerUserModel extends client.UserModel{

	public ServerUserModel(String username, String password, String email, String fullName) {
		super(username, password, email, fullName);
		// TODO Auto-generated constructor stub
	}
	
	public static ServerUserModel findByUsername(String usr, DBConnection db) {
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
	
	public static ArrayList<ServerUserModel> searchByUsername(String usr, DBConnection db) {
		ArrayList<ServerUserModel> ret = new ArrayList<ServerUserModel>();
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE username LIKE '" + usr + "';");
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
	
	public static ArrayList<ServerUserModel> searchByEmail(String em, DBConnection db) {
		ArrayList<ServerUserModel> ret = new ArrayList<ServerUserModel>();
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE email LIKE '" + em + "';");
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
	
	public static ArrayList<ServerUserModel> searchByUsernameAndEmail(String usr, String em, DBConnection db) {
		ArrayList<ServerUserModel> ret = new ArrayList<ServerUserModel>();
		try {
			ResultSet rs = db.preformQuery("SELECT * FROM user WHERE username LIKE '" + usr + "' AND email LIKE '" + em + "';");
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

package server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import server.DBConnection;
import server.ServerMain;
import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.UserModel;

/**
 * Server Side MeetingModel with methods for reading and writing to server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerMeetingModel extends MeetingModel implements IServerModel {

	public ServerMeetingModel() {
		super();
	}
	
	/**
	 * Construct Meeting model from ResultSet
	 * 
	 * @param rs
	 */
	public ServerMeetingModel(ResultSet rs, DBConnection db) throws SQLException {		
		id = rs.getInt("id");
		setName(rs.getString("title"));
		setDescription(rs.getString("description"));
		ownerId = rs.getString("owner");
		setActive(rs.getBoolean("active"));
		
		Calendar fromTime = Calendar.getInstance();
		fromTime.setTime(rs.getDate("start_date"));
		setTimeFrom(fromTime);
		Calendar toTime = Calendar.getInstance();
		toTime.setTime(rs.getDate("start_date"));
		setTimeTo(toTime);
	}

	/**
	 * Get user model
	 * 
	 * Server side this may not yet be loaded, do JIT loading
	 */
	@Override
	public UserModel getOwner() {
		if(owner == null) {
			owner = ServerUserModel.findByUsername(ownerId, ServerMain.dbConnection);
		}
		return owner;
	}
	
	/**
	 * Get meeting attenddes
	 * 
	 * Server side this may not yet be loaded, do JIT loading
	 */
	@Override
	public ArrayList<InvitationModel> getInvitations() {
		if(invitations == null) {
			invitations = InvitationModel.findByMeeting(this, ServerMain.dbConnection);
		}
		return invitations;
	}
	
	/**
	 * Store model to database
	 */
	@Override
	public void store() {
		try {
			if(id == -1) {
				// Insert
				Statement st = ServerMain.dbConnection.createStatement();

				st.executeUpdate(String.format(
						"INSERT INTO appointment(title, start_date, end_date, description, owner)"
						+" VALUES('%s', '%s', '%s', '%s', '%s')",						
						getName(), getFormattedDate(getTimeFrom()), getFormattedDate(getTimeTo()),
						getDescription(), getOwner().getUsername()), Statement.RETURN_GENERATED_KEYS);
				
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				id = rs.getInt(1);
				st.close();
			} else {
				// TODO Update
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Format a Calendar for MySQL's DATETIME field
	 * 
	 * @param c
	 * @return
	 */
	private static String getFormattedDate(Calendar c) {
		return String.format(
				"%d-%d-%d %d:%d:%d", 
				c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)				
		);
	}

	/**
	 * Search database for meetings concerning all the given users, within the
	 * given time period
	 * 
	 * @param users
	 * @param startDate
	 * @param endDate
	 * @param db
	 * @return
	 */
	public static ArrayList<ServerMeetingModel> searchByUsernamesAndPeriod(
			String[] users, Calendar startDate, Calendar endDate, DBConnection db) {
		
		if(users.length == 0) {
			throw new IllegalArgumentException("Need atleast one user");
		}		
		ArrayList<ServerMeetingModel> ret = new ArrayList<ServerMeetingModel>();
		try {
			StringBuilder userIn = new StringBuilder();			
			for(String u : users) {
				userIn.append(",'"+u+"'");
			}
			String userList = userIn.toString().substring(1);
			
			ResultSet rs = db.preformQuery(
					"SELECT DISTINCT a.* FROM appointment as a " +
					"LEFT JOIN user_appointment as ap ON a.id = ap.appointment_id " +
					"WHERE start_date >= '"+getFormattedDate(startDate)+"' " +
					"AND start_date < '"+getFormattedDate(endDate)+"'" +
					"AND (a.owner IN (" + userList + ") OR ap.username IN ("+userList+"))");
			while (rs.next()) {
				ret.add(new ServerMeetingModel(rs, db));
			}
			rs.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Find a single model based on a id number
	 * 
	 * @param id
	 * @param db
	 * @return
	 */
	public static ServerMeetingModel findById(int id, DBConnection db) {
		try {
			ResultSet rs = db.preformQuery(
					"SELECT * FROM appointment AS a " +
					"LEFT JOIN user_appointment AS ap ON a.id = ap.appointment_id " +
					"WHERE a.id = " + id + ";");
			
			if(rs.next()) {
				return new ServerMeetingModel(rs, db);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}

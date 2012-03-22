package client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import client.IServerResponseListener;
import client.ServerConnection;
import client.gui.usersearch.AbstractFilteredUserListModel;

/**
 * A implementation of IFilteredUserList that searches the user database using
 * the currently active ServerConnection instance.
 *  
 * @author Peter Ringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class FilteredUserListModel 
	extends AbstractFilteredUserListModel 
	implements IServerResponseListener {

	// Internal variables
	private int lastRequestId;
	private UserModel[] filtered;
	private String lastFilter = null;	
	private HashSet<UserModel> userBlacklist;
	private List<UserModel> serverList;
	
	/**
	 * Setup a new FilteredUserListModel
	 * 
	 */
	public FilteredUserListModel() {
		filtered = new UserModel[0];
		userBlacklist = new HashSet<UserModel>();
	}

	/**
	 * Set the filter for the list
	 * This method propagates the filter to the server, response is
	 * asynchronously given in {@code onServerResponse()}
	 * 
	 * @TODO limit the number of requests/s ? Could be done with a Timer
	 * @param filter
	 */
	@Override
	public void setFilter(String filter) {
		// Only send a request if we've actually updated the filter
		if(lastFilter == null || !filter.equals(lastFilter)) {
			lastFilter = filter;
			lastRequestId = ServerConnection.instance().requestFilteredUserList(this, filter);
		}
	}

	/**
	 * Set a blank filter. Calls {@code setFilter("")}
	 */
	@Override
	public void setFilter() {
		setFilter("");
	}

	/**
	 * Get an array of users matching the filter
	 */
	@Override
	public UserModel[] getUserList() {
		return filtered;
	}
	
	/**
	 * Add a array of users to the blacklist
	 * 
	 * @param users
	 */
	public void addUsersToBlacklist(UserModel[] users) {
		userBlacklist.addAll(Arrays.asList(users));
		rebuildFilteredList();
	}
	
	/**
	 * Remove a array of users from the blacklist
	 * 
	 * @param users
	 */
	public void removeUsersFromBlacklist(UserModel[] users) {
		userBlacklist.removeAll(Arrays.asList(users));
		rebuildFilteredList();
	}
	
	/**
	 * Rebuild the filtered list
	 * 
	 */
	private void rebuildFilteredList() {

		UserModel[] old = filtered;
		
		HashSet<UserModel> tmp = new HashSet<UserModel>();
		tmp.addAll(serverList);
		tmp.removeAll(userBlacklist);
		filtered = tmp.toArray(new UserModel[tmp.size()]);		
		
		fireUserListChangeEvent(old, filtered);
	}

	/**
	 * Server response comes here
	 * 
	 * @param requestId id of the request that caused this response
	 * @param data actual data returned
	 */
	@Override
	public void onServerResponse(int requestId, Object data) {
		// We're only interested in the newest responses, throw out old ones
		if (lastRequestId == requestId) {			
			serverList = (List<UserModel>)data;
			
			rebuildFilteredList();
		}
	}

}

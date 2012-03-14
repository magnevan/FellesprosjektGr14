package client.gui;

import java.util.ArrayList;

import client.IServerResponseListener;
import client.ServerConnection;
import client.model.UserModel;

/**
 * A model for the filtered user list.
 * 
 * @author Peter Ringset
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 *
 */
public class FilteredUserListModel 
	extends AbstractFilteredUserListModel 
	implements IServerResponseListener {

	private ServerConnection sc;
	private int lastRequestId;
	private UserModel[] filtered;	
	
	private String lastFilter = null;
	
	public FilteredUserListModel() {
		sc = ServerConnection.instance();
		filtered = new UserModel[0];
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
			lastRequestId = sc.requestFilteredUserList(this, filter);
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
	 * Get an array of all filtered users.
	 */
	@Override
	public UserModel[] getUserList() {
		return filtered;
	}

	/**
	 * Server response comes here.
	 * 
	 * @param requestId
	 * @param data
	 */
	@Override
	public void onServerResponse(int requestId, Object data) {
		// We're only interested in the newest responses, throw out old ones
		if (lastRequestId == requestId) {
			ArrayList<UserModel> response = (ArrayList<UserModel>)data;
			UserModel[] old = filtered;
			filtered = response.toArray(new UserModel[response.size()]);
			
			fireUserListChangeEvent(old, filtered);
		}
	}

}

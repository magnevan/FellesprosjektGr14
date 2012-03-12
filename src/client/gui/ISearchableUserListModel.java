package client.gui;

import java.beans.PropertyChangeListener;

public interface ISearchableUserListModel {

	public final static String PROPERTY_USER_LIST = "userlist";
	
	/**
	 * Set a string that is used to filter out matching users 
	 * 
	 * @param filter
	 */
	public void setFilter(String filter);
	
	/**
	 * Get the filtered user list
	 * 
	 * After setting a filter using setFilter this method will provide filtered
	 * data, but only after it has fired a property change event
	 */
	public String[][] getUserList();
	
	/**
	 * Add a PropertyChangeListener object
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * Remove a PropertyChangeListener object
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
}

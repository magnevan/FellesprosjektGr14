package client.gui;

import java.beans.PropertyChangeListener;

import client.model.UserModel;

/**
 * Data model that provides a way to search in a list of users using a filter
 * string.
 * 
 * The flow of this model is that a external object will call setFilter() and
 * set the filter string to be used. Once the filtered list of users is ready
 * a property change event will be triggered in all listeners of this object
 * and they may pull the user list using getUserList()
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public interface IFilteredUserListModel {

	public final static String PROPERTY_USER_LIST = "userlist";
	
	/**
	 * Set a string that is used to filter out matching users 
	 * 
	 * @param filter
	 */
	public void setFilter(String filter);
	
	/**
	 * Set a empty filter, in effect clearing the filter
	 */
	public void setFilter();
	
	/**
	 * Get the filtered user list
	 * 
	 * After setting a filter using setFilter this method will provide filtered
	 * data, but only after it has fired a property change event
	 */
	public UserModel[] getUserList();
	
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

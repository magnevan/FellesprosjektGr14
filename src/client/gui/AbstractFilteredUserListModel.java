package client.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract implementation of the IFilteredUserListModel that provides some
 * basic listener support
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public abstract class AbstractFilteredUserListModel 
	implements IFilteredUserListModel {
	
	private PropertyChangeSupport pcs;
		
	public AbstractFilteredUserListModel() {
		pcs = new PropertyChangeSupport(this);
	}
	
	/**
	 * Register a listener with the model
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	/**
	 * Deregister a registered listener
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	/**
	 * Helper method for sending of a property change event for the filtered
	 * user list
	 * 
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireUserListChangeEvent(String[][] oldValue, String[][] newValue) {
		pcs.firePropertyChange(PROPERTY_USER_LIST, oldValue, newValue);
	}
	
}

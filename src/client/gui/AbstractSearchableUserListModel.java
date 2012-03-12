package client.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract implementation of the ISearchableUserListModel that provides some
 * basic listener support
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public abstract class AbstractSearchableUserListModel 
	implements ISearchableUserListModel {
	
	private PropertyChangeSupport pcs;
		
	public AbstractSearchableUserListModel() {
		pcs = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
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

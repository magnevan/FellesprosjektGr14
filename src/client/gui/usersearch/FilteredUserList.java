package client.gui.usersearch;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import client.model.UserModel;

/**
 * Filtered user list widget
 * 
 * This widget provides a search field and a list of users. It takes a
 * implementation of ISearchableUserListModel as its data source.
 * 
 * Example usage:
 * 
 * <code>
 * // A implementation of ISearchableUserListModel that communicates with
 * // The server has to be implemented
 * ISearchableUserListModel model = new ...
 * SearchableUserList list = new SearchableUserList(model);
 * 
 * // Get a array of the currently selected users
 * String[][] = list.getSelectedUsers();
 * </code>
 * 
 * @TODO The model should deal in User object and not String[]
 * @TODO General code cleanup
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
@SuppressWarnings("serial")
public class FilteredUserList extends JPanel 
	implements PropertyChangeListener, KeyListener {
	
	// Internal component
	private JTextField searchField;
	private JTable userTable;
	
	// Table headers
	private static final String[] HEADERS = new String[]{"Navn", "Epost"};
	
	// Internal and external model
	private UserListModel userListModel;
	private IFilteredUserListModel model;
	
	/**
	 * Create a new list of user object with a search box for live search
	 *  
	 * @param model The filtered user list model
	 */
	public FilteredUserList(IFilteredUserListModel model) {
		
		this.model = model;
		model.addPropertyChangeListener(this);
		
		setLayout(new BorderLayout());		
		searchField = new JTextField();		
		searchField.addKeyListener(this);
		add(searchField, BorderLayout.NORTH);
		
		userListModel = new UserListModel();
		userTable = new JTable(userListModel);
		userTable.setFillsViewportHeight(true);
		add(new JScrollPane(userTable), BorderLayout.CENTER);
		
		// Clear filter
		model.setFilter();		
	}
	
	/**
	 * Return the currently selected user objects within the filtered user list
	 * 
	 * @return 
	 */
	public UserModel[] getSelectedUsers() {
		int[] rows = userTable.getSelectedRows();
		UserModel[] selection = new UserModel[rows.length];
		
		for(int i = 0; i < rows.length; i++) {
			selection[i] = model.getUserList()[rows[i]];
		}
		
		return selection;
	}
	
	/**
	 * Handle events from the IFilteredUserList, tell the table
	 * that it's model has changed
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(IFilteredUserListModel.PROPERTY_USER_LIST)
				&& event.getOldValue() != event.getNewValue()) {
			userListModel.fireTableDataChanged();
		}
		
	}

	/**
	 * Capture keyPressed events in the searchField text field and update the 
	 * filter of the filtered user list model. This is expected to cause a 
	 * propertyChange event soon after.
	 * 
	 * @param event
	 */
	@Override
	public void keyPressed(KeyEvent event) {}

	@Override
	public void keyReleased(KeyEvent event) {
		
		model.setFilter(searchField.getText());
	}
	@Override
	public void keyTyped(KeyEvent event) {}
	
	/**
	 * Table model providing data for the internal JTable 
	 */
	@SuppressWarnings("serial")
	class UserListModel extends AbstractTableModel {

		/**
		 * Get header name for column i
		 * 
		 * @param i zero indexed column number
		 */
		@Override
		public String getColumnName(int i) {
			return HEADERS[i];
		}
		
		/**
		 * Get number of columns
		 */
		@Override
		public int getColumnCount() {
			return 2;
		}

		/**
		 * Get number of rows
		 */
		@Override
		public int getRowCount() {
			return model.getUserList().length;
		}

		/**
		 * Get value at rowIndex
		 * 
		 * @param rowIndex
		 * @param columnIndex
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) return model.getUserList()[rowIndex].getFullName();
			else return model.getUserList()[rowIndex].getEmail();
		}
		
		/**
		 * Boolean indicating if the table cell is editable
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
	}

}

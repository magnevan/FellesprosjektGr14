package client.gui;

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
	public String[][] getSelectedUsers() {
		int[] rows = userTable.getSelectedRows();
		String[][] selection = new String[rows.length][];
		
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
	public void keyPressed(KeyEvent event) {
		if(Character.isLetterOrDigit(event.getKeyChar())) {
			model.setFilter(searchField.getText()+event.getKeyChar());
		} else if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE 
				&& searchField.getText().length() > 0) {
			model.setFilter(searchField.getText().substring(0, searchField.getText().length()-1));
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {}
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
		 * Get value at (rowIndex,columnIndex)
		 * 
		 * @param rowIndex
		 * @param columnIndex
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return model.getUserList()[rowIndex][columnIndex];
		}
		
		/**
		 * Boolean indicating if the table cell is editable
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
	}
	
	// DEBUG 
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setLayout(new GridLayout(2,1, 5, 5));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final FilteredUserList list = new FilteredUserList(new TestModel());
		list.setSize(300, 150);
		f.add(list);
		
		JButton b = new JButton("Print selection");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("\nCurrent selection:");
				for(String[] s : list.getSelectedUsers()) {
					System.out.println(s[0]);
				}
			}
		});
		f.add(b);
		
		f.setSize(300, 170);
		f.setVisible(true);
	}

}

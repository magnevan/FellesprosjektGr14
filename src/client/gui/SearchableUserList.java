package client.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

/**
 * Searchable user list widget
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
 * @TODO This can be combined with two buttons and an other table in a JPanel to created the select atendees widged
 * 
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
@SuppressWarnings("serial")
public class SearchableUserList extends JPanel 
	implements PropertyChangeListener, KeyListener {
	
	// Internal component
	private JTextField searchField;
	private JTable userTable;
	
	private static final String[] HEADERS = new String[]{"Navn", "Epost"};
	
	private UserListModel userListModel;
	private ISearchableUserListModel model;
	
	/**
	 * Create a new list of user object with a search box for live search
	 *  
	 * @param model The searchable user list model
	 */
	public SearchableUserList(ISearchableUserListModel model) {
		
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
		
		model.setFilter("");
		
	}
	
	/**
	 * Handle events from the searchable user list model, tell the table
	 * that it's model has changed
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(ISearchableUserListModel.PROPERTY_USER_LIST)
				&& event.getOldValue() != event.getNewValue()) {
			userListModel.fireTableDataChanged();
		}
		
	}

	/**
	 * Capture keyPressed events in the searchField text field and update the 
	 * filter of the searchable user list model. This will in turn cause a 
	 * propertyChange event
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
	public void keyTyped(KeyEvent event) {
	}
	
	/**
	 * Table model providing data for the filtered user table
	 * 
	 */
	@SuppressWarnings("serial")
	class UserListModel extends AbstractTableModel {

		@Override
		public String getColumnName(int i) {
			return HEADERS[i];
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return model.getUserList().length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return model.getUserList()[rowIndex][columnIndex];
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new SearchableUserList(new TestModel()));
		f.setSize(300, 150);
		f.setVisible(true);
	}

}

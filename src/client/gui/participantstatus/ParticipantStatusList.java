package client.gui.participantstatus;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.UserModel;

/**
 * ParticipantStatusList widget
 * 
 * A read-only table that maintains information about users invited to meetings
 * 
 * @author Magne
 *
 */
public class ParticipantStatusList extends JPanel implements PropertyChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4473084764548566328L;

	private static final String[] HEADERS = new String[]{"Navn", "Status"};
	
	private final MeetingModel mmodel;
	private final ParticipantModel pmodel;
	
	private final JTable table;
	
	public ParticipantStatusList(MeetingModel mmodel) {
		this.mmodel = mmodel;
		this.pmodel = new ParticipantModel();
		this.table = new JTable(pmodel);
		
		mmodel.addPropertyChangeListener(this);
		
		for (InvitationModel inv : mmodel.getInvitations())
			inv.addPropertyChangeListener(this);
		
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		pmodel.fireTableDataChanged();
		
		if        (evt.getPropertyName() == MeetingModel.INVITATION_CREATED) {
			((InvitationModel)evt.getNewValue()).addPropertyChangeListener(this);
		} else if (evt.getPropertyName() == MeetingModel.INVITATION_REMOVED) {
			((InvitationModel)evt.getNewValue()).removePropertyChangeListener(this);
		}
	}
	
	/**
	 * Return the currently selected user objects within the filtered user list
	 * 
	 * @return 
	 */
	public UserModel[] getSelectedUsers() {
		int[] rows = table.getSelectedRows();
		UserModel[] selection = new UserModel[rows.length];
		
		for(int i = 0; i < rows.length; i++) {
			selection[i] = mmodel.getInvitations().get(rows[i]).getUser();
		}
		
		return selection;
	}
	
	/**
	 * Prepare for garbage collection
	 */
	public void close() {
		for (InvitationModel inv : mmodel.getInvitations())
			inv.removePropertyChangeListener(this);
	}
	
	class ParticipantModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4164972929676997268L;

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
			return mmodel.getInvitations().size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			InvitationModel inv = mmodel.getInvitations().get(rowIndex);
			
			switch (columnIndex) {
			case 0: return inv.getUser().getFullName();
			case 1: return inv.getStatus();
			default: return null;
			}
			
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
	}
	
}

package client.gui.participantstatus;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import client.model.InvitationModel;
import client.model.MeetingModel;

/**
 * ParticipantStatusList widget
 * 
 * A read-only table that maintains information about users invited to meetings
 * 
 * @author Magne
 *
 */
public class ParticipantStatusList extends JTable implements PropertyChangeListener {
	
	private static final String[] HEADERS = new String[]{"Navn", "Status"};
	
	private final MeetingModel mmodel;
	private final ParticipantModel pmodel;
	
	public ParticipantStatusList(MeetingModel mmodel) {
		this.mmodel = mmodel;
		this.pmodel = new ParticipantModel();
		
		mmodel.addPropertyChangeListener(this);
		
		this.setModel(pmodel);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == MeetingModel.INVITATION_CREATED ||
			evt.getPropertyName() == MeetingModel.INVITATION_REMOVED) {
			
			pmodel.fireTableDataChanged();
		}
	}
	
	class ParticipantModel extends AbstractTableModel {

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

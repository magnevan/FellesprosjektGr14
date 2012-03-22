package client.model;

/**
 * Valid invitation statuses
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public enum InvitationStatus {
	INVITED,
	DECLINED,
	ACCEPTED,
	NOT_YET_SAVED("Not sendt"); //Used only while a meeting is being edited
	
	private final String tostring;
	private InvitationStatus() {
		this.tostring = super.toString();
	}
	private InvitationStatus(String tostring) {
		this.tostring = tostring;
	}
	
	@Override
	public String toString() {
		return tostring;
	}
}

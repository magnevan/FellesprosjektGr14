package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CalendarModel extends AbstractModel {

	protected String	ownerUsername,
						ownerEmail,
						ownerFullname;
	
	
	protected Set<MeetingModel> meetings;
	protected TreeSet<MeetingModel> meetingsFrom;
	
	public CalendarModel() {
		
		meetings = new HashSet<MeetingModel>();
//		meetings = new TreeSet<MeetingModel>();
	}
	
	@Override
	public void fromStream(BufferedReader stream) throws IOException {
	}

	@Override
	public void toStream(BufferedWriter stream) throws IOException {
	}

}

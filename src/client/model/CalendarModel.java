package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CalendarModel extends Model {

	protected String	ownerUsername,
						ownerEmail,
						ownerFullname;
	
	
	protected final Set<MeetingModel> meetings;
	protected final TreeMap<Calendar, MeetingModel> 	meetingsFrom,
														meetingsTo;
	
	public CalendarModel() {
		
		meetings = new HashSet<MeetingModel>();
		meetingsFrom = new TreeMap<Calendar, MeetingModel>();
		meetingsTo = new TreeMap<Calendar, MeetingModel>();
	}
	
	public CalendarModel addMeeting(MeetingModel meeting) {
		
		meetings.add(meeting);
		meetingsFrom.put(meeting.getTimeFrom(), meeting);
		meetingsTo.put(meeting.getTimeTo(), meeting);
		
		return this;
	}
	
	public boolean contains(MeetingModel meeting) {
		return meetings.contains(meeting);
	}
	
	
	public Set<MeetingModel> getMeetingInterval(Calendar fromTime, Calendar toTime) {
		return getMeetingInterval(fromTime, toTime, false);
	}
	
	public Set<MeetingModel> getMeetingInterval(Calendar fromTime, Calendar toTime, boolean tight) {
		Set<MeetingModel> returnSet;
		
		Map<Calendar, MeetingModel> fromMap = meetingsFrom.subMap(fromTime, true, toTime, true);
		Map<Calendar, MeetingModel> toMap = meetingsTo.subMap(fromTime, true, toTime, true);
		
		//Convert to set for cut operation.
		Set<MeetingModel> fromSet = new HashSet<MeetingModel>(fromMap.values());
		Set<MeetingModel> toSet = new HashSet<MeetingModel>(toMap.values());
		
		if (tight) {
			//This should represent the set operation returnSet = fromSet (CUT) toSet
			//i.e Meetings that start after the given fromTime, and end before the given toTime
			returnSet = fromSet;
			returnSet.retainAll(toSet);
		} else {
			//This should represent the set operation returnSet = fromSet (UNION) toSet
			//i.e Meetings where some of the meeting time is happening within the fromTime and toTime
			returnSet = fromSet;
			returnSet.addAll(toSet);
		}
		
		
		return returnSet;
	}
	
	@Override
	public void fromStream(BufferedReader stream) throws IOException {
	}

	@Override
	public void toStream(BufferedWriter stream) throws IOException {
	}

}

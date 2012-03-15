package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CalendarModel extends Model {

	protected final Set<MeetingModel> 		meetings;
	protected final TreeSet<MeetingModel> 	meetingsFrom,
											meetingsTo;
	
	public CalendarModel() {
		
		meetings = new HashSet<MeetingModel>();
		meetingsFrom = new TreeSet<MeetingModel>();
		meetingsTo = new TreeSet<MeetingModel>();
		
	}
	
	public CalendarModel add(MeetingModel meeting) {
		
		meetings.add(meeting);
		meetingsFrom.add(meeting);
		meetingsTo.add(meeting);
		
		return this;
	}
	
	public CalendarModel addAll(Collection<MeetingModel> c) {
		meetings.addAll(c);
		meetingsFrom.addAll(c);
		meetingsTo.addAll(c);
		
		return this;
	}
	
	public CalendarModel remove(MeetingModel meeting) {
		
		meetings.remove(meeting);
		meetingsFrom.remove(meeting);
		meetingsTo.remove(meeting);
		
		return this;
	}
	
	public CalendarModel removeAll(Collection<MeetingModel> c) {
		meetings.removeAll(c);
		meetingsFrom.removeAll(c);
		meetingsTo.removeAll(c);
		
		return this;
	}
	
	public void clear() {
		meetings.clear();
		meetingsFrom.clear();
		meetingsTo.clear();
	}
	
	public boolean contains(MeetingModel meeting) {
		return meetings.contains(meeting);
	}
	
	/**
	 * @param fromTime Calendar object representing the start of the interval
	 * @param toTime Calendar object representing the end of the interval
	 * @return
	 */
	public Set<MeetingModel> getMeetingInterval(Calendar fromTime, Calendar toTime) {
		return getMeetingInterval(fromTime, toTime, false);
	}
	
	/**
	 * @param fromTime Calendar object representing the start of the interval
	 * @param toTime Calendar object representing the end of the interval
	 * @param tight boolean value. true returns only meetings that overlap fully with the timeinterval, false returns all meetings that overlaps to some degree
	 * @return Set with all the meetings within the given interval
	 */
	public Set<MeetingModel> getMeetingInterval(Calendar fromTime, Calendar toTime, boolean tight) {
		Set<MeetingModel> returnSet;
		
		//TODO Uses a sort blank MeetingModel for the comparison. Non-elegant solution but couldn't find anything better
		Set<MeetingModel> fromSet = meetingsFrom.subSet(new MeetingModel(fromTime, toTime, null), true, new MeetingModel(fromTime, toTime, null), true);
		Set<MeetingModel> toSet = meetingsTo.subSet(new MeetingModel(fromTime, toTime, null), true, new MeetingModel(fromTime, toTime, null), true);
		
		
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
	
	public int size() {
		return meetings.size();
	}
	
	@Override
	public void fromStream(BufferedReader stream) throws IOException {
		//TODO
	}

	@Override
	public void toStream(BufferedWriter stream) throws IOException {
		//TODO
	}

}

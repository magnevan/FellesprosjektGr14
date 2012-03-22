package client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CalendarModel {
	
	private PropertyChangeSupport pcs;

	//These essentially hold a buffer of meetings
	protected final Set<MeetingModel> 		meetings;
	protected final TreeSet<MeetingModel> 	meetingsFrom,
											meetingsTo;
	
	public final static String	MEETING_ADDED = "MEETING_ADDED";
	public final static String  MEETING_REMOVED = "MEETING REMOVE";
	
	private final UserModel owner;
	
	public CalendarModel(UserModel owner) {
		
		this.owner = owner;
		
		meetings = new HashSet<MeetingModel>();
		meetingsFrom = new TreeSet<MeetingModel>();
		meetingsTo = new TreeSet<MeetingModel>();
		
		pcs = new PropertyChangeSupport(this);
		
		//Requests a chuck of meetings from the server
		Calendar 	from = Calendar.getInstance(),
					to   = Calendar.getInstance();
		
		from.roll(Calendar.MONTH, -1);
		to  .roll(Calendar.MONTH,  1);
		from.set(Calendar.DAY_OF_MONTH, 1);
	}
	
	private CalendarModel add(MeetingModel meeting) {
		
		meetings.add(meeting);
		meetingsFrom.add(meeting);
		meetingsTo.add(meeting);

		pcs.firePropertyChange(MEETING_ADDED, null, meeting);
		
		return this;
	}
	
	private CalendarModel addAll(Collection<MeetingModel> c) {
		meetings.addAll(c);
		meetingsFrom.addAll(c);
		meetingsTo.addAll(c);
		
		for (MeetingModel mm : c)
			pcs.firePropertyChange(MEETING_ADDED, null, mm);
		
		return this;
	}
	
	private CalendarModel remove(MeetingModel meeting) {
		
		meetings.remove(meeting);
		meetingsFrom.remove(meeting);
		meetingsTo.remove(meeting);
		
		pcs.firePropertyChange(MEETING_REMOVED, null, meeting);
		
		return this;
	}
	
	private CalendarModel removeAll(Collection<MeetingModel> c) {
		meetings.removeAll(c);
		meetingsFrom.removeAll(c);
		meetingsTo.removeAll(c);
		
		for (MeetingModel mm : c)
			pcs.firePropertyChange(MEETING_REMOVED, null, mm);
		
		return this;
	}
	
	public boolean contains(MeetingModel meeting) {
		return meetings.contains(meeting);
	}
	
	public Set<MeetingModel> getMeetingsInWeek(Calendar date) {
		Calendar fromTime = (Calendar)date.clone(),
				   toTime = (Calendar)date.clone();
		
		fromTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		toTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		
		fromTime.set(Calendar.HOUR_OF_DAY, 0);
		fromTime.set(Calendar.MINUTE, 0);
		fromTime.set(Calendar.SECOND, 0);
		
		fromTime.set(Calendar.HOUR_OF_DAY, 23);
		fromTime.set(Calendar.MINUTE, 59);
		fromTime.set(Calendar.SECOND, 59);
		
		
		return getMeetingInterval(fromTime, toTime,true);
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
	
	public void addPropertyChangeListner(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
}

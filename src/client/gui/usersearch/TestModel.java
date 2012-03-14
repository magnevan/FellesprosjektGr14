package client.gui.usersearch;

import java.util.ArrayList;

public class TestModel extends AbstractFilteredUserListModel {

	String[][] data = new String[][] {
			{"Runar Olsen", "Runar@example.com"},
			{"Roger Hansen", "Roger@example.com"},
			{"Zumer Zumers", "Zumer@example.com"},
			{"Ørjan Ørevis", "Ørjan@example.com"}
	};
	
	ArrayList<String[]> filtered = new ArrayList<String[]>();
	
	/**
	 * Set empty filter
	 */
	public void setFilter() {
		setFilter("");
	}
	
	@Override
	public void setFilter(String filter) {
		filtered.clear();
		for(String[] s : data) {
			if(s[0].toLowerCase().startsWith(filter.toLowerCase())) {
				filtered.add(s);
			}
		}
		fireUserListChangeEvent(null, getUserList());
	}

	@Override
	public String[][] getUserList() {
		return filtered.toArray(new String[filtered.size()][]);
	}
	
	
	
}
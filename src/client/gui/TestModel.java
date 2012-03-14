package client.gui;

import java.util.ArrayList;

class TestModel extends AbstractFilteredUserListModel {

	String[][] data = new String[][] {
			{"Runar Olsen", "test@example.com"},
			{"Råger Hansen", "test@example.com"},
			{"Zumer Zumers", "test@example.com"},
			{"Ørjan Ørevis", "test@example.com"}
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
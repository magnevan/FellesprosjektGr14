package client.gui;

import java.util.ArrayList;

import client.model.UserModel;

class TestModel extends AbstractFilteredUserListModel {

	UserModel[] data = new UserModel[] {
			new UserModel("runarolsen", "", "runar@example.com", "Runar Olsen"),
			new UserModel("roger", "", "roger@example.com", "Roger Hansen"),
			new UserModel("zumer", "", "zumer@example.com", "Zumer Zumers"),
			new UserModel("orjan", "", "orjan@example.com", "Orjan Orje")
	};
	
	ArrayList<UserModel> filtered = new ArrayList<UserModel>();
	
	/**
	 * Set empty filter
	 */
	public void setFilter() {
		setFilter("");
	}
	
	@Override
	public void setFilter(String filter) {
		filtered.clear();
//		for (UserModel m : filtered) {
//			if (	m.getUsername().toLowerCase().startsWith(filter) ||
//					m.getFullName().toLowerCase().startsWith(filter) ||
//					m.getEmail().toLowerCase().startsWith(filter)) {
//				filtered.add(m);
//			}
//		}
		for (UserModel m : data) {
			filtered.add(m);
		}
		fireUserListChangeEvent(null, getUserList());
	}

	@Override
	public UserModel[] getUserList() {
		return filtered.toArray(new UserModel[filtered.size()]);
	}
	
	
	
}
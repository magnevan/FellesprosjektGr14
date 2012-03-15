package server.model;

import client.model.MeetingModel;

public class ServerMeetingModel extends MeetingModel implements IServerModel {

	public ServerMeetingModel() {
		super();
	}
	
	@Override
	public IServerModel store() {
		
		return null;
	}

}

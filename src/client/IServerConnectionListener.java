package client;

public interface IServerConnectionListener {
	
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	
	public void serverConnectionChange(String change);
}

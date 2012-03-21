package client;

/**
 * Interface for objects that will request data from the server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public interface IServerResponseListener {

	/**
	 * Receive requested 
	 * 
	 * @param requestId the same id number as the request* method that caused
	 * 	this request returned
	 * @param data raw data, allways a List<RequestedModelType>
	 */
	public void onServerResponse(int requestId, Object data);
	
}

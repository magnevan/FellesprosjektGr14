package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import client.model.MeetingModel;
import client.model.AbstractModel;
import client.model.UserModel;

/**
 * The clients interface to the remote calendar server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerConnection extends AbstractConnection {

	private static Logger LOGGER = Logger.getLogger("ServerConnection");
	private static ServerConnection instance;	
	
	private ReaderThread readerThread;	
	private int nextRequestId = 1;	
	private UserModel user;
	
	// Stores listeners while we wait for the server to respond
	private Map<Integer, IServerResponseListener> listeners;
	
	// Stores models that come back from the server after beeing stored
	private Map<Integer, AbstractModel> storedModels;
		
	/**
	 * Create a new ServerConnection and attempt to preform a login
	 * 
	 * @param address
	 * @param port
	 * @param username
	 * @param password
	 * @throws IOException On reading errors
	 * @throws InvalidArgumentException or username/password errors
	 */
	private ServerConnection(InetAddress address, int port, 
			String username, String password) throws IOException {
		
		super();		
		listeners = Collections.synchronizedMap(
				new HashMap<Integer, IServerResponseListener>()
			);
		storedModels = Collections.synchronizedMap(
				new HashMap<Integer, AbstractModel>()
			);
			
		
		try {
			socket = new Socket(address, port);			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			LOGGER.info(reader.readLine()); // Read welcome message
			
			writeLine(String.format("LOGIN %s %s", username, password));			
			String line = reader.readLine();
			if(!line.startsWith("OK")) {
				throw new IllegalArgumentException("Bad login");
			}
			
			line = reader.readLine();// User header
			// Read user model off stream
			user = (UserModel) (readModels()).get(0);
			
			// Start a reader thread and return
			readerThread = new ReaderThread();
			readerThread.start();
		} catch(IOException e) {
			throw e;
		}
		
	}
	
	/**
	 * Attempt to login
	 * 
	 * If successfull a ServerConnection instance will be accessable from
	 * instance();
	 * 
	 * @param address
	 * @param port
	 * @param username
	 * @param password
	 * @throws IOException
	 */
	public static boolean login(InetAddress address, int port, 
			String username, String password) throws IOException {
		instance = new ServerConnection(address, port, username, password);
		return true;
	}
	
	/**
	 * Logout the currently logged in user
	 * 
	 * @return
	 */
	public static boolean logout() {
		if(instance != null) {
			try {
				instance.writeLine(instance.formatCommand(0, "LOGOUT"));
			} catch(IOException e) {
				// Ignore
			} finally {
				instance = null;
			}			
			return true;
		}
		return false;
	}
	
	public static boolean isOnline() {
		return instance != null;
	}
	
	/**
	 * Get singleton instance
	 * 
	 * @return
	 */
	public static ServerConnection instance() {
		return instance;
	}
	
	/**
	 * Construct client side models for readModels()
	 */
	protected AbstractModel createModel(String name) {
		if(name.equals("UserModel")) {
			return new UserModel();
		} else if(name.equals("MeetingModel")) {
			return new MeetingModel();
		}
		return null;
	}
	
	/**
	 * Private reader thread
	 *
	 */
	class ReaderThread extends Thread {		
		@Override
		public void run() {
			try {
				String line;
				while((line = reader.readLine()) != null) {
					LOGGER.info(line);
					
					String[] parts = line.split("\\s+");
					
					// All server responses should contain a id and a method name
					if(parts.length < 2) {
						LOGGER.severe("Maformed server response: "+line);
						continue;
					}
					
					int id = Integer.parseInt(parts[0]);
					String method = parts[1];
					
					// Notifications come with a zero id
					if(id == 0) {
						LOGGER.info("Unhandled notice: "+line);
						continue;
					}
					
					ArrayList<AbstractModel> models = readModels();
					
					// Stored models are saved
					if(method.equals("STORE")) {
						storedModels.put(id, models.get(0));
						return;
					} 
					
					// All other models are passed to their listeners
					IServerResponseListener listener = listeners.get(id);
					if(listener == null) {
						LOGGER.severe("No listener registered for response "+line);						
					} else {
						listener.onServerResponse(id, models);
					}	
				}
			} catch(IOException e) {
				
			} finally {
				try {
					socket.close();
				} catch(IOException e) {}
				instance = null;
			}
		}
		
	}
	
	/**
	 * Return the currently logged in user object
	 * 
	 * @return
	 */
	public UserModel getUser() {
		return user;
	}
	
	/**
	 * Request all meetings within a given time period from this users calendar
	 * 
	 * @return request id
	 */
	/*public int requestMeetings(
			IServerResponseListener listener, Date startDate, Date endDate) {
		return requestMeetings(listener, new UserModel[]{getUser()}, startDate, endDate);
	}*/
	
	/**
	 * Request all meetings within a given time periode from the given users calendars
	 * 
	 * @param listener
	 * @return request id
	 */
	/*public int requestMeetings(
			IServerResponseListener listener, UserModel[] users, Date startDate, 
			Date endDate) {
		
	}*/
	
	/**
	 * Creates a new meeting model with only the title set
	 * 
	 * @param title
	 * @return
	 */
	/*public MeetingModel createMeeting(String title) {
		
	}*/
	
	/**
	 * Stores the given model on the remote server
	 * 
	 * The returned model will be equal to the given with any changes that was
	 * caused on the server as a result to the store call.
	 * 
	 * <code>
	 * ServerConnection server = ...;
	 * Model model = ...;
	 * 
	 * model = server.storeModel(model);
	 * 
	 * // Returned model should be used here after 
	 * </code>
	 * 
	 * @param model
	 * @return
	 */
	public AbstractModel storeModel(AbstractModel model) {
		int id = ++nextRequestId;
		try {
			writeModels(new AbstractModel[]{model}, id, "STORE");
			
			// Updated model will come in reader thread, halt till its there
			while(!storedModels.containsKey(id)) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {}
			}
			model = storedModels.get(id);
			storedModels.remove(id);
			return model;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Request a list of all users filtered on the given filter
	 * 
	 * @param listener
	 * @param filter
	 * @return
	 */
	public int requestFilteredUserList(IServerResponseListener listener, String filter) {
		int id = ++nextRequestId;
				
		try {
			listeners.put(id, listener);
			writeLine(formatCommand(id, "REQUEST",  "FILTERED_USERLIST "+filter));
		} catch(IOException e) {
			listeners.remove(id);
			LOGGER.severe("IOException requestFilteredUserList");
			LOGGER.severe(e.toString());
			return -1;
		}
			
		return id;
	}
}

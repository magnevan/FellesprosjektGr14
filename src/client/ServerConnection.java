package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import server.ModelEnvelope;
import client.gui.exceptions.BadLoginException;
import client.model.ActiveUserModel;
import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;
import client.model.NotificationModel;
import client.model.TransferableModel;
import client.model.UserModel;

/**
 * The clients interface to the remote calendar server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerConnection extends AbstractConnection {
	
	// Stores listeners interested in server connection changes
	private static final Set<IServerConnectionListener> serverConnectionListeners = new HashSet<IServerConnectionListener>();
	
	private static Logger LOGGER = Logger.getLogger("ServerConnection");
	private static ServerConnection instance = null;	
	
	private ReaderThread readerThread;	
	private int nextRequestId = 1;
	
	// Stores listeners while we wait for the server to respond
	private Map<Integer, IServerResponseListener> listeners;
	
	// Stores models that come back from the server after being stored
	private Map<Integer, TransferableModel> storedModels;
		
	
	/**
	 * Attempt to login
	 * 
	 * If successfull a ServerConnection instance will be accessible from
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
		fireServerConnectionChange(IServerConnectionListener.LOGIN);
		return true;
	}
	
	/**
	 * Logout the currently logged in user
	 * 
	 * @return
	 */
	public static boolean logout() {
		fireServerConnectionChange(IServerConnectionListener.LOGOUT);
		ClientMain.setActiveUser(null);
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
	
	/**
	 * @return are we online?
	 */
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
				new HashMap<Integer, TransferableModel>()
			);
			
		
		try {
			socket = new Socket(address, port);			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			LOGGER.info(reader.readLine()); // Read welcome message
			
			writeLine(String.format("LOGIN %s %s", username, password));			
			String line = reader.readLine();
			if(!line.startsWith("OK")) {
				throw new BadLoginException();
			}
			
			reader.readLine();
			ClientMain.setActiveUser(new ActiveUserModel((UserModel) readModels().get(0)));
			
			// Start a reader thread and return
			readerThread = new ReaderThread();
			readerThread.start();
		} catch(IOException e) {
			throw e;
		}
		
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
					
					List<TransferableModel> models = readModels();
					
					// Broadcasts come with a zero id
					if(id == 0 && method.equals("BROADCAST")) {
						TransferableModel model = models.get(0);
						
						if(model instanceof NotificationModel) {
							System.out.println("Got notification, handle it ! TODO");
						}
						continue;
					}
					
					
					// Stored models are saved
					if(method.equals("STORE")) {
						storedModels.put(id, models.get(0));
						continue;
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
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch(IOException e) {}
				instance = null;
			}
		}
		
	}
	
	/**
	 * Request all meetings within a given time period from this users calendar
	 * 
	 * @return request id
	 */
	public int requestMeetings(
			IServerResponseListener listener, Calendar startDate, Calendar endDate) {
		return requestMeetings(listener, new UserModel[]{ClientMain.getActiveUser()}, startDate, endDate);
	}
	
	/**
	 * Request all meetings within a given time period from the given users calendars
	 * 
	 * @param listener
	 * @return request id
	 */
	public int requestMeetings(
			IServerResponseListener listener, UserModel[] users, Calendar startDate, 
			Calendar endDate) {
		
		int id = ++nextRequestId;
		
		StringBuilder ul = new StringBuilder();
		for(UserModel u : users) {
			ul.append(",");
			ul.append(u.getUsername());
		}
		
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			
			listeners.put(id, listener);			
			writeLine(formatCommand(id, "REQUEST",  "MEETING_LIST"));
			writeLine(df.format(startDate.getTime()));
			writeLine(df.format(endDate.getTime()));
			writeLine(ul.toString().substring(1));
			writeLine("");
		} catch(IOException e) {
			listeners.remove(id);
			LOGGER.severe("IOException requestMeetings");
			LOGGER.severe(e.toString());
			return -1;
		}
			
		return id;
		
	}
	
	/**
	 * Request a meeting based on id
	 * 
	 * @param listener
	 * @param id
	 * @return
	 */
	public int requestMeeting(IServerResponseListener listener, int mid) {
		int id = ++nextRequestId;
		
		try {
			listeners.put(id, listener);
			writeLine(formatCommand(id, "REQUEST",  "MEETING "+mid));
		} catch(IOException e) {
			listeners.remove(id);
			LOGGER.severe("IOException requestMeeting");
			LOGGER.severe(e.toString());
			return -1;
		}			
		return id;
	}
	
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
	public TransferableModel storeModel(TransferableModel model) {
		int id = ++nextRequestId;
		try {
			writeModels(Arrays.asList(model), id, "STORE");
			
			// Updated model will come in reader thread, halt untill it's there
			while(!storedModels.containsKey(id)) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {}
			}
			return storedModels.remove(id);
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
	
	
	/**
	 * Request a list of available meeting rooms within the given time period
	 * 
	 * @param listener
	 * @param from
	 * @param to
	 * @return
	 */
	public int requestAvailableRooms(IServerResponseListener listener, Calendar from, Calendar to) {
		int id = ++nextRequestId;
		
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			
			listeners.put(id, listener);
			writeLine(formatCommand(id, "REQUEST",  "AVAILABLE_ROOMS"));
			writeLine(df.format(from.getTime()));
			writeLine(df.format(to.getTime()));
			writeLine("");
			
		} catch(IOException e) {
			listeners.remove(id);
			LOGGER.severe("IOException requestFilteredUserList");
			LOGGER.severe(e.toString());
			return -1;
		}
		
		return id;
	}
	
	public static void addServerConnectionListener(IServerConnectionListener listener) {
		serverConnectionListeners.add(listener);
	}
	public static void removeServerConnectionListener(IServerConnectionListener listener) {
		serverConnectionListeners.remove(listener);
	}
	
	private static void fireServerConnectionChange(String change) {
		for (IServerConnectionListener listener : serverConnectionListeners)
			listener.serverConnectionChange(change);
	}	
	
	/**
	 * Read models off stream
	 * 
	 */
	@Override
	protected List<TransferableModel> readModels() throws IOException {
		ModelEnvelope envelope = new ModelEnvelope(reader, false);
		return envelope.getModels();
	}
	
}

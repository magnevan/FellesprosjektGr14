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

import client.model.Model;

/**
 * The clients interface to the remote calendar server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerConnection {

	private static ServerConnection instance;
	
	private static Logger LOGGER = Logger.getLogger("ServerConnection");
	
	private final Socket s;
	private BufferedReader reader;
	private BufferedWriter writer;
	private ReaderThread readerThread;
	
	private int nextRequestId = 1;
	
	// Stores listeners while we wait for the server to respond
	private Map<Integer, IServerResponseListener> listeners;
		
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
		
		listeners = Collections.synchronizedMap(
			new HashMap<Integer, IServerResponseListener>()
		);
		
		try {
			s = new Socket(address, port);			
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			reader.readLine(); // Read welcome message
			
			writeLine(String.format("LOGIN %s %s", username, password));			
			String line = reader.readLine();
			if(!line.startsWith("OK")) {
				throw new IllegalArgumentException("Bad login");
			}
			
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
	 * Get singleton instance
	 * 
	 * @return
	 */
	public static ServerConnection instance() {
		return instance;
	}
	
	/**
	 * Write a line to the server
	 * 
	 * @param line
	 */
	private synchronized void writeLine(String line) {
		try {
			LOGGER.info(line);
			writer.write(line + "\r\n");
			writer.flush();
		} catch(IOException e) {
			e.printStackTrace();// TODO handle exception
		}
	}

	/**
	 * Write a simple request
	 * 
	 * @param id
	 * @param request
	 */
	private void writeSimpleRequest(int id, String string) {
		writeLine(String.format("%d %s", id, string));		
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
					
					IServerResponseListener listener = listeners.get(id);
					if(listener == null) {
						LOGGER.severe("No listener registered for response "+line);						
					}
					
					// TODO Are there any other valid returns than models?
					ArrayList<Model> models = new ArrayList<Model>();
					while(!(line = reader.readLine()).equals("")) {
						try {
							Model model = (Model) Class.forName(line).newInstance();
							model.fromStream(reader);
							models.add(model);
							reader.readLine();
						} catch(Exception e) {
							LOGGER.severe("Unkown model class sent by server, "+line);
							LOGGER.severe(e.toString());
						}
					}
					listener.onServerResponse(id, models);
					
				}
			} catch(IOException e) {
				
			} finally {
				try {
					s.close();
				} catch(IOException e) {}
			}
		}
		
	}
	
	
	/**
	 * Return the currently logged in user object
	 * 
	 * @return
	 */
	/*public UserModel getUser() {
		
	}*/
	
	/**
	 * Request a list of users filtered by the given string filter
	 * 
	 * @param lisener lister object that will be notified once the response comes
	 * @param filter text filter
	 * @return request id
	 */
	/*public int requestFilteredUserlist(
			IServerResponseListener listener, String filter) {
		
	}*/
	
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
	/*public Model storeModel(Model model) {
		
	}*/
	
	
	public int requestFilteredUserList(IServerResponseListener listener, String filter) {
		int id = ++nextRequestId;
				
		listeners.put(id, listener);
		writeSimpleRequest(id, "REQUEST FILTERED_USERLIST "+filter);
		
		return id;
	}
}

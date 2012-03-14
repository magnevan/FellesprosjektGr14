package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Logger;

/**
 * The clients interface to the remote calendar server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ServerConnection {

	private static Logger LOGGER = Logger.getLogger("ServerConnection");
	
	private final Socket s;
	private BufferedReader reader;
	private BufferedWriter writer;
	private ReaderThread readerThread;
		
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
	public ServerConnection(InetAddress address, int port, 
			String username, String password) throws IOException { 
		
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
					
					// Notifications come what a zero id
					if(line.startsWith("0 NOTICE ")) {
						LOGGER.info("Got notice: "+line.substring(9));
					}
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
	public UserModel getUser() {
		
	}
	
	/**
	 * Request a list of users filtered by the given string filter
	 * 
	 * @param lisener lister object that will be notified once the response comes
	 * @param filter text filter
	 * @return
	 */
	public UserModel[] requestFilteredUserlist(
			ServerResponseListener listener, String filter) {
		
	}
	
	/**
	 * Request all meetings within a given time period from this users calendar
	 * 
	 * @return
	 */
	public MeetingModel[] requestMeetings(
			ServerResponseListener listener, Date startDate, Date endDate) {
		return requestMeetings(listener, new UserModel[]{getUser()}, startDate, endDate);
	}
	
	/**
	 * Request all meetings within a given time periode from the given users calendars
	 * 
	 * @param listener
	 * @return
	 */
	public MeetingModel[] requestMeetings(
			ServerResponseListener listener, UserModel[] users, Date startDate, 
			Date endDate) {
		
	}
	
	/**
	 * Creates a new meeting model with only the title set
	 * 
	 * @param title
	 * @return
	 */
	public MeetingModel createMeeting(String title) {
		
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
	public Model storeModel(Model model) {
		
	}
	
}

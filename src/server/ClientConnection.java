package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import server.model.IServerModel;
import server.model.ServerMeetingModel;
import server.model.ServerUserModel;
import client.AbstractConnection;
import client.model.AbstractModel;

/**
 * ClientConnection
 * 
 * ClientConnection is the server side counter part to the client side
 * ServerConnection. After a client has connected and has authenticated a
 * ClientConnection thread is created and spawned with the given clients
 * socket, streams and ServerUserModel.
 * 
 * ClientConnection will then enter a read loop reading commands from the
 * client, executing the request and then returning a response back
 * 
 * TODO writer access has to be synchronized, or else a notification from above
 * might collide with a normal response
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ClientConnection extends AbstractConnection implements Runnable {

	private static Logger LOGGER = Logger.getLogger("ClientConnection");	
	
	// Internal variables
	private final ServerUserModel user;	
	private final ClientConnectionListener handler;	
	private boolean running = true;
	
	/**
	 * Create a ClientConnection thread
	 * 
	 * @param s
	 * @param reader
	 * @param writer
	 */
	public ClientConnection(Socket s, BufferedReader reader, BufferedWriter writer, 
			ServerUserModel user, ClientConnectionListener handler) {
		super(s, writer, reader);
		this.user = user;
		this.handler = handler;
	}
	
	/**
	 * Start thread, listen for incoming request, handle and repeat
	 */
	@Override
	public void run() {
		LOGGER.info(String.format(
			"Client thread for %s (%s) started", 
			socket.getInetAddress().toString(), user
		));
		
		DBConnection db = ServerMain.dbConnection;
		try {
			
			writeModels(new AbstractModel[]{user}, 0, "USER");
			
			// Read loop, read untill we've shutting down or we reach EOF
			String line = null;
			while(running && (line = reader.readLine()) != null) {
				
				// All request starts with a ID followed by the method
				String[] parts = line.split("\\s+");				
				if(parts.length < 2) {
					LOGGER.severe("Malformed command recived from client: "+line);
					continue;
				}
				int id = Integer.parseInt(parts[0]);
				String method = parts[1];
				
				if(method.equals("REQUEST") && parts.length > 2) {
					String smethod = parts[2];
					
					// Request a filtered user list
					if(smethod.equals("FILTERED_USERLIST")) {
						// Allow empty filters
						String filter = "";
						if(parts.length == 4) {
							filter = parts[3];
						}
						
						ArrayList<ServerUserModel> matches = ServerUserModel.searchByUsernameAndEmail(
								filter, filter, ServerMain.dbConnection);
						
						// Pull models from database and write them back to client
						writeModels((AbstractModel[]) matches.toArray(new AbstractModel[matches.size()]), 
								id, method, smethod);
						
					}
				
				} else if(method.equals("STORE")) {
					// Store model
					
					AbstractModel model = readModels().get(0);		
					((IServerModel)model).store();					
					writeModels(new AbstractModel[]{model}, id, method);
					
				} else if(method.equals("LOGOUT")) {
					writeLine(formatCommand(id, "LOGOUT"));
					disconnect();
				}
				
				else {
					LOGGER.severe("Malformed command recived from client: "+line);
					continue;
				}
					
			}
			
		} catch(IOException e) {
			// Drop client if we cannot read/write socket
			LOGGER.info(String.format(
				"Client %s (%s) dropped due to IOException", 
				socket.getInetAddress().toString(), user
			));
			LOGGER.info(e.toString());
		} finally {
			disconnect();
		}
		
	}
	
	/**
	 * Disconnect the client
	 * 
	 */
	public void disconnect() {
		try {
			running = false;
			handler.removeClient(user);
			reader.close();
			writer.close();
			socket.close();
		} catch(IOException e) {
			// Ignore
		}
	}
	
	/**
	 * Construct client side models for readModels()
	 */
	protected AbstractModel createModel(String name) {
		if(name.equals("UserModel")) {
			return new ServerUserModel();
		} else if(name.equals("MeetingModel")) {
			return new ServerMeetingModel();
		}
		return null;
	}
	
}

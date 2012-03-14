package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ClientConnection extends Thread {

	private static Logger LOGGER = Logger.getLogger("ClientConnection");	
	
	private final Socket s;
	private final BufferedReader reader;
	private final BufferedWriter writer;
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
		this.s = s;
		this.user = user;
		this.reader = reader;
		this.writer = writer;
		this.handler = handler;
	}
	
	/**
	 * Send a line to the user
	 * 
	 * @param line
	 */
	public synchronized void writeLine(String line) {
		try {
			writer.write(line + "\r\n");
			writer.flush();			
		} catch(IOException e) {
			LOGGER.info(String.format(
				"Client %s (%s) dropped due to IOException", 
				s.getInetAddress().toString(), user
			));
			LOGGER.info(e.toString());
			disconnect();
		}
	}
	
	/**
	 * Start thread, listen for incoming request, handle and repeat
	 */
	@Override
	public void run() {
		LOGGER.info(String.format(
			"Client thread for %s (%s) started", 
			s.getInetAddress().toString(), user
		));
		DBConnection db = null;
		try {
			db = new DBConnection(Main.properties);
			String line = null;
			while(running && (line = reader.readLine()) != null) {
				// Read next command
				String[] parts = line.split("\\s+");
				
				if(parts.length < 2) {
					LOGGER.severe("Malformed command recived from client: "+line);
					continue;
				}
				
				int id = Integer.parseInt(parts[0]);
				String method = parts[1];
				
				if(method.equals("REQUEST")) {
					if(parts[2].equals("FILTERED_USERLIST")) {
						// Allow empty filters
						String filter = "";
						if(parts.length == 4) {
							filter = parts[3];
						}
						
						ArrayList<ServerUserModel> matches = ServerUserModel.searchByUsernameAndEmail(
								filter, filter, Main.dbConnection);
						
						writeLine(String.format("%d %s %s", id, method, parts[2]));
						for(ServerUserModel model : matches) {
							model.toStream(writer);
							writeLine("");
						}
						writeLine("");
						
					}
				}
			}
		} catch(SQLException e) {
			LOGGER.info(String.format(
					"Client %s (%s) dropped due to SQLException", 
					s.getInetAddress().toString(), user
				));
				LOGGER.info(e.toString());			
		} catch(IOException e) {
			LOGGER.info(String.format(
				"Client %s (%s) dropped due to IOException", 
				s.getInetAddress().toString(), user
			));
			LOGGER.info(e.toString());
		} finally {
			if(db != null)
				db.close();
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
			s.close();
		} catch(IOException e) {
			// Ignore
		}
	}
	
}

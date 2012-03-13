package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ClientConnectionListener {

	private static Logger LOGGER = Logger.getLogger("ClientConnectionListener");
	
	private Map<String, ClientConnection> clients;
	private final int port;
	
	/**
	 * Create a new ClientConnectionListener on the given port number
	 * 
	 * @param port
	 */
	public ClientConnectionListener(int port) {
		this.port = port;
		clients = Collections.synchronizedMap(new HashMap<String, ClientConnection>());
	}

	/**
	 * Boardcast a message to all connected clients
	 * 
	 * @param line
	 */
	public void broadcastLine(String line) {
		for(ClientConnection c : clients.values()) {
			c.sendLine(line);
		}
	}
	
	/**
	 * Removes a client, called by the client it self after a logout
	 * or a dropped connection
	 * 
	 * @param username
	 */
	public void removeClient(String username) {
		if(clients.containsKey(username)) {
			clients.remove(username);
			LOGGER.info(String.format("%s is gone", username));
			LOGGER.info(String.format("%d clients in total", clients.size()));
		}
	}
	
	/**
	 * Attempts to open up the port given in the constructor for listening and
	 * then enters a listen loop that will run forever
	 * 
	 * @throws IOException if port number is taken
	 */
	public void listen() throws IOException {
		ServerSocket ss = new ServerSocket(this.port);
		
		LOGGER.info("Server open for requests on "+this.port);
		
		while(true) {
			try {
				// Wait for new clients
				Socket s = ss.accept();
				
				LOGGER.info("Accepted client from "+s.getInetAddress().toString());
				
				// login sequence				
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				
				writer.write("Welcome to "+Main.VERSION+"\r\n");
				writer.flush();
				String login = reader.readLine();
				String[] parts = login.split("\\s+");
				
				if(!login.startsWith("LOGIN ") || parts.length != 3) {
					LOGGER.info("Invalid login packet, dropping client");
					writer.write("Invalid login packet, disconnection");
					writer.flush();
					s.close();
					continue;
				}
				
				String username = parts[1], password = parts[2];
				
				// Check database here
				
				writer.write("Welcome "+username +"\r\n");
				writer.flush();
				
				// Store and start separate handler thread
				ClientConnection cc = new ClientConnection(s, reader, writer, username, this);
				LOGGER.info(String.format("Client from %s authenticated as %s", s.getInetAddress().toString(), username));
				LOGGER.info(String.format("%d clients in total", clients.size()));
				clients.put(username, cc);
				cc.start();
				
				// Return to handle next client
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientConnection extends Thread {

	private static Logger LOGGER = Logger.getLogger("ClientConnection");	
	
	private final Socket s;
	private final BufferedReader reader;
	private final BufferedWriter writer;
	private final String username;
	
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
			String username, ClientConnectionListener handler) {
		this.s = s;
		this.username = username;
		this.reader = reader;
		this.writer = writer;
		this.handler = handler;
	}
	
	/**
	 * Send a line to the user
	 * 
	 * @param line
	 */
	public synchronized void sendLine(String line) {
		try {
			writer.write(line + "\r\n");
			writer.flush();			
		} catch(IOException e) {
			LOGGER.info(String.format(
				"Client %s (%s) dropped due to IOException", 
				s.getInetAddress().toString(), username
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
			s.getInetAddress().toString(), username
		));
		try {
			String line = null;
			while(running && (line = reader.readLine()) != null) {
				// Read next command
				String[] command = line.split("\\s+");
				
				// Debug commands 
				if(command[0].toUpperCase().equals("PING")) {
					sendLine("PONG!");
				} else if(command[0].toUpperCase().equals("EXIT")) {
					System.exit(1);
				} else if(command[0].toUpperCase().equals("BROADCAST")) {
					handler.broadcastLine(command[1]);
				}				
			}
		} catch(IOException e) {
			LOGGER.info(String.format(
				"Client %s (%s) dropped due to IOException", 
				s.getInetAddress().toString(), username
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
			handler.removeClient(username);
			reader.close();
			writer.close();
			s.close();
		} catch(IOException e) {
			// Ignore
		}
	}
	
}

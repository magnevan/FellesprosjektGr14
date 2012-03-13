package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
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
	
	
	// Public interface
	public void pong() {
		writeLine("PING");
	}

	public static void main(String[] args) throws IOException {
		ServerConnection sc = new ServerConnection(InetAddress.getLocalHost(), 9034, "runar", "runar");
		sc.pong();
	}
	
}

package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class ServerMain {

	private static Logger LOGGER = Logger.getLogger("Main");	
	public static String VERSION = "calendar-server-0.0.1";	
	public static Properties properties;	
	public static DBConnection dbConnection;
	
	/**
	 * 
	 * @param p server configuration
	 * @throws NumberFormatException on badly formatted number in properties
	 */
	public ServerMain(Properties p) throws NumberFormatException {		
		properties = p;
		
		try {
			dbConnection = new DBConnection(p);
		} catch(SQLException e) {
			LOGGER.severe("Unable to connect to database, server stopping");
			LOGGER.severe(e.toString());
			return;
		}
		
		ClientConnectionListener ccl = new ClientConnectionListener(
			Integer.parseInt(p.getProperty("fp.server.listen_port"))
		);
		
		try {
			// Enter listen loop
			ccl.listen();
		} catch(IOException e) {
			LOGGER.severe("Port number in use");
			LOGGER.severe(e.toString());
		}
		
	}
	
	/**
	 * Server entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Load configuration from server.properties
			Properties p = new Properties();
			File f = new File("src/server.properties.dist");
			p.load(new FileReader(f));
			
			new ServerMain(p);
		} catch (FileNotFoundException e) {
			LOGGER.severe("Missing server.properties");
			LOGGER.severe(e.toString());
			System.exit(1);
		} catch (IOException e) {
			LOGGER.severe("IOException while reading properties");
			LOGGER.severe(e.toString());
			System.exit(1);
		} catch(NumberFormatException e) {
			LOGGER.severe("Bad number format in properties");
			LOGGER.severe(e.toString());
		}
		
	}
	
}

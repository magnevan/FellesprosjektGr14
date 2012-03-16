package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import client.model.AbstractModel;

/**
 * Abstract connection implements helper methods for reading and 
 * writing generic structures to a stream. Both the ServerConnection
 * and ClientConnection inherits from this.
 * 
 * AbstractConnection is thread safe.
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public abstract class AbstractConnection {

	private static Logger LOGGER = Logger.getLogger("AbstractConnection");

	protected Socket socket;
	protected BufferedWriter writer;
	protected BufferedReader reader;
	
	/**
	 * Create a AbstractConnection object
	 */
	public AbstractConnection() {
		this(null, null, null);
	}
	
	/**
	 * Create a AbstractConnection object
	 * 
	 * @param socket
	 * @param writer
	 * @param reader
	 */
	public AbstractConnection(Socket socket, BufferedWriter writer, BufferedReader reader) {
		this.socket = socket;
		this.writer = writer;
		this.reader = reader;
	}
	  	
	/**
	 * Send a single line to the user
	 * 
	 * @param line
	 */
	public void writeLine(String line) throws IOException {
		synchronized(writer) {
			writer.write(line + "\r\n");
			writer.flush();
		}
	}
	
	/**
	 * Format and return a command with the given id number and method name
	 * 
	 * @param id
	 * @param method
	 */
	protected String formatCommand(int id, String method) {
		return formatCommand(id, method, "");
	}
	
	/**
	 * Format and return a command with the given id number, method name and 
	 * sub method
	 * 
	 * @param id
	 * @param method
	 * @param smethod
	 */
	protected String formatCommand(int id, String method, String smethod) {
		return String.format("%d %s %s", id, method, smethod).trim();
	}
	
	/**
	 * Dumps the given array of models to the stream, the id and method will
	 * be printed as a header 
	 * 
	 * @param models
	 */	
	protected void writeModels(AbstractModel[] models, int id, String method) throws IOException {
		writeModels(models, id, method, "");
	}
	
	/**
	 * Dumps the given array of models to the stream
	 * 
	 * @param models
	 * @param id
	 * @param method
	 * @param smethod
	 */
	protected void writeModels(AbstractModel[] models, int id, String method, String smethod) 
			throws IOException {

		synchronized(writer) {
			writer.write(formatCommand(id, method, smethod)+"\r\n");
			for(AbstractModel m : models) {
				if(m != null) {
					m.toStream(writer);
					writer.write("\r\n");
				}
			}
			writer.write("\r\n");
			writer.flush();
		}
	}
	
	/**
	 * Attempts to read a set of models from the input stream
	 */
	protected ArrayList<AbstractModel> readModels() throws IOException {
		ArrayList<AbstractModel> models = new ArrayList<AbstractModel>();
		String line;
		while(!(line = reader.readLine()).equals("")) {
			try {
				AbstractModel model = createModel(line);				
				model.fromStream(reader);
				models.add(model);
				reader.readLine(); // Read the empty separator line
			} catch(Exception e) {
				// TODO This is way to generic, makes debugging hard
				LOGGER.severe("Unkown model class sent by server, "+line);
				LOGGER.severe(e.toString());
			}
		}		
		return models;
	}
	
	/**
	 * Create a model object based on the given name
	 * @param name
	 */
	protected abstract AbstractModel createModel(String name);
	
}

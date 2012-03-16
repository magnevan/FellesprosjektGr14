package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Super class for all models that may be transfered across between client
 * and server. 
 * 
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public abstract class TransferableModel {
	
	/**
	 * Write object to a stream
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public abstract void fromStream(BufferedReader stream) throws IOException ;
	
	/**
	 * Read object from stream
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public abstract void toStream(BufferedWriter stream) throws IOException ;
	
	/**
	 * Get a unique model id, used by the cache manager
	 * 
	 * @return a string ID or null if the object is unidentifiable
	 */
	protected String getUMI() {
		if(getMID() != null)
			return getClass().getName()+"_"+getMID().toString();
		return null;
	}
	
	/**
	 * Return a id that will identify this exact instance of a TransferableModel
	 * subclass among all the other possible objects of the same subclass.
	 * 
	 * Ie. the ID number, username, etc
	 *
	 * @return ID or null if this cannot be identified (not yet stored)
	 */
	protected abstract Object getMID();
}

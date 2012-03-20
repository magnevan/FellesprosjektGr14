package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import server.ModelEnvelope;

/**
 * Super class for all models that may be packed in a ModelEnvelope for
 * transfering between client and server.
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
	//public abstract void fromStream(BufferedReader stream) throws IOException ;
	
	/**
	 * Read object from stream
	 * 
	 * @param stream
	 * @throws IOException
	 */
	//public abstract void toStream(BufferedWriter stream) throws IOException ;
	
	/**
	 * Create a model object based on data read from the given BufferedReader
	 * and buffered models from the model buffer HashMap
	 * 
	 * @param reader
	 * @param modelBuff
	 */
	//public TransferableModel(BufferedReader reader, 
	//		HashMap<String, TransferableModel> modelBuff) {
	//}
	
	public TransferableModel() {}
	
	/**
	 * Called by the envelope as its about to dump itself to a stream. In this
	 * method a model should add all its sub models (i.e. all fields within the
	 * model that themselves are models).
	 * 
	 * @param envelope
	 */
	public abstract void addSubModels(ModelEnvelope envelope);
	
	/**
	 * Called by the envelope right after all models has been added. In this
	 * the model should dump its data in the format of its choosing, but any
	 * dependant models should be dumped as ID strings.
	 * 
	 * @param sb
	 */
	public abstract void toStringBuilder(StringBuilder sb);	
		
	/**
	 * Return the value of the primary key identifier for this model
	 *
	 * @return ID or null if this cannot be identified (not yet stored)
	 */
	public abstract String getUMID();
}

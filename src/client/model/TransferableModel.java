package client.model;

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
	 * Get a unique model id, used by the cache manager
	 * 
	 * @return a string ID or null if the object is unidentifiable
	 */
	public String getUMID() {
		if(getMID() != null)
			return getClass().getName()+"_"+getMID().toString();
		return null;
	}
	
	/**
	 * Return the value of the primary key identifier for this model
	 *
	 * @return ID or null if this cannot be identified (not yet stored)
	 */
	protected abstract Object getMID();
}

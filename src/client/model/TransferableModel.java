package client.model;

import server.ModelEnvelope;

/**
 * Super class for all models that may be packed in a ModelEnvelope for
 * transfering between client and server.
 * 
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public interface TransferableModel {	
	
	/**
	 * Tell the model to copy the data in source. 
	 * 
	 * This is a part of the model cacher, when it receive a model it already
	 * have cached it will call this on the already cached model with the newly
	 * received model as source. That way models changes are relfected
	 * automagically (tm)
	 * 
	 * @param source
	 */
	public void copyFrom(TransferableModel source);
	
	/**
	 * Called by the envelope as its about to dump itself to a stream. In this
	 * method a model should add all its sub models (i.e. all fields within the
	 * model that themselves are models).
	 * 
	 * @param envelope
	 */
	public void addSubModels(ModelEnvelope envelope);
	
	/**
	 * Called by the envelope right after all models has been added. In this
	 * the model should dump its data in the format of its choosing, but any
	 * dependant models should be dumped as ID strings.
	 * 
	 * @param sb
	 */
	public void toStringBuilder(StringBuilder sb);	
		
	/**
	 * Pull in any dependencies
	 * 
	 * Last step on receiving end
	 * 
	 * @param modelBuff
	 */
	public void registerSubModels(ModelEnvelope envelope);
	
	/**
	 * Return the value of the primary key identifier for this model
	 *
	 * @return ID or null if this cannot be identified (not yet stored)
	 */
	public String getUMID();
}

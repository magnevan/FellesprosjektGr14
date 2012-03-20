package client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.model.IDBStorableModel;
import client.model.TransferableModel;

/**
 * The model cacher is a middle man between the ServerConnection and all
 * other code. Upon reciving a model from the server the model cacher
 * will cache the model, making sure that only a single instance of each
 * model representing the same data exists. And thereby keeping all existing
 * instances of a model up to date
 * 
 * @TODO This is just a proof of consept needs a lot more work
 * 
 * @author Runar B. Olsen
 */
public abstract class ModelCacher {

	/**
	 * Map containing cached models identified by their unique model id string
	 */
	private static Map<String, TransferableModel> cache = Collections.synchronizedMap(
			new HashMap<String, TransferableModel>()
		);
	
	/**
	 * Caches the given model
	 * 
	 * @param model
	 * @return
	 */
	public static TransferableModel cache(TransferableModel model) {
		// Do not cache unidentifiable models
		if(model.getUMID() == null)
			return model;
		
		if(cache.containsKey(model.getUMID())) {
			ModelCacher.update(model);
		} else {
			cache.put(model.getUMID(), model);
		}
		return cache.get(model.getUMID());
	}
	
	/**
	 * Updates our version of the given model, if it exists
	 * 
	 * @param model
	 * @return
	 */
	public static TransferableModel update(TransferableModel model) {
		if(model instanceof IDBStorableModel) 
			return model;
		
		// @TODO this does nothing yet
		return model;
	}
	
	/**
	 * Free the given model from the cache
	 * 
	 * Models should be freed upon being dropped in the client code. If not
	 * dropped the model will remain in cache even if dropped elsewere and we've
	 * got a memory leak.
	 * 
	 * @param model
	 */
	public static void free(TransferableModel model) {
		if(model.getUMID() != null) {
			cache.remove(model.getUMID());
		}
	}
	
	/**
	 * Free a list of models {@see free(TransferableModel)}
	 * 
	 * @param models
	 */
	public static void free(List<TransferableModel> models) {
		for(TransferableModel m : models) {
			free(m);
		}
	}
			
	
}

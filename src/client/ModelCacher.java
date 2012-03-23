package client;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import client.model.TransferableModel;

/**
 * The model cacher is a middle man between the ServerConnection and all
 * other code. Upon receiving a model from the server the model cacher
 * will cache the model, making sure that only a single instance of each
 * model representing the same data exists. And thereby keeping all existing
 * instances of a model up to date.
 * 
 * The cache uses a weak reference when storing the models it caches,
 * as a result the cache will not keep the models from GC after all other
 * code has dropped their references to it.
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public abstract class ModelCacher {

	/**
	 * Map containing cached models identified by their unique model id string
	 */
	private static Map<String, WeakReference<TransferableModel>> cache = 
			Collections.synchronizedMap(
					new HashMap<String, WeakReference<TransferableModel>>()
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
		
		if(containsKey(model.getUMID())) {
			ModelCacher.update(model);
		} else {
			put(model.getUMID(), model);
		}
		return get(model.getUMID());
	}
	
	/**
	 * Updates our version of the given model, if it exists
	 * 
	 * @param model
	 * @return
	 */
	public static TransferableModel update(TransferableModel model) {
		if(model.getUMID() != null && containsKey(model.getUMID())) {
			get(model.getUMID()).copyFrom(model);
			return get(model.getUMID());
		}
		return null;
	}
	
	/**
	 * Add a TransferableModel to the map
	 * 
	 * @param umid
	 * @param model
	 */
	private static void put(String umid, TransferableModel model) {
		if(!containsKey(umid)) {
			cache.put(umid, new WeakReference<TransferableModel>(model));
		}
	}
	
	/**
	 * Get the model identified by the given umid, or null if we've got none
	 * 
	 * @param umid
	 * @return
	 */
	private static TransferableModel get(String umid) {
		if(containsKey(umid)) {
			return cache.get(umid).get();
		}
		return null;
	}
	
	/**
	 * Check if the given umid exists, and if it does make sure the object is
	 * still with us
	 * 
	 * @param umid
	 * @return
	 */
	private static boolean containsKey(String umid) {
		if(cache.containsKey(umid)) {
			if(cache.get(umid).get() != null) {
				return true;
			}
			cache.remove(umid);
		}
		return false;
	}
			
	
}

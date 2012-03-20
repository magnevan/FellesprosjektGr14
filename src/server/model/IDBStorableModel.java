package server.model;

import server.DBConnection;

/**
 * Interface for server models
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public interface IDBStorableModel {

	/**
	 * Saves the model to db
	 * 
	 * @param db
	 * @return
	 */
	public void store(DBConnection db);
	
}

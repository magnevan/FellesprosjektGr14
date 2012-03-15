package server.model;

/**
 * Interface for server models
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public interface IServerModel {

	/**
	 * Saves the model to db, and returns the newly stores model
	 * 
	 * The returned model should replace the old in any preceeding code
	 * 
	 * @return
	 */
	public IServerModel store();
	
}

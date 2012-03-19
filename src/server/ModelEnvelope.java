package server;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import client.model.TransferableModel;

/**
 * A model envelope contains a model thats about to be transfered between
 * the user and the server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ModelEnvelope {

	/**
	 * Stack of models that will be transfered, the bottom one is the actual
	 * model
	 */
	private Stack<TransferableModel> models;
	
	/**
	 * Models that has already been sent
	 */
	private HashSet<String> modelUMIDs;
	
	public ModelEnvelope(TransferableModel model) {
		models = new Stack<TransferableModel>();
		modelUMIDs = new HashSet<String>();
	}
	
	/**
	 * True if the given model already has been added to the envelope
	 * 
	 * @param model
	 * @return
	 */
	public boolean hasModel(TransferableModel model) {
		return modelUMIDs.contains(model.getUMID());
	}
	
	/**
	 * Add the given model 
	 * 
	 * @param model
	 */
	public void addModel(TransferableModel model) {
		models.push(model);
		modelUMIDs.add(model.getUMID());
	}
	
	/**
	 * Writes the contents of the envelope to a stream
	 * 
	 * @param writer
	 */
	public void writeToStream(BufferedWriter writer) {
		// Have the base model add all its sub models, models are only added if
		// the envelope doesn't contain it
		models.get(0).addSubModels(this);
		
		StringBuilder sb = new StringBuilder();
		for(TransferableModel m : models) {
			// toEnvelope writes only the data directly linked to the model
			// any relations are represented by MUIDs and will be reassembled 
			// on the other side
			m.toEnvelope(sb);
		}
		
		writer.write(sb.toString());
	}
}
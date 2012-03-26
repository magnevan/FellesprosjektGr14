package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import server.model.ServerActiveUserModel;
import server.model.ServerInvitationModel;
import server.model.ServerMeetingModel;
import server.model.ServerMeetingRoomModel;
import server.model.ServerNotificationModel;
import server.model.ServerUserModel;
import client.ModelCacher;
import client.model.ActiveUserModel;
import client.model.InvitationModel;
import client.model.MeetingModel;
import client.model.MeetingRoomModel;
import client.model.NotificationModel;
import client.model.TransferableModel;
import client.model.UserModel;

/**
 * A model envelope contains a model thats about to be transfered between
 * the user and the server
 * 
 * @author Runar B. Olsen <runar.b.olsen@gmail.com>
 */
public class ModelEnvelope {

	/**
	 * Model envelope header
	 */
	private static final String ENVELOPE_HEADER = "MODEL ENVELOPE";
	
	/**
	 * Model envelope footer
	 */
	private static final String ENVELOPE_FOOTER = "\0MODEL ENVELOPE";
	
	/**
	 * Models that are about to be transported over the socket
	 */
	private Stack<TransferableModel> models;
	
	/**
	 * Set of already added MUIDs
	 * 
	 * Used to check if you need to add a sub model 
	 */
	private HashSet<String> modelUMIDs;
	
	/**
	 * Array of countModels boolean each indicating if the i'th model in a
	 * client side model envelope that was read off stream was new to the
	 * client
	 */
	private boolean[] newFlags;
	
	/**
	 * The number of models that are about to be transfered.
	 * 
	 * This is the number of actual models and not the number of submodels.
	 */
	private int countModels;
	
	/**
	 * Buffer while the envelope is being read
	 */
	private HashMap<String, TransferableModel> modelBuff;
	
	/**
	 * True if we're on server
	 */
	private boolean server;
	
	/**
	 * Create a new ModelEnvelope sending a single model
	 * 
	 * @param model
	 */
	public ModelEnvelope(TransferableModel model) {
		this(Arrays.asList(model));
	}
	
	/**
	 * Create a new ModelEnvelope sending over a list of models
	 * 
	 * @param models
	 */
	public ModelEnvelope(List<TransferableModel> models) {
		this.models = new Stack<TransferableModel>();
		modelUMIDs = new HashSet<String>();
		countModels = models.size();
		newFlags = new boolean[countModels];
		
		// Add models one by one
		for(TransferableModel m : models) {
			this.models.push(m);
			if(m.getUMID() != null)
				modelUMIDs.add(m.getUMID());
		}
	}
	
	/**
	 * Construct a ModelEnvelope based on the given reader
	 * 
	 * @param reader
	 * @param server true if we're server side, false on client side
	 */
	public ModelEnvelope(BufferedReader reader, boolean server) throws IOException {
		modelBuff = new HashMap<String, TransferableModel>(); 
		this.server = server;
		
		String line = reader.readLine();
		if(line == null)
			throw new EOFException("Expected envelope header got EOF");
		if(!line.equals(ENVELOPE_HEADER))
			throw new IOException("Expected envelope header, found "+line);
		
		int numModels = Integer.parseInt(reader.readLine());
		countModels = Integer.parseInt(reader.readLine());
		
		TransferableModel[] list = new TransferableModel[numModels];
		newFlags = new boolean[countModels];
		
		// create models
		try {
			for(int i = 0; i < numModels; i++) {	
				String name = reader.readLine(),
						umid = reader.readLine();
				list[i] = createModel(name, reader, server);
				if(!umid.equals("")) {
					modelBuff.put(umid, list[i]);
				}
			}
		} catch(IOException e) {
			// Attempt to clear the remainder of the envelope off stream
			while(!reader.readLine().equals(ENVELOPE_FOOTER)) ;
			throw new IOException("Exception reading model", e);
		}
		
		// Validate that we're at the end
		if(!(line = reader.readLine()).equals(ENVELOPE_FOOTER)) {
			throw new IOException("Expected envelope footer line, got "+line);
		}
		
		// Have all models pull in dependencies, and register models in cacher
		for(int i = 0; i < list.length; i++) {
			list[i].registerSubModels(this);
			
			if((numModels-i) <= countModels)
				newFlags[countModels - (numModels-i)] = ModelCacher.containsKey(list[i].getUMID());
			list[i] = ModelCacher.cache(list[i]);
		}
		
		models = new Stack<TransferableModel>();
		modelUMIDs = new HashSet<String>();
		
		// Turn around the models and push to list
		for(int i = list.length-1; i >= list.length-countModels; i--) {
			models.push(list[i]);
			modelUMIDs.add(list[i].getUMID());
		}
	}

	/**
	 * Get a entry for the model buffer
	 * 
	 * @param umid
	 * @return
	 */
	public TransferableModel getFromBuffer(String umid) {
		if(!server && ModelCacher.containsKey(umid)) {
			return ModelCacher.get(umid);
		} 
		return modelBuff.get(umid);
	}
	
	/**
	 * Get the number of models in the envelope
	 * 
	 * @return
	 */
	public int getModelCount() {
		return countModels;
	}
	
	/**
	 * True if the given model already has been added to the envelope
	 * 
	 * @param model
	 * @return
	 */
	private boolean hasModel(TransferableModel model) {
		return modelUMIDs.contains(model.getUMID());
	}
	
	/**
	 * Add the given model 
	 * 
	 * @param model
	 */
	public void addModel(TransferableModel model) {
		if(!hasModel(model)) {
			models.push(model);
			modelUMIDs.add(model.getUMID());
			model.addSubModels(this);
		}
	}
	
	/**
	 * Get the transfered models
	 * 
	 * @return
	 */
	public List<TransferableModel> getModels() {
		return models.subList(0, getModelCount());
	}
	
	/**
	 * True if the i'th model in a client side stream created model envelope
	 * was new to the cache before it was received this time
	 * 
	 * @param i
	 * @return
	 */
	public boolean isNewModel(int i) {
		if(i >= countModels)
			throw new IllegalArgumentException("Index out of bounds "+i);
		return newFlags[i];
	}
	
	/**
	 * Writes all the models in this envelope to the given BufferedWriter 
	 * 
	 * This may be read back using the ModelEnvelope constructor with a
	 * BufferedReader instance
	 * 
	 * @param writer
	 */
	public void writeToStream(BufferedWriter writer) throws IOException {
		// Have the base model add all its sub models, models are only added if
		// the envelope doesn't contain it.
		for(int i = 0; i < countModels; i++) {
			models.get(i).addSubModels(this);
		}
		
		StringBuilder sb = new StringBuilder();
		
		// Write header data
		sb.append(ENVELOPE_HEADER+"\r\n");
		sb.append(models.size() + "\r\n" + getModelCount() + "\r\n");
		
		// Write models in reverse order to make sure all dependencies are provided
		// before a model is read on the other side
		for(int i = models.size()-1; i >= 0; i --) {			
			TransferableModel m = models.get(i);
			
			// Write model header string, so we know which model to initialize
			// the other side
			sb.append(getModelName(m)+"\r\n");
			
			// Write the models UMID if we got it
			if(m.getUMID() != null)
				sb.append(m.getUMID());
			sb.append("\r\n");
			
			// Have the model itself dump its data to the buffer
			m.toStringBuilder(sb);
		}
		
		// Append envelope footer line
		sb.append(ENVELOPE_FOOTER+"\r\n");
		
		writer.write(sb.toString());
	}
	
	/**
	 * Get the name identifying the model passed
	 * 
	 * This name is used on the receiving side to generate the actual model class
	 * 
	 * @param model
	 */
	private String getModelName(TransferableModel model) {
			if(model instanceof MeetingModel) {
				return "MeetingModel";
			} else if(model instanceof ActiveUserModel) {
				return "ActiveUserModel";
			} else if(model instanceof UserModel) {
				return "UserModel";
			} else if(model instanceof MeetingRoomModel) {
				return "MeetingRoomModel";
			} else if(model instanceof InvitationModel) {
				return "InvitationModel";
			} else if(model instanceof NotificationModel) {
				return "NotificationModel";
			}
			throw new IllegalArgumentException("Unknown model type passed to getModelName" +
					" "+model.getClass().getName());
	}
	
	/**
	 * Create a model object from the provided data
	 * 
	 * @param modelName
	 * @param reader
	 * @param modelBuff
	 * @param server
	 * @return
	 */
	private TransferableModel createModel(String modelName,
			BufferedReader reader, boolean server) throws IOException {

		TransferableModel model = null;
		if(modelName.equals("UserModel"))
			if(server) 
				model = new ServerUserModel(reader);
			else
				model = new UserModel(reader);
		else if(modelName.equals("MeetingModel"))
			if(server)
				model = new ServerMeetingModel(reader);
			else
				model = new MeetingModel(reader);
		else if(modelName.equals("MeetingRoomModel"))
			if(server)
				model = new ServerMeetingRoomModel(reader);
			else
				model = new MeetingRoomModel(reader);
		else if(modelName.equals("InvitationModel"))
			if(server)
				model = new ServerInvitationModel(reader);
			else
				model = new InvitationModel(reader);
		else if(modelName.equals("NotificationModel"))
			if(server)
				model = new ServerNotificationModel(reader);
			else
				model = new NotificationModel(reader);
		else if(modelName.equals("ActiveUserModel"))
			if(server)
				model = new ServerActiveUserModel(reader);
			else
				model = new ActiveUserModel(reader);
				
		return model;		
	}
	
}
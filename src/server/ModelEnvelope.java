package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	 * The number of models that are about to be transfered.
	 * 
	 * This is the number of actual models and not the number of submodels.
	 */
	private int countModels;
	
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
		HashMap<String, TransferableModel> modelBuff = new HashMap<String, TransferableModel>(); 
		
		String line = reader.readLine();
		if(!line.equals("MODEL ENVOLOPE")) {
			throw new IOException("Expected envelope header, found "+line);
		}
		
		int numModels = Integer.parseInt(reader.readLine());
		countModels = Integer.parseInt(reader.readLine());
		
		TransferableModel[] list = new TransferableModel[numModels];
		
		// create models
		for(int i = 0; i < numModels; i++) {	
			String name = reader.readLine(),
					umid = reader.readLine();
			list[i] = createModel(name, reader, modelBuff, server);
			if(!umid.equals(""))
				modelBuff.put(umid, list[i]);
		}
		
		// Validate that we're at the end
		if(!reader.readLine().equals("")) {
			throw new IOException("Expected empty line after envelope, got "+line);
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
	public boolean hasModel(TransferableModel model) {
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
	 * Writes all the models in this envolope to the given BufferedWriter 
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
		sb.append("MODEL ENVOLOPE\r\n");
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
		
		// Append empty seperator line, and write the data
		sb.append("\r\n");		
		writer.write(sb.toString());
	}
	
	/**
	 * Get the name identifing the model passed
	 * 
	 * This name is used on the reciving side to generate the actual model class
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
			BufferedReader reader, HashMap<String, TransferableModel> modelBuff,
			boolean server) {

		TransferableModel model = null;
		try {
			if(modelName.equals("UserModel"))
				if(server) 
					model = new ServerUserModel(reader, modelBuff);
				else
					model = new UserModel(reader, modelBuff);
			else if(modelName.equals("MeetingModel"))
				if(server)
					model = new ServerMeetingModel(reader, modelBuff);
				else
					model = new MeetingModel(reader, modelBuff);
			else if(modelName.equals("MeetingRoomModel"))
				if(server)
					model = new ServerMeetingRoomModel(reader, modelBuff);
				else
					model = new MeetingRoomModel(reader, modelBuff);
			else if(modelName.equals("InvitationModel"))
				if(server)
					model = new ServerInvitationModel(reader, modelBuff);
				else
					model = new InvitationModel(reader, modelBuff);
			else if(modelName.equals("NotificationModel"))
				if(server)
					model = new ServerNotificationModel(reader, modelBuff);
				else
					model = new NotificationModel(reader, modelBuff);
			else if(modelName.equals("ActiveUserModel"))
				if(server)
					model = new ServerActiveUserModel(reader, modelBuff);
				else
					model = new ActiveUserModel(reader, modelBuff);
					
			// If we're on client run model through the cacher 
			if(!server && model != null) 
				model = ModelCacher.cache(model);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return model;		
	}
	
}
package client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public abstract class Model {
	
	public abstract void fromStream(BufferedReader stream) throws IOException ;
	
	public abstract void toStream(BufferedWriter stream) throws IOException ;
	
}

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class DebugReader extends BufferedReader {

	public DebugReader(Reader arg0) {
		super(arg0);
	}

	@Override
	public String readLine() throws IOException {
		String line = super.readLine();
		System.out.println("<< "+line);
		return line;
	}
	
}

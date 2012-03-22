package client;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;


public class DebugWriter extends BufferedWriter {

	public DebugWriter(Writer arg0) {
		super(arg0);
	}

	@Override
	public void write(String s) throws IOException {
		super.write(s);
		System.out.print(">> "+s);
	}
	
}

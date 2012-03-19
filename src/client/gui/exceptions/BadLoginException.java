package client.gui.exceptions;

import java.net.ConnectException;

public class BadLoginException extends ConnectException {
	public BadLoginException() {
		super("Bad Login");
	}
}

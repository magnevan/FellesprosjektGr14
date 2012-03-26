package client.gui.exceptions;

import java.net.ConnectException;

public class BadLoginException extends ConnectException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6427705648042452238L;

	public BadLoginException() {
		super("Bad Login");
	}
}

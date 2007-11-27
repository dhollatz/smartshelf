package de.haw.smartshelf.reader;

public class NoSuchReaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3197753484612714726L;

	public NoSuchReaderException() {
	}

	public NoSuchReaderException(String message) {
		super(message);
	}

	public NoSuchReaderException(Throwable cause) {
		super(cause);
	}

	public NoSuchReaderException(String message, Throwable cause) {
		super(message, cause);
	}

}

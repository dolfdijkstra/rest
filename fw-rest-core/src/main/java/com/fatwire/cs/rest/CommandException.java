package com.fatwire.cs.rest;

public class CommandException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2035227170427028382L;

	public CommandException() {
		super();
	}

	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandException(String message) {
		super(message);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}

}

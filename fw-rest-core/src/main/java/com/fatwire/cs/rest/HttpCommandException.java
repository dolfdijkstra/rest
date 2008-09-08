package com.fatwire.cs.rest;

public class HttpCommandException extends CommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1233299147628309435L;

	final int status;

	private byte[] body;

	public HttpCommandException(String message,int status) {
		super(message);
		this.status = status;
	}
	public HttpCommandException(Throwable t,int status) {
		super(t);
		this.status = status;
	}

	public HttpCommandException(String message,int status, byte[] body) {
		super(message);
		this.status = status;
		this.body = body;
	}

	public byte[] getBody() {
		return body;
	}

	public int getStatus() {
		return status;
	}

}

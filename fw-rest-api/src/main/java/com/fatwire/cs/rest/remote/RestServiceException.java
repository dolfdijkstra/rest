package com.fatwire.cs.rest.remote;


public class RestServiceException extends Exception {



	/**
     * 
     */
    private static final long serialVersionUID = -7563793869483467392L;

    public RestServiceException() {
		super();
	}

	public RestServiceException(String s) {
		super(s);
	}

	public RestServiceException(String s, Throwable cause) {
		super(s, cause);
	}

}

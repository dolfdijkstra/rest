package com.fatwire.cs.rest.remote;

/**
 * This exception is thrown when the remote server has responded with a error message, usually in xml format. 
 * @author Dolf.Dijkstra
 * @since Sep 3, 2008
 */
public class RemoteFaultException extends RestServiceException {

    private final String errorCode;

    private final String errorString;

    private final String errorActor;

    /**
     * 
     */
    private static final long serialVersionUID = 266541446181500428L;

    public RemoteFaultException(final String errorCode,
            final String errorString, final String errorActor) {
        super();
        this.errorCode = errorCode;
        this.errorString = errorString;
        this.errorActor = errorActor;
    }

    public RemoteFaultException(String message, Throwable t,
            final String errorCode, final String errorString,
            final String errorActor) {
        super(message, t);
        this.errorCode = errorCode;
        this.errorString = errorString;
        this.errorActor = errorActor;

    }

    public RemoteFaultException(String message, final String errorCode,
            final String errorString, final String errorActor) {
        super(message);
        this.errorCode = errorCode;
        this.errorString = errorString;
        this.errorActor = errorActor;

    }

    public String getErrorActor() {
        return errorActor;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorString() {
        return errorString;
    }

}

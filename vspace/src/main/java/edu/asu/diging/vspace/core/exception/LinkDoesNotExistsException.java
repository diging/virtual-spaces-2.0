package edu.asu.diging.vspace.core.exception;

public class LinkDoesNotExistsException extends Exception{
    private static final long serialVersionUID = 1L;

    public LinkDoesNotExistsException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public LinkDoesNotExistsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public LinkDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public LinkDoesNotExistsException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public LinkDoesNotExistsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}

package edu.asu.diging.vspace.core.exception;

public class LinkDoesNotExistsException extends Exception{
    private static final long serialVersionUID = 1L;

    public LinkDoesNotExistsException() {
        super();
    }

    public LinkDoesNotExistsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LinkDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkDoesNotExistsException(String message) {
        super(message);
    }

    public LinkDoesNotExistsException(Throwable cause) {
        super(cause);
    }
}

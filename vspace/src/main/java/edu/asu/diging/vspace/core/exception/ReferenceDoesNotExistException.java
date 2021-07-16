package edu.asu.diging.vspace.core.exception;

public class ReferenceDoesNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;    

    public ReferenceDoesNotExistException() {
        super();
    }

    public ReferenceDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ReferenceDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceDoesNotExistException(String message) {
        super(message);
    }

    public ReferenceDoesNotExistException(Throwable cause) {
        super(cause);
    }
}

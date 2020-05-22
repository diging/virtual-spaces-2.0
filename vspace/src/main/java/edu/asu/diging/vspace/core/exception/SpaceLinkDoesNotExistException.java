package edu.asu.diging.vspace.core.exception;

public class SpaceLinkDoesNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SpaceLinkDoesNotExistException() {
        super();
    }

    public SpaceLinkDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SpaceLinkDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpaceLinkDoesNotExistException(String message) {
        super(message);
    }

    public SpaceLinkDoesNotExistException(Throwable cause) {
        super(cause);
    }

}

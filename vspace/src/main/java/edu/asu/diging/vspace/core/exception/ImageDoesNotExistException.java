package edu.asu.diging.vspace.core.exception;

public class ImageDoesNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -6849852485214921351L;

    public ImageDoesNotExistException() {
        super();
    }

    public ImageDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ImageDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageDoesNotExistException(String message) {
        super(message);
    }

    public ImageDoesNotExistException(Throwable cause) {
        super(cause);
    }

}

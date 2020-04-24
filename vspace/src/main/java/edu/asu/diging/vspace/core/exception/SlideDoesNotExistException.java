package edu.asu.diging.vspace.core.exception;

public class SlideDoesNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -6849852485214921351L;

    public SlideDoesNotExistException() {
        super();
    }

    public SlideDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SlideDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public SlideDoesNotExistException(String message) {
        super(message);
    }

    public SlideDoesNotExistException(Throwable cause) {
        super(cause);
    }
}

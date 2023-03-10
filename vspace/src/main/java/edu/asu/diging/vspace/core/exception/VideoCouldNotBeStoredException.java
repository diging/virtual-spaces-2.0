package edu.asu.diging.vspace.core.exception;

public class VideoCouldNotBeStoredException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public VideoCouldNotBeStoredException() {
        super();
    }

    public VideoCouldNotBeStoredException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public VideoCouldNotBeStoredException(String message, Throwable cause) {
        super(message, cause);
    }

    public VideoCouldNotBeStoredException(String message) {
        super(message);
    }

    public VideoCouldNotBeStoredException(Throwable cause) {
        super(cause);
    }

}

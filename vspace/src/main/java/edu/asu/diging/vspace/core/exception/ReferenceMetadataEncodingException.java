package edu.asu.diging.vspace.core.exception;

public class ReferenceMetadataEncodingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;    

    public ReferenceMetadataEncodingException() {
        super();
    }

    public ReferenceMetadataEncodingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ReferenceMetadataEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceMetadataEncodingException(String message) {
        super(message);
    }

    public ReferenceMetadataEncodingException(Throwable cause) {
        super(cause);
    }
}

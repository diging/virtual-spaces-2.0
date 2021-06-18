package edu.asu.diging.vspace.core.exception;

public class BibliographyDoesNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -2632307868319819150L;

    public BibliographyDoesNotExistException() {
        super();
    }

    public BibliographyDoesNotExistException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BibliographyDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public BibliographyDoesNotExistException(String message) {
        super(message);
    }

    public BibliographyDoesNotExistException(Throwable cause) {
        super(cause);
    }

}

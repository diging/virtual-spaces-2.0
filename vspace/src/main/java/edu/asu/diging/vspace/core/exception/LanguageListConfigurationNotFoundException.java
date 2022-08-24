package edu.asu.diging.vspace.core.exception;

public class LanguageListConfigurationNotFoundException  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LanguageListConfigurationNotFoundException() {
        super();
    }

    public LanguageListConfigurationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LanguageListConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LanguageListConfigurationNotFoundException(String message) {
        super(message);
    }

    public LanguageListConfigurationNotFoundException(Throwable cause) {
        super(cause);
    }

}

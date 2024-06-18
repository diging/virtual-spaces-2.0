package edu.asu.diging.vspace.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exhibition snapshot folder not found")
public class ExhibitionSnapshotNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;
    
    public ExhibitionSnapshotNotFoundException() {
        super();
    }

    public ExhibitionSnapshotNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExhibitionSnapshotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExhibitionSnapshotNotFoundException(Throwable cause) {
        super(cause);
    }

    public ExhibitionSnapshotNotFoundException(String id) {
        super("Exhibition snapshot folder with id " + id + " not found");
    }
}

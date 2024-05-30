package edu.asu.diging.vspace.core.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Snapshot could not be created")
public class SnapshotCouldNotBeCreatedException extends Exception{
private static final long serialVersionUID = 1L;
    
    public SnapshotCouldNotBeCreatedException(String id) {
        super("Snapshot could not be created for snapshot task" + id + " not found");
    }
    
    public SnapshotCouldNotBeCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}

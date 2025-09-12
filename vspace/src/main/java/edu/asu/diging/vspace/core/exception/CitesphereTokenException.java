package edu.asu.diging.vspace.core.exception;

/**
 * Exception thrown when there are issues with Citesphere authentication tokens
 */
public class CitesphereTokenException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private final int statusCode;
    private final boolean isTokenExpired;
    
    public CitesphereTokenException(String message) {
        super(message);
        this.statusCode = 0;
        this.isTokenExpired = false;
    }
    
    public CitesphereTokenException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.isTokenExpired = (statusCode == 401 || statusCode == 403);
    }
    
    public CitesphereTokenException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.isTokenExpired = false;
    }
    
    public CitesphereTokenException(String message, int statusCode, boolean isTokenExpired) {
        super(message);
        this.statusCode = statusCode;
        this.isTokenExpired = isTokenExpired;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public boolean isTokenExpired() {
        return isTokenExpired;
    }
}

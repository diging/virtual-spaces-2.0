package edu.asu.diging.vspace.core.exception;

/**
 * Thrown when an exhibition snapshot is requested but there are no published
 * spaces that could be exported.
 */
public class NoPublishedSpacesException extends SnapshotCouldNotBeCreatedException {
    private static final long serialVersionUID = 1L;

    public NoPublishedSpacesException() {
        super("The exhibition has no published spaces to export.", null);
    }
}

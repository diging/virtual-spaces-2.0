package edu.asu.diging.vspace.web.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Space Not Found")
public class SpaceNotFoundException extends Exception {
    private static final long serialVersionUID = -3332292346834265371L;

    public SpaceNotFoundException(String id) {
        super("Space not found");
    }

}
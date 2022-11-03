package edu.asu.diging.vspace.core.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Space Not Found")
public class SpaceNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public SpaceNotFoundException(String id) {
        super("Space with id "+ id +" not found");
    }

}
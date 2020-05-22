package edu.asu.diging.vspace.web.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Slide Not Found")
public class SlideNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public SlideNotFoundException(String id) {
        super("Slide with id " + id + " not found");
    }
}
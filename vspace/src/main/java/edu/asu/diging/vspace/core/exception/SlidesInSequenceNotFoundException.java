package edu.asu.diging.vspace.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Module Not Found")
public class SlidesInSequenceNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public SlidesInSequenceNotFoundException() {
        super("Selected Sequence contains no Slides to display");
    }

}
package edu.asu.diging.vspace.web.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Module Not Found")
public class SlidesInSequenceNotFoundException extends Exception {
    private static final long serialVersionUID = -3332292346834265371L;

    public SlidesInSequenceNotFoundException() {
        super("Selected Sequence contains no Slides to display");
    }

}
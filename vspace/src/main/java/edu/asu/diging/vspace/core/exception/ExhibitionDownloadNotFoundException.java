package edu.asu.diging.vspace.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exhibition download folder not found")
public class ExhibitionDownloadNotFoundException extends Exception{
    private static final long serialVersionUID = 1L;
    
    public ExhibitionDownloadNotFoundException(String id) {
        super("Exhibition download folder with id " + id + " not found");
    }
}
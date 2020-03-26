package edu.asu.diging.vspace.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Module Not Configured")
public class ModuleNotConfiguredException extends Exception {
    private static final long serialVersionUID = -3332292346834265371L;

    public ModuleNotConfiguredException(String id) {
        super("Sorry, module with id " + id + " has not been configured yet.");
    }

}
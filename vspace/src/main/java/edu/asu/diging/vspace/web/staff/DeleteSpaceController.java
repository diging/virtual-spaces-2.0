package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class DeleteSpaceController {
    
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/staff/space/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteSpace(@PathVariable String id) {
        spaceManager.deleteSpaceById(null);
    }
}

package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class DeleteSpaceController {
    
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/staff/space/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSpace(@PathVariable String id) {
        try {
            spaceManager.deleteSpaceById(id);
        } catch(IllegalArgumentException | EmptyResultDataAccessException exception) {
            return new ResponseEntity<>("Invalid input. Please try again", HttpStatus.BAD_REQUEST);
        } 
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

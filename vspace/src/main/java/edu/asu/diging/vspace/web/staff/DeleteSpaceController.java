package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class DeleteSpaceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/staff/space/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSpace(@PathVariable String id, Model model) {
        try {
            spaceManager.deleteSpaceById(id);
        } catch (SpaceDoesNotExistException exception) {
            logger.error("Could not delete space.", exception);
            return new ResponseEntity<>("Sorry, unable to delete space.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

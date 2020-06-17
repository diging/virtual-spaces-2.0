package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class EditExternalLinkController extends EditSpaceLinks{

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private ILinkManager linkManager;

    @RequestMapping(value = "/staff/space/link/external/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> createExternalLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("externalLinkLabel") String title, @RequestParam("url") String externalLink,
            @RequestParam("externalLinkIdValueEdit") String externalLinkIdValueEdit, @RequestParam("externalLinkDisplayId") String externalLinkDisplayId)
            throws SpaceDoesNotExistException, IOException, LinkDoesNotExistsException {
        
        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if(validation!=null) {
            return validation;
        }
        IExternalLinkDisplay display = linkManager.editExternalLink(title, id, new Float(x), new Float(y), externalLink, externalLinkIdValueEdit, externalLinkDisplayId);        
        return success(display.getExternalLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(), display.getRotation(), display.getExternalLink().getExternalLink());

    }
}

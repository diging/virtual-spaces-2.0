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
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class EditSpaceLinkController extends EditSpaceLinks{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ILinkManager linkManager;

    @RequestMapping(value = "/staff/space/link/space/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editSpaceLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("rotation") String rotation, @RequestParam("spaceLinkLabel") String title,
            @RequestParam("linkedSpace") String linkedSpaceId, @RequestParam("spaceLinkLabel") String spaceLinkLabel, 
            @RequestParam("spaceLinkIdValueEdit") String spaceLinkIdValueEdit, @RequestParam("spaceLinkDisplayId") String spaceLinkDisplayId)
                    throws NumberFormatException, SpaceDoesNotExistException, LinkDoesNotExistsException, IOException {

        
        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if(validation!=null) {
            return validation;
        }
        ISpaceLinkDisplay display;
        display = linkManager.editSpaceLink(title, id, new Float(x), new Float(y),
                    new Integer(rotation), linkedSpaceId, spaceLinkLabel, spaceLinkIdValueEdit, spaceLinkDisplayId);
        return success(display.getLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(), display.getRotation(), null);
    }

}


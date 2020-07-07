package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class EditModuleLinkController extends EditSpaceLinksController{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @RequestMapping(value = "/staff/space/link/module/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editModuleLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("rotation") String rotation, @RequestParam("moduleLinkLabel") String title,
            @RequestParam("linkedModule") String linkedModuleId, @RequestParam("moduleLinkLabel") String moduleLinkLabel, 
            @RequestParam("moduleLinkIdValueEdit") String moduleLinkIdValueEdit, @RequestParam("moduleLinkDisplayId") String moduleLinkDisplayId,
            @RequestParam("type") String displayType)
                    throws NumberFormatException, SpaceDoesNotExistException, LinkDoesNotExistsException, IOException, ImageCouldNotBeStoredException {

        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if(validation!=null) {
            return validation;
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        IModuleLinkDisplay display;
        display = moduleLinkManager.editLink(title, id, new Float(x), new Float(y),
                new Integer(rotation), linkedModuleId, moduleLinkLabel, moduleLinkIdValueEdit, moduleLinkDisplayId, type, null, null);
        return success(display.getLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(), display.getRotation(),null);
    }

}

package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class EditSpaceLinkController extends EditSpaceLinksController{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @RequestMapping(value = "/staff/space/link/space/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editSpaceLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("rotation") String rotation, @RequestParam("spaceLinkLabel") String title,
            @RequestParam("linkedSpace") String linkedSpaceId, @RequestParam("spaceLinkLabel") String spaceLinkLabel, 
            @RequestParam("spaceLinkDesc") String spaceLinkDesc, @RequestParam("spaceLinkIdValueEdit") String spaceLinkIdValueEdit, 
            @RequestParam("spaceLinkDisplayId") String spaceLinkDisplayId, 
            @RequestParam("type") String displayType, @RequestParam("spaceLinkImage") MultipartFile file)
                    throws NumberFormatException, SpaceDoesNotExistException, LinkDoesNotExistsException, IOException, ImageCouldNotBeStoredException {


        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if(validation!=null) {
            return validation;
        }
        byte[] linkImage = null;
        String filename = null;
        if (file != null) {
            linkImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        ISpaceLinkDisplay display = (ISpaceLinkDisplay) spaceLinkManager.updateLink(title, id, new Float(x), new Float(y),
                new Integer(rotation), linkedSpaceId, spaceLinkLabel, spaceLinkDesc, spaceLinkIdValueEdit, spaceLinkDisplayId, type, linkImage, filename);
        SpaceStatus targetSpaceStatus=spaceManager.getSpace(linkedSpaceId).getSpaceStatus();
        String linkedSpaceStatus = targetSpaceStatus!=null ? targetSpaceStatus.toString() : null;
        return success(display.getLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(), display.getRotation(), null, title,displayType,linkedSpaceId, linkedSpaceStatus);
    }

}


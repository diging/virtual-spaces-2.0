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
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
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
<<<<<<< HEAD
            @RequestParam("moduleLinkImageIdEdit") String moduleLinkImageIdEdit,
            @RequestParam("type") String displayType, @RequestParam(value="moduleLinkImage", required=false) MultipartFile file, @RequestParam(value="imageId", required=false) String imageId)
                    throws NumberFormatException, SpaceDoesNotExistException, LinkDoesNotExistsException, IOException, ImageCouldNotBeStoredException, ImageDoesNotExistException {
=======
            @RequestParam("type") String displayType, @RequestParam("moduleLinkImage") MultipartFile file)
                    throws NumberFormatException, SpaceDoesNotExistException, LinkDoesNotExistsException, IOException, ImageCouldNotBeStoredException {
>>>>>>> origin/develop

        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if(validation!=null) {
            return validation;
        }
        byte[] linkImage = null;
        String filename = null;
        if (file != null) {
            linkImage = file.getBytes();
            filename = file.getOriginalFilename();
<<<<<<< HEAD
        } else if(imageId==null || imageId.equals("")){
            String[] token = moduleLinkImageIdEdit.split("/");
            imageId = token[token.length - 1];
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        IModuleLinkDisplay display = (IModuleLinkDisplay) moduleLinkManager.updateLink(title, id, new Float(x), new Float(y),
                new Integer(rotation), linkedModuleId, moduleLinkLabel, moduleLinkIdValueEdit, moduleLinkDisplayId, type, linkImage, filename, imageId);
=======
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        IModuleLinkDisplay display = (IModuleLinkDisplay) moduleLinkManager.updateLink(title, id, new Float(x), new Float(y),
                new Integer(rotation), linkedModuleId, moduleLinkLabel, moduleLinkIdValueEdit, moduleLinkDisplayId, type, linkImage, filename);
>>>>>>> origin/develop
        return success(display.getLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(), display.getRotation(),null,title,displayType,linkedModuleId,null);
    }

}

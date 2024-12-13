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
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.ExternalLinkManager;

@Controller
public class EditExternalLinkController extends EditSpaceLinksController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;

    @RequestMapping(value = "/staff/space/link/external/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> createExternalLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("externalLinkLabel") String title,
            @RequestParam("url") String externalLink,
            @RequestParam("externalLinkIdValueEdit") String externalLinkIdValueEdit,
            @RequestParam("externalLinkDisplayId") String externalLinkDisplayId,
            @RequestParam("tabOpen") String howToOpen, @RequestParam("type") String displayType,
            @RequestParam("editExternalLinkInfoImage") MultipartFile file,
			@RequestParam(value = "editExternalLinkInfo-imageId", required = false) String imageId)
			throws SpaceDoesNotExistException, IOException, LinkDoesNotExistsException, NumberFormatException,
			ImageCouldNotBeStoredException, ImageDoesNotExistException {

        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if (validation != null) {
            return validation;
        }
        String desc = "";
        byte[] linkImage = null;
        String filename = null;
        if (file != null) {
            linkImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        ExternalLinkDisplayMode externalLinkOpenMode = howToOpen.isEmpty() ? null
                : ExternalLinkDisplayMode.valueOf(howToOpen);
        IExternalLinkDisplay display = (IExternalLinkDisplay) externalLinkManager.updateLink(title, id, new Float(x),
                new Float(y), 0, externalLink, title,desc, externalLinkIdValueEdit, externalLinkDisplayId, type, linkImage,
				filename, imageId);
        return success(display.getExternalLink().getId(), display.getId(), display.getPositionX(),
                display.getPositionY(), display.getRotation(), display.getExternalLink().getExternalLink(), title,
                displayType, null, null);

    }
}
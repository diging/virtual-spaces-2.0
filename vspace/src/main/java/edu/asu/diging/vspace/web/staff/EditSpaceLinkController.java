package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;

@Controller
public class EditSpaceLinkController extends EditSpaceLinksController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @RequestMapping(value = "/staff/space/link/space/{id}", method = RequestMethod.POST)
	public ResponseEntity<String> editSpaceLink(@PathVariable("id") String id, @RequestParam("x") String x,
		@RequestParam("y") String y, @RequestParam("rotation") String rotation,
		@RequestParam("spaceLinkLabel") String title, @RequestParam("linkedSpace") String linkedSpaceId,
		@RequestParam("spaceLinkLabel") String spaceLinkLabel,@RequestParam(value = "spaceLinkDesc", required = false) String spaceLinkDesc,
		@RequestParam("spaceLinkIdValueEdit") String spaceLinkIdValueEdit,
		@RequestParam("spaceLinkDisplayId") String spaceLinkDisplayId,
		@RequestParam("type") String displayType, @RequestParam(value="spaceLinkImage", required = false) MultipartFile file,
		@RequestParam(value = "imageId", required = false) String imageId) throws NumberFormatException,
		SpaceDoesNotExistException, LinkDoesNotExistsException, IOException, ImageCouldNotBeStoredException, ImageDoesNotExistException {

        ResponseEntity<String> validation = checkIfSpaceExists(spaceManager, id, x, y);
        if (validation != null) {
            return validation;
        }
        byte[] linkImage = null;
        String filename = null;
        if (file != null && !file.isEmpty()) {
            linkImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        if (file == null && (imageId == null || imageId.equals(""))) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "No image provided for space link.");
            return new ResponseEntity<String>(mapper.writeValueAsString(node), HttpStatus.BAD_REQUEST);
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        ISpaceLinkDisplay display = (ISpaceLinkDisplay) spaceLinkManager.updateLink(title, id, new Float(x),
				new Float(y), new Integer(rotation), linkedSpaceId, spaceLinkLabel,spaceLinkDesc, spaceLinkIdValueEdit,
				spaceLinkDisplayId, type, linkImage, filename,  imageId);
        SpaceStatus targetSpaceStatus = spaceManager.getSpace(linkedSpaceId).getSpaceStatus();
        String linkedSpaceStatus = targetSpaceStatus != null ? targetSpaceStatus.toString() : null;
        return success(display.getLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(),
				display.getRotation(), null, title, displayType, linkedSpaceId, linkedSpaceStatus);
    }

}

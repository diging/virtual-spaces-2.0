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
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.SpaceLinkManager;

@Controller
public class AddSpaceLinkController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private SpaceLinkManager spaceLinkManager;

    @RequestMapping(value = "/staff/space/{id}/spacelink", method = RequestMethod.POST)
    public ResponseEntity<String> createSpaceLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("rotation") String rotation, @RequestParam("spaceLinkLabel") String title,
            @RequestParam("linkedSpace") String linkedSpaceId, @RequestParam("spaceLinkLabel") String spaceLinkLabel,
            @RequestParam("type") String displayType, @RequestParam("spaceLinkImage") MultipartFile file)
                    throws NumberFormatException, SpaceDoesNotExistException, IOException {

        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
        }

        if (x == null || x.trim().isEmpty() || y == null || y.trim().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "No link coordinates specified.");
            return new ResponseEntity<String>(mapper.writeValueAsString(node), HttpStatus.BAD_REQUEST);
        }

        byte[] linkImage = null;
        String filename = null;
        if (file != null) {
            linkImage = file.getBytes();
            filename = file.getOriginalFilename();
        }

        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        ISpaceLinkDisplay display;
        try {
            display = spaceLinkManager.createLink(title, id, new Float(x), new Float(y),
                    new Integer(rotation), linkedSpaceId, spaceLinkLabel, type, linkImage, filename);
        } catch (ImageCouldNotBeStoredException e) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "Link icon could not be stored.");
            return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        display.setRotation(new Integer(rotation));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", display.getLink().getId());
        linkNode.put("displayId", display.getId());
        linkNode.put("x", display.getPositionX());
        linkNode.put("y", display.getPositionY());
        ISpace targetSpace=spaceManager.getSpace(linkedSpaceId);
        String linkedSpaceStatus = null;
        if(targetSpace!=null) {
            linkedSpaceStatus = targetSpace.getSpaceStatus().toString();
        }
        linkNode.put("linkedSpaceStatus", linkedSpaceStatus);
        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }

}

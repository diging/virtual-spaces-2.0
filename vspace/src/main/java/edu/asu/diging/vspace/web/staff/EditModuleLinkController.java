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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class EditModuleLinkController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ILinkManager linkManager;

    @RequestMapping(value = "/staff/space/link/module/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editModuleLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("rotation") String rotation, @RequestParam("moduleLinkLabel") String title,
            @RequestParam("linkedModule") String linkedModuleId, @RequestParam("moduleLinkLabel") String moduleLinkLabel, 
            @RequestParam("moduleLinkIdValueEdit") String moduleLinkIdValueEdit, @RequestParam("moduleLinkDisplayId") String moduleLinkDisplayId)
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
        IModuleLinkDisplay display;
        try {
            display = linkManager.editModuleLink(title, source, new Float(x), new Float(y),
                    new Integer(rotation), linkedModuleId, moduleLinkLabel, moduleLinkIdValueEdit, moduleLinkDisplayId);
        } catch (SpaceDoesNotExistException e) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "space could not be found.");
            return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", display.getLink().getId());
        linkNode.put("displayId", display.getId());
        linkNode.put("x", display.getPositionX());
        linkNode.put("y", display.getPositionY());

        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }

}

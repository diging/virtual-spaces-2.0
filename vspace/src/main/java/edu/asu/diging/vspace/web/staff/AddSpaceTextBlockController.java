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
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;

@Controller
public class AddSpaceTextBlockController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceTextBlockManager spaceTextBlockManager;


    @RequestMapping(value = "/staff/space/{id}/textblock", method = RequestMethod.POST)
    public ResponseEntity<String> createSpaceTextBlock(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("textContent") String text,
            @RequestParam("height") String height, @RequestParam("width") String width, @RequestParam("textColor") String textColor)
                    throws IOException{

        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
        }

        if (x == null || x.trim().isEmpty() || y == null || y.trim().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "No block coordinates specified.");
            return new ResponseEntity<String>(mapper.writeValueAsString(node), HttpStatus.BAD_REQUEST);
        }

        ISpaceTextBlockDisplay display=null;
        try {
            display = spaceTextBlockManager.createTextBlock(id, new Float(x), new Float(y),
                    text, new Float(height), new Float(width), textColor);
        } catch (SpaceDoesNotExistException e) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "space could not be found.");
            return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode textNode = mapper.createObjectNode();
        textNode.put("id", display.getSpaceTextBlock().getId());
        textNode.put("displayId", display.getId());
        textNode.put("x", display.getPositionX());
        textNode.put("y", display.getPositionY());

        return new ResponseEntity<>(mapper.writeValueAsString(textNode), HttpStatus.OK);
    }

}
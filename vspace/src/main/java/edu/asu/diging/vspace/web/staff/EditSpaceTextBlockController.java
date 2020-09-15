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

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;

@Controller
public class EditSpaceTextBlockController{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceTextBlockManager spaceTextBlockManager;

    @RequestMapping(value = "/staff/space/link/textBlock/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editSpaceTextBlock(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("textBlockIdValueEdit") String textBlockIdValueEdit, @RequestParam("textBlockDisplayId") String textBlockDisplayId, 
            @RequestParam("textContentEdit") String text,
            @RequestParam("height") String height, @RequestParam("width") String width)
                    throws NumberFormatException, SpaceDoesNotExistException, LinkDoesNotExistsException, IOException, ImageCouldNotBeStoredException {

        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
        }
//        if (x == null || x.trim().isEmpty() || y == null || y.trim().isEmpty()) {
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode node = mapper.createObjectNode();
//            node.put("errorMessage", "No link coordinates specified.");
//            return new ResponseEntity<String>(mapper.writeValueAsString(node), HttpStatus.BAD_REQUEST);
//        }
        
        ISpaceTextBlockDisplay display = (ISpaceTextBlockDisplay) spaceTextBlockManager.updateTextBlock(id, new Float(x), new Float(y),
                textBlockIdValueEdit, textBlockDisplayId, text, new Float(height), new Float(width));
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", id);
        linkNode.put("x", x);
        linkNode.put("y", y);
        linkNode.put("width", width);
        linkNode.put("height", height);
        linkNode.put("textContent", text);
        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
        
        //return success(display.getLink().getId(), display.getId(), display.getPositionX(), display.getPositionY(), display.getRotation(),null,title,displayType,linkedModuleId,null);
    }

}

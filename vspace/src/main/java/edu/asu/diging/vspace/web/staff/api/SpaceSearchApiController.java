package edu.asu.diging.vspace.web.staff.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class SpaceSearchApiController {
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/spaces/search/{id}")
    public ResponseEntity<String> searchSpace(@PathVariable("id") String id)
    		throws NumberFormatException, SpaceDoesNotExistException, IOException {
        ISpace space = spaceManager.getSpace(id);
        
        if (space == null) {
            return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode itemsArray = mapper.createArrayNode();
        ObjectNode spaceNode = mapper.createObjectNode();
        spaceNode.put("id", space.getId());
        spaceNode.put("name", space.getName());
        itemsArray.add(spaceNode);
        
        return new ResponseEntity<String>(itemsArray.toString(), HttpStatus.OK);
    }
}

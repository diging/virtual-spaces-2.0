package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class AddExternalLinkController {

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/staff/space/{id}/externallink", method = RequestMethod.POST)
    public ResponseEntity<String> createExternalLink(@PathVariable("id") String id, @RequestParam("x") String x,
            @RequestParam("y") String y, @RequestParam("url") String url)
            throws JsonProcessingException, NumberFormatException, SpaceDoesNotExistException {
        
        System.out.println("in ecternal kink controleev---------");

        ISpace space = spaceManager.getSpace(id);
        if (space == null) {
            return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
        }

        IExternalLinkDisplay display = spaceManager.createExternalLink("test", space, new Float(x), new Float(y), url);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("displayId", display.getId());
        linkNode.put("x", display.getPositionX());
        linkNode.put("y", display.getPositionY());
        linkNode.put("url", display.getExternalLink().getId());

        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }
}

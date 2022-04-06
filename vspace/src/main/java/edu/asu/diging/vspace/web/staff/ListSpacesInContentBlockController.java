package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ListSpacesInContentBlockController {
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @RequestMapping("/staff/spaces/search")
    public ResponseEntity<String> listSpacesForSlideContentBlock(@RequestParam(value = "term", required = false) String searchTerm){
        List<ISpace> spaces = null;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            spaces = spaceManager.findByName(searchTerm);
        } else {
            spaces = spaceManager.getAllSpaces();
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode idArray = mapper.createArrayNode();
        for (ISpace space : spaces) {
            ObjectNode imageNode = mapper.createObjectNode();
            imageNode.put("id", space.getId());
            imageNode.put("name", space.getName());
            idArray.add(imageNode);
        }
        return new ResponseEntity<String>(idArray.toString(), HttpStatus.OK);
    }

}

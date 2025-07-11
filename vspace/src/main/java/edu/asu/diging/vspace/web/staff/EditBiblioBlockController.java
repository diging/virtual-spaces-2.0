package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class EditBiblioBlockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/bibliography/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editTextBlock(@PathVariable("id") String slideId,
                                                @PathVariable("moduleId") String moduleId, 
                                                @RequestBody String biblioBlockData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(biblioBlockData);
        
        String biblioTitle = rootNode.get("biblioTitle").asText();
        String description = rootNode.get("description").asText();
        
        JsonNode idNode = rootNode.get("id");
        if (idNode == null) {
            logger.warn("No 'id' field found in the JSON data.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        String id = idNode.asText();
        
        IBiblioBlock biblioBlock = contentBlockManager.getBiblioBlock(id);
        
        if (biblioBlock == null) {
            logger.warn("BiblioBlock with id {} not found.", id);
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        
        biblioBlock.setBiblioTitle(biblioTitle);
        biblioBlock.setDescription(description);
        contentBlockManager.updateBiblioBlock((BiblioBlock) biblioBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}

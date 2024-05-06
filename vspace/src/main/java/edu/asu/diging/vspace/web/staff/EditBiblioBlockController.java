package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

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

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editBiblioBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestBody String biblioBlockData) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(biblioBlockData);

        // Extract biblioTitle and description from the JSON
        String biblioTitle = rootNode.get("biblioTitle").asText();
        String description = rootNode.get("description").asText();
        String id = rootNode.get("id").asText();
        
        IBiblioBlock biblioBlock = contentBlockManager.getBiblioBlock(id);
        biblioBlock.setBiblioTitle(biblioTitle);
        biblioBlock.setDescription(description);
        contentBlockManager.updateBiblioBlock((BiblioBlock) biblioBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
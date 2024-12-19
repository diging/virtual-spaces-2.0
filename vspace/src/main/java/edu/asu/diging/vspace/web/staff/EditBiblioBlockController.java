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

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/bibliography/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editTextBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestBody String biblioBlockData) throws IOException {
    	System.out.println("CP1\n\n\n\n\n");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(biblioBlockData);
        System.out.println("CP2\n\n\n\n\n");
        System.out.println(rootNode);
        System.out.println(biblioBlockData);
        
        String biblioTitle = rootNode.get("biblioTitle").asText();
        String description = rootNode.get("description").asText();
        String id = rootNode.get("id").asText();
        System.out.println(id);
        System.out.println("CP3\n\n");
        
        IBiblioBlock biblioBlock = contentBlockManager.getBiblioBlock(id);
        
        if (biblioBlock == null) {
        	System.out.println("CP4\n\n");
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        System.out.println("CP5\n\n\n");
        
        biblioBlock.setBiblioTitle(biblioTitle);
        biblioBlock.setDescription(description);
        
        System.out.println(biblioBlock);
        contentBlockManager.updateBiblioBlock((BiblioBlock) biblioBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
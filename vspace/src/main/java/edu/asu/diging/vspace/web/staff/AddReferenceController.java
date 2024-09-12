package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Controller
public class AddReferenceController {

    @Autowired
    private IReferenceManager referenceManager;
    
    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/bibliography/{biblioId}/references", method = RequestMethod.POST)
    public ResponseEntity<String> addReference(@PathVariable("id") String moduleId, @PathVariable("slideId") String slideId, 
            @PathVariable("biblioId") String biblioId, 
            @RequestBody String reference, Model model) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(reference);
        
        String title = rootNode.get("title").asText();
        String author = rootNode.get("author").asText();
        String year = rootNode.get("year").asText();
        String journal = rootNode.get("journal").asText();
        String url = rootNode.get("url").asText();
        String volume = rootNode.get("volume").asText();
        String issue = rootNode.get("issue").asText();
        String pages = rootNode.get("pages").asText();
        String editor = rootNode.get("editors").asText();
        String type = rootNode.get("type").asText();
        String note = rootNode.get("note").asText();
        
        IBiblioBlock biblio = contentBlockManager.getBiblioBlock(biblioId);
        IReference ref = referenceManager.createReference(biblio, title, author,year,journal,url,volume,issue,pages,editor,type,note);
        return new ResponseEntity<>(mapper.writeValueAsString(ref), HttpStatus.OK);
    }

}
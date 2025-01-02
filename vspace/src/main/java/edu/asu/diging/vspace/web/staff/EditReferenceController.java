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

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Controller
public class EditReferenceController {

    @Autowired
    private IReferenceManager referenceManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/bibliography/{biblioId}/reference/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editReference(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @PathVariable("biblioId") String biblioId, 
            @RequestBody Reference ref) throws IOException {
    	
    	System.out.print("XXXXXX\n\n\n\n\n\n\nXXXXXX");
    	System.out.print(ref.getId());
    	System.out.print("YYYYXXXXXX\n\n\n\n\n\n\nXXXXXX");
    	
    	IReference reference = referenceManager.getReference(ref.getId());
        reference.setTitle(ref.getTitle());
        reference.setAuthor(ref.getAuthor());
        reference.setYear(ref.getYear());
        reference.setJournal(ref.getJournal());
        reference.setUrl(ref.getUrl());
        reference.setVolume(ref.getVolume());
        reference.setIssue(ref.getIssue());
        reference.setPages(ref.getPages());
        reference.setEditors(ref.getEditors());
        reference.setType(ref.getType());
        reference.setNote(ref.getNote());
        referenceManager.updateReference((Reference) reference);
        
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode rootNode = mapper.readTree(ref);
//        
//        System.out.println(ref);
//        
//        String title = rootNode.get("title").asText();
//        String author = rootNode.get("author").asText();
//        String year = rootNode.get("year").asText();
//        String journal = rootNode.get("journal").asText();
//        String url = rootNode.get("url").asText();
//        String volume = rootNode.get("volume").asText();
//        String issue = rootNode.get("issue").asText();
//        String pages = rootNode.get("pages").asText();
//        String editor = rootNode.get("editors").asText();
//        String type = rootNode.get("type").asText();
//        String note = rootNode.get("note").asText();
//        String id = rootNode.get("id").asText();
//        
//        IReference reference = referenceManager.getReference(id);
//        System.out.print("XXXXXX\n\n\n\n\n\n\nXXXXXX");
//        System.out.print(id);
//        if (reference == null) {
//        	System.out.print("XXXXXXYYY\n\n\n\n\n\n\nXXXXXXYYYY");
//            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
//        }
//        
//        reference.setTitle(title);
//        reference.setAuthor(author);
//        reference.setYear(year);
//        reference.setJournal(journal);
//        reference.setUrl(url);
//        reference.setVolume(volume);
//        reference.setIssue(issue);
//        reference.setPages(pages);
//        reference.setEditors(editor);
//        reference.setType(type);
//        reference.setNote(note);
//        referenceManager.updateReference((Reference) reference);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
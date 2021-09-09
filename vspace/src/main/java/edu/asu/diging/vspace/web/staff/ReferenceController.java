package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IReferenceManager;
import edu.asu.diging.vspace.references.ReferenceDisplayProvider;

@Controller
public class ReferenceController {
    
    @Autowired
    private ReferenceRepository referenceRepo;
    
    @Autowired
    private IReferenceManager referenceManager;
    
    @Autowired
    private ReferenceDisplayProvider referenceDisplayProvider;

    @RequestMapping("/staff/display/reference/{id}")
    public String showReference(@PathVariable String id, Model model) {
        IReference reference = referenceRepo.findById(id).get();
        model.addAttribute("reference", reference);
        return "staff/references/reference";
    }
    
    @RequestMapping("/staff/module/reference/displaytext/{id}")
    public ResponseEntity<String> referenceDisplaytext(@PathVariable String id, Model model) {
        IReference reference = referenceRepo.findById(id).get();
        String refDisplayText = "";
        if(reference!=null) { 
            refDisplayText = referenceDisplayProvider.getReferenceDisplayText((Reference)reference);
            return new ResponseEntity<String>(refDisplayText, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }
    
    @RequestMapping("/staff/module/{moduleId}/slide/{id}/biblio/{biblioId}/reference/{refId}")
    public ResponseEntity<Reference> getReference(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @PathVariable("biblioId") String biblioId, 
            @PathVariable("refId") String refId) throws IOException {
        IReference reference = referenceManager.getReference(refId);
        if(reference!=null) { 
            return new ResponseEntity<Reference>((Reference) reference, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

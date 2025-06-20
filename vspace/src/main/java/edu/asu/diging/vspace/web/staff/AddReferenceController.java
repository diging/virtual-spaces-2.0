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

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.model.impl.ReferenceData;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Controller
public class AddReferenceController {

    @Autowired
    private IReferenceManager referenceManager; 

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/bibliography/{biblioId}/reference/add", 
                    method = RequestMethod.POST)
    public ResponseEntity<IReference> addReference(@PathVariable("id") String moduleId, 
                                                  @PathVariable("slideId") String slideId, 
                                                  @PathVariable("biblioId") String biblioId, 
                                                  @RequestBody ReferenceData referenceData, 
                                                  Model model) {
        IReference ref = referenceManager.createReference(
            biblioId, 
            referenceData.getTitle(), 
            referenceData.getAuthor(), 
            referenceData.getYear(), 
            referenceData.getJournal(), 
            referenceData.getUrl(), 
            referenceData.getVolume(), 
            referenceData.getIssue(), 
            referenceData.getPages(), 
            referenceData.getEditors(), 
            referenceData.getType(), 
            referenceData.getNote()
        );
        
        return ResponseEntity.ok(ref);
    }
}

package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.references.IReferenceDisplayProvider;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IReferenceManager;
import edu.asu.diging.vspace.web.model.ReferenceBlock;

@Controller
public class AddReferenceController {

    @Autowired
    private IReferenceManager referenceManager;
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private IReferenceDisplayProvider referenceDisplayProvider;

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/biblio/{biblioId}/reference/add", method = RequestMethod.POST)
    public ResponseEntity<ReferenceBlock> addReference(@PathVariable("id") String moduleId, @PathVariable("slideId") String slideId, 
            @PathVariable("biblioId") String biblioId, 
            @RequestBody Reference reference) throws JsonProcessingException {
        
        IBiblioBlock biblio = contentBlockManager.getBiblioBlock(biblioId);
        IReference ref = referenceManager.saveReference(biblio, reference);
       
        String refDisplayText = referenceDisplayProvider.getReferenceDisplayText(ref);
        ReferenceBlock refBlock = new ReferenceBlock(ref, refDisplayText);
        return new ResponseEntity<ReferenceBlock>(refBlock, HttpStatus.OK);
    }

}
package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IReferenceManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class DeleteReferenceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private IReferenceManager referenceManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/{biblioId}/reference/{refId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBiblioBlock(@PathVariable("moduleId") String moduleId, @PathVariable("id") String slideId, 
            @PathVariable("biblioId") String biblioId, @PathVariable("refId") String refId) throws IOException {
        
        ISlide slide = slideManager.getSlide(slideId);
        IModule module = moduleManager.getModule(moduleId);
        IBiblioBlock biblio = contentBlockManager.getBiblioBlock(biblioId);
        // If the slide Id, module Id and Biblio ID is not found, show message on screen.
        if(slide==null) {
            logger.warn("Slide Id does not exist, bad request.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        
        if(module==null) {
            logger.warn("Module Id does not exist, bad request.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        
        if(biblio==null) {
            logger.warn("Biblio Block Id does not exist, bad request.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            referenceManager.deleteReferenceById(refId, biblioId);
        } catch (Exception e) {
            logger.error("Reference id does not exist, bad request.", e);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
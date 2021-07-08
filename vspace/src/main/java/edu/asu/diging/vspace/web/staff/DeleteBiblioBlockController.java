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

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ReferenceListDeletionForBiblioException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class DeleteBiblioBlockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/{blockId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBiblioBlock(@PathVariable("moduleId") String moduleId, @PathVariable("id") String slideId, 
            @PathVariable("blockId") String blockId) throws IOException, ReferenceListDeletionForBiblioException {
        
        ISlide slide = slideManager.getSlide(slideId);
        IModule module = moduleManager.getModule(moduleId);

        if(slide==null) {
            logger.warn("Slide Id does not exist, bad request.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        
        if(module==null) {
            logger.warn("Module Id does not exist, bad request.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            contentBlockManager.deleteBiblioBlockById(blockId);
        } catch (BlockDoesNotExistException e) {
            logger.warn("Biblio Id does not exist, bad request.", e);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
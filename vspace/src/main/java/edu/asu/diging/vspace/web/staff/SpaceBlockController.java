package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class SpaceBlockController {
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/spaceblock/{blockId}", method = RequestMethod.GET)
    public ResponseEntity<String> getSpaceBlockSpace(@PathVariable("id") String slideId,
            @PathVariable("blockId") String blockId) throws IOException {
        ISpace space = null;
        try {
            space = contentBlockManager.getCurrentSpaceForSpaceBlock(blockId, slideId);
        } catch (BlockDoesNotExistException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<String>(space.getName(),HttpStatus.OK);
    }

}

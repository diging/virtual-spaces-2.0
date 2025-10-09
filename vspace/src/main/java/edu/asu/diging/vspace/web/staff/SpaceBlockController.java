package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class SpaceBlockController {
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/space/{blockId}", method = RequestMethod.GET)
    public ResponseEntity<ISpaceBlock> getSpaceBlock(@PathVariable("id") String slideId,
            @PathVariable("blockId") String blockId) throws IOException {
        ISpaceBlock spaceBlock = contentBlockManager.getSpaceBlock(blockId);
        if(spaceBlock == null) {
            return new ResponseEntity<ISpaceBlock>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ISpaceBlock>(spaceBlock,HttpStatus.OK);
    }

}

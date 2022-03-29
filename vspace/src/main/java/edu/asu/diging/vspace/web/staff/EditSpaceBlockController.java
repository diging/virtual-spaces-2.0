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
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class EditSpaceBlockController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/space/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editSpaceBlock(@PathVariable("id") String slideId,
            @RequestParam("spaceBlockId") String blockId, @PathVariable("moduleId") String moduleId,
            @RequestParam("spaceBlockTitle") String spaceBlockTitle,
            @RequestParam("spaceId") String spaceId) throws IOException {
        ISpaceBlock spaceBlock = contentBlockManager.getSpaceBlock(blockId);
        ISpace space = spaceManager.getSpace(spaceId);
        spaceBlock.setTitle(spaceBlockTitle);
        spaceBlock.setSpace(space);
        contentBlockManager.updateSpaceBlock((SpaceBlock) spaceBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/staff/module/slide/spaceblock/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getSpaceBlock(@PathVariable("id") String spaceBlockId)
            throws IOException {
        ISpaceBlock spaceBlock = contentBlockManager.getSpaceBlock(spaceBlockId);
        if(spaceBlock.getSpace() == null) {
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
        return new ResponseEntity<String>(spaceBlock.getSpace().getName(), HttpStatus.OK);
    }

}

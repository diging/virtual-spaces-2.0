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

import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class EditSpaceBlockController {
    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/space/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editTextBlock(@PathVariable("id") String slideId,
            @RequestParam("spaceBlockId") String blockId, @PathVariable("moduleId") String moduleId,
            @RequestParam("spaceBlockTitle") String spaceBlockTitle) throws IOException {
        ISpaceBlock spaceBlock = contentBlockManager.getSpaceBlock(blockId);
        spaceBlock.setTitle(spaceBlockTitle);
        contentBlockManager.updateSpaceBlock((SpaceBlock) spaceBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}

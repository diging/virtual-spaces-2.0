package edu.asu.diging.vspace.web.staff;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceContentBlockForm;

@Controller
public class AddSpaceBlockController {
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/space", method = RequestMethod.POST)
    ResponseEntity<String> addSpaceBlock(@RequestParam("title") String title,@RequestParam("spaceId") String spaceId, @PathVariable("slideId") String slideId,
            @PathVariable("moduleId") String moduleId){
        ISpace space = spaceManager.getSpace(spaceId);
        Integer contentOrder = contentBlockManager.findMaxContentOrder(slideId);
        contentOrder = contentOrder == null ? 0 : contentOrder + 1;
        ISpaceBlock spaceBlock = contentBlockManager.createSpaceBlock(slideId, title, contentOrder, space);
        return new ResponseEntity<>(spaceBlock.getId(), HttpStatus.OK);
    }

}

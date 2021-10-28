package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;

@Controller
public class AddSpaceBlockController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/image", method = RequestMethod.POST)
    ResponseEntity<String> addSpaceBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId){
        
        IModuleLink moduleLink = moduleLinkManager.getModuleLinkByModuleId(moduleId);
        ISpace space = moduleLink.getSpace();
        Integer contentOrder = contentBlockManager.findMaxContentOrder(slideId);
        contentOrder = contentOrder == null ? 0 : contentOrder + 1;
        contentBlockManager.createSpaceBlock(slideId, moduleId, contentOrder, space);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;

@Controller
public class DeleteLinkController {

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;
    
    @Autowired
    private ISpaceTextBlockManager spaceTextBlockManager;

    @RequestMapping(value = "/staff/space/{id}/modulelink/{linkId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteModuleLink(@PathVariable("id") String spaceId, @PathVariable("linkId") String linkId) {
        moduleLinkManager.deleteLink(linkId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/space/{id}/spacelink/{linkId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSpaceLink(@PathVariable("id") String spaceId, @PathVariable("linkId") String linkId) {
        spaceLinkManager.deleteLink(linkId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/space/{id}/externallink/{linkId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteExternalLink(@PathVariable("id") String spaceId, @PathVariable("linkId") String linkId) {
        externalLinkManager.deleteLink(linkId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/staff/space/{id}/textBlock/{blockId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSpaceTextBlock(@PathVariable("id") String spaceId, @PathVariable("blockId") String blockId) {
        spaceTextBlockManager.deleteTextBlock(blockId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}

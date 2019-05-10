package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.ILinkManager;

@Controller
public class DeleteModuleLinkController {
    
    @Autowired
    private ILinkManager linkManager;

    @RequestMapping(value = "/staff/space/{id}/modulelink/{linkId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteModuleLink(@PathVariable("id") String spaceId, @PathVariable("linkId") String linkId) {
        linkManager.deleteModuleLink(linkId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}

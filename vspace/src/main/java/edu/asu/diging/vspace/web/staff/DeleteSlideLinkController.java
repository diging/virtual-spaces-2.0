package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;

@Controller
public class DeleteSlideLinkController {

    @Autowired
    private ISlideExternalLinkManager slideExternalLinkManager;
    
    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/externallink/{linkId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSlideExternalLink(@PathVariable("id") String moduleId, @PathVariable("slideId") String slideId, @PathVariable("linkId") String linkId) {
        slideExternalLinkManager.deleteLink(linkId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}

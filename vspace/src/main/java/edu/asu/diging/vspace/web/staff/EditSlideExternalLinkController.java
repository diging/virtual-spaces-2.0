package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class EditSlideExternalLinkController extends EditSlideLinksController{
    
    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ISlideExternalLinkManager externalLinkManager;
    
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/link/external/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editExternalLink(@PathVariable("id") String id, @PathVariable("slideId") String slideId,
    		@RequestParam("externalLinkLabel") String title,
            @RequestParam("url") String externalLink
    	) throws SlideDoesNotExistException, IOException, LinkDoesNotExistsException, NumberFormatException {

        ResponseEntity<String> validation = checkIfSlideExists(slideManager, slideId);
        if (validation != null) {
            return validation;
        }

        IExternalLinkSlide link = externalLinkManager.updateExternalLink(title, externalLink, id);
        return success(link.getId(), link.getExternalLink(), title, null, 0, 0, 0,
        		null, null, null);

    }


}

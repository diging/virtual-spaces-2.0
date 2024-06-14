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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class AddSlideExternalLinkController {
    
    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ISlideExternalLinkManager slideExternalLinkManager;
        
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/externallink", method = RequestMethod.POST)
    public ResponseEntity<String> createSlideExternalLink(@PathVariable("id") String id, @PathVariable("moduleId") String moduleId, 
            @RequestParam("externalLinkLabel") String label, @RequestParam("url") String externalLink)
            throws NumberFormatException, SlideDoesNotExistException, IOException {

        ISlide slide = slideManager.getSlide(id);
        
        if (slide == null) {
            return new ResponseEntity<>("{'error': 'Slide could not be found.'}", HttpStatus.NOT_FOUND);
        }

        IExternalLinkSlide externalLinkSlide = slideExternalLinkManager.createExternalLink(label, externalLink, id);
        slide.getExternalLinks().add(externalLinkSlide);
    	slideManager.updateSlide(slide);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("label", externalLinkSlide.getLabel());
        linkNode.put("id", externalLinkSlide.getId());
        linkNode.put("url", externalLinkSlide.getExternalLink());
 
        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }

}

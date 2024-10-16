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

import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class EditSlideExternalLinkController {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ISlideExternalLinkManager externalLinkManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/link/external/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> editExternalLink(@PathVariable("id") String id,
            @PathVariable("slideId") String slideId, @RequestParam("externalLinkLabel") String title,
            @RequestParam("url") String externalLink)
            throws SlideDoesNotExistException, IOException, LinkDoesNotExistsException, NumberFormatException {

        ResponseEntity<String> validation = checkIfSlideExists(slideManager, slideId);
        if (validation != null) {
            return validation;
        }

        IExternalLinkSlide link = externalLinkManager.updateExternalLink(title, externalLink, id);
        return success(link.getId(), link.getExternalLink(), title, null, null);

    }
    
    public ResponseEntity<String> checkIfSlideExists(ISlideManager slideManager, String id) throws IOException{
        ISlide source = slideManager.getSlide(id);
        if (source == null) {
            return new ResponseEntity<>("{'error': 'Slide could not be found.'}", HttpStatus.NOT_FOUND);
        }
        return null;
    }
    
    public ResponseEntity<String> success(String id, String url, String label, String linkedId, String linkedSlideStatus) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", id);
        linkNode.put("label", label);
        linkNode.put("linkedId", linkedId);
        if(linkedSlideStatus!=null) {
            linkNode.put("linkedSlideStatus", linkedSlideStatus);
        }
        if(url!=null) {
            linkNode.put("url", url);
        }
        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }


}

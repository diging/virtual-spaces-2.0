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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class AddSlideExternalLinkController {
    
    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ISlideExternalLinkManager slideExternalLinkManager;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/externallink", method = RequestMethod.POST)
    public ResponseEntity<String> createSlideExternalLink(@PathVariable("id") String id, @PathVariable("moduleId") String moduleId, 
            @RequestParam("x") String x, @RequestParam("y") String y, @RequestParam("externalLinkLabel") String title,
            @RequestParam("url") String externalLink, @RequestParam("tabOpen") String howToOpen,
            @RequestParam("type") String displayType, @RequestParam("externalLinkImage") MultipartFile file)
            throws NumberFormatException, SlideDoesNotExistException, IOException, ImageCouldNotBeStoredException {

        ISlide slide = slideManager.getSlide(id);
        if (slide == null) {
            return new ResponseEntity<>("{'error': 'Slide could not be found.'}", HttpStatus.NOT_FOUND);
        }

        byte[] linkImage = null;
        String filename = null;
        if (file != null) {
            linkImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        DisplayType type = displayType.isEmpty() ? null : DisplayType.valueOf(displayType);
        ExternalLinkDisplayMode externalLinkOpenMode = howToOpen.isEmpty() ? null
                : ExternalLinkDisplayMode.valueOf(howToOpen);
        ISlideExternalLinkDisplay display = slideExternalLinkManager.createLink(title, id, new Float(x), new Float(y), 0,
                externalLink, title, type, linkImage, filename, externalLinkOpenMode);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", display.getExternalLink().getId());
        linkNode.put("displayId", display.getId());
        linkNode.put("x", display.getPositionX());
        linkNode.put("y", display.getPositionY());
        linkNode.put("url", display.getExternalLink().getId());

        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }

}

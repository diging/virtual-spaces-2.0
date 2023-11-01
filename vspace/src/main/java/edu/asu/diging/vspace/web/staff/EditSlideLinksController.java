package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class EditSlideLinksController {
    
    protected ResponseEntity<String> checkIfSlideExists(ISlideManager slideManager, String id, String x, String y) throws IOException{
        ISlide source = slideManager.getSlide(id);
        if (source == null) {
            return new ResponseEntity<>("{'error': 'Slide could not be found.'}", HttpStatus.NOT_FOUND);
        }
        if (x == null || x.trim().isEmpty() || y == null || y.trim().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "No link coordinates specified.");
            return new ResponseEntity<String>(mapper.writeValueAsString(node), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
    
    protected ResponseEntity<String> success(String id, String  displayId, float posX, float posY, int rotation, String url, String label, String displayType, String linkedId, String linkedSlideStatus) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", id);
        linkNode.put("displayId", displayId);
        linkNode.put("x", posX);
        linkNode.put("y", posY);
        linkNode.put("rotation", rotation);
        linkNode.put("label", label);
        linkNode.put("displayType", displayType);
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

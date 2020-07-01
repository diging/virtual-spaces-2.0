package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class EditSpaceLinks {

    public ResponseEntity<String> checkIfSpaceExists(ISpaceManager spaceManager, String id, String x, String y) throws IOException{
        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            return new ResponseEntity<>("{'error': 'Space could not be found.'}", HttpStatus.NOT_FOUND);
        }
        if (x == null || x.trim().isEmpty() || y == null || y.trim().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", "No link coordinates specified.");
            return new ResponseEntity<String>(mapper.writeValueAsString(node), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<String> success(String id, String  displayId, float posX, float posY, int rotation, String url) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode linkNode = mapper.createObjectNode();
        linkNode.put("id", id);
        linkNode.put("displayId", displayId);
        linkNode.put("x", posX);
        linkNode.put("y", posY);
        linkNode.put("rotation", rotation);
        if(url!=null) {
            linkNode.put("url", url);
        }
        return new ResponseEntity<>(mapper.writeValueAsString(linkNode), HttpStatus.OK);
    }
}

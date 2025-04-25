package edu.asu.diging.vspace.web.staff.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/staff/search/api/space")
public class SpaceSearchApiController {
    private static final Logger logger = LoggerFactory.getLogger(SpaceSearchApiController.class);
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> searchSpace(@PathVariable("id") String id) {
        ISpace space = spaceManager.getSpace(id);
        
        if (space == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Space could not be found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("name", space.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
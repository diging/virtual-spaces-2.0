package edu.asu.diging.vspace.web.staff.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/staff/search/api/space")
public class SpaceSearchApiController {

    private static final Logger logger = LoggerFactory.getLogger(SpaceSearchApiController.class);

    @Autowired
    private ISpaceManager spaceManager;

    @GetMapping("/{id}")
    public ResponseEntity<String> searchSpace(@PathVariable("id") String id) {
        logger.info("Received search request for space ID: {}", id);

        ISpace space = spaceManager.getSpace(id);

        if (space == null) {
            return new ResponseEntity<>("{\"error\": \"Space could not be found.\"}", HttpStatus.NOT_FOUND);
        }

        logger.info("Space found: {}", space.getName());

        return new ResponseEntity<>("\"" + space.getName() + "\"", HttpStatus.OK);
    }

}

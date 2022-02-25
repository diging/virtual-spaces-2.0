package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class DeleteVideoBlockController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/video/{blockId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVideoBlock(@PathVariable("blockId") String blockId) throws IOException {
        try {
            contentBlockManager.deleteVideoBlockById(blockId);
        } catch (BlockDoesNotExistException e) {
            logger.warn("Video Id does not exist, bad request.", e);
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}

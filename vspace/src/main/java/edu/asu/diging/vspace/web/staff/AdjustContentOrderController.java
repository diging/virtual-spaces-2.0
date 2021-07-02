package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AdjustContentOrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/adjustContentOrder/{blockId}", method = RequestMethod.POST)
    public ResponseEntity<String> adjustContentOrder(@PathVariable("blockId") String blockId,
            @RequestParam("contentOrder") Integer contentOrder) {

        try {
            contentBlockManager.adjustContentOrder(blockId, contentOrder);
        } catch (BlockDoesNotExistException e) {
            logger.warn("Block Id does not exist, bad request.", e);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

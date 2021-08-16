package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AdjustContentOrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/adjustContentOrder", method = RequestMethod.POST)
    public ResponseEntity<List<ContentBlock>> adjustContentOrder(@RequestBody List<ContentBlock> contentBlockList) {

        try {
            contentBlockManager.adjustContentOrder(contentBlockList);
        } catch (BlockDoesNotExistException e) {
            logger.warn("Block Id does not exist, bad request.", e);
            return new ResponseEntity<List<ContentBlock>>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<ContentBlock>>(contentBlockList,HttpStatus.OK);
    }

}

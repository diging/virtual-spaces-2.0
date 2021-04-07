package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;

public class DeleteSpaceTextBlockController {
    @Autowired
    private ISpaceTextBlockManager spaceTextBlockManager;

    @RequestMapping(value = "/staff/space/{id}/textBlock/{blockId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSpaceTextBlock(@PathVariable("id") String spaceId, @PathVariable("blockId") String blockId) {
        spaceTextBlockManager.deleteTextBlock(blockId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}

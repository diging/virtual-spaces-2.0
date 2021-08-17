package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AddBiblioBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/bibliography", method = RequestMethod.POST)
    public ResponseEntity<BiblioBlock> addBiblioBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestParam("content") String content, @RequestBody BiblioBlock biblioBlockData) throws JsonProcessingException {
        
        Integer contentOrder = contentBlockManager.findMaxContentOrder(slideId);
        contentOrder = contentOrder == null ? 0 : contentOrder + 1;
        
        IBiblioBlock biblioBlock = contentBlockManager.createBiblioBlock(slideId, biblioBlockData, contentOrder);
        return new ResponseEntity<BiblioBlock>((BiblioBlock) biblioBlock, HttpStatus.OK);
    }

}
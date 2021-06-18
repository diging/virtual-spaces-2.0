package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class AddBiblioBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/bibliography", method = RequestMethod.POST)
    public ResponseEntity<String> addTextBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestBody BiblioBlock biblioBlockData) throws IOException {

        IBiblioBlock biblioBlock = contentBlockManager.createBiblioBlock(slideId, biblioBlockData);
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(mapper.writeValueAsString(biblioBlock), HttpStatus.OK);
    }

}
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

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Controller
public class EditBiblioBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/{biblioId}/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editBiblioBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @PathVariable("biblioId") String biblioId,
            @RequestBody BiblioBlock biblioBlockData) throws IOException {
        IBiblioBlock biblioBlock = contentBlockManager.getBiblioBlock(biblioId);
        biblioBlock.setBiblioTitle(biblioBlockData.getBiblioTitle());
        biblioBlock.setDescription(biblioBlockData.getDescription());
        contentBlockManager.updateBiblioBlock((BiblioBlock) biblioBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
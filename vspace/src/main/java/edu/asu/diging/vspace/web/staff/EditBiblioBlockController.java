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

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editTextBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestBody BiblioBlock biblioBlockData) throws IOException {
        IBiblioBlock biblioBlock = contentBlockManager.getBiblioBlock(biblioBlockData.getId());
        biblioBlock.setTitle(biblioBlockData.getTitle());
        biblioBlock.setAuthor(biblioBlockData.getAuthor());
        biblioBlock.setYear(biblioBlockData.getYear());
        biblioBlock.setJournal(biblioBlockData.getJournal());
        biblioBlock.setUrl(biblioBlockData.getUrl());
        biblioBlock.setVolume(biblioBlockData.getVolume());
        biblioBlock.setIssue(biblioBlockData.getIssue());
        biblioBlock.setPages(biblioBlockData.getPages());
        biblioBlock.setEditors(biblioBlockData.getEditors());
        biblioBlock.setType(biblioBlockData.getType());
        biblioBlock.setNote(biblioBlockData.getNote());
        contentBlockManager.updateBiblioBlock((BiblioBlock) biblioBlock);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
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

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Controller
public class EditReferenceController {

    @Autowired
    private IReferenceManager referenceManager;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/reference/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editReference(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @RequestBody Reference ref) throws IOException {
        IReference reference = referenceManager.getReference(ref.getId());
        reference.setTitle(reference.getTitle());
        reference.setAuthor(reference.getAuthor());
        reference.setYear(reference.getYear());
        reference.setJournal(reference.getJournal());
        reference.setUrl(reference.getUrl());
        reference.setVolume(reference.getVolume());
        reference.setIssue(reference.getIssue());
        reference.setPages(reference.getPages());
        reference.setEditors(reference.getEditors());
        reference.setType(reference.getType());
        reference.setNote(reference.getNote());
        referenceManager.updateReference((Reference) reference);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
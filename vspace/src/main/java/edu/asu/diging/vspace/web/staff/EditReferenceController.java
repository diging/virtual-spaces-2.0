package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IReferenceManager;

@Controller
public class EditReferenceController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IReferenceManager referenceManager;
    
    @RequestMapping(value = "/staff/reference/{referenceId}/edit", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("referenceId") String referenceId, RedirectAttributes attributes) {
        IReference reference = referenceManager.getReferenceById(referenceId);
        model.addAttribute("referenceData", reference);
        model.addAttribute("referenceId", referenceId);
        return "staff/references/edit";
    }
    
    @RequestMapping(value = "/staff/reference/{referenceId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute Reference refData, @PathVariable("referenceId") String referenceId, 
            RedirectAttributes attributes) {
        IReference reference = referenceManager.getReferenceById(referenceId);
        refData.setId(reference.getId());
        referenceManager.updateReference(referenceId, refData);
        return "redirect:/staff/display/reference/{referenceId}";
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/{biblioId}/reference/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editReference(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @PathVariable("biblioId") String biblioId, 
            @RequestBody Reference ref) throws IOException {
        IReference reference = referenceManager.getReference(ref.getId());
        reference.setTitle(ref.getTitle());
        reference.setAuthor(ref.getAuthor());
        reference.setYear(reference.getYear());
        reference.setJournal(ref.getJournal());
        reference.setUrl(ref.getUrl());
        reference.setVolume(ref.getVolume());
        reference.setIssue(ref.getIssue());
        reference.setPages(ref.getPages());
        reference.setEditors(ref.getEditors());
        reference.setType(ref.getType());
        reference.setNote(ref.getNote());
        referenceManager.updateReference((Reference) reference);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
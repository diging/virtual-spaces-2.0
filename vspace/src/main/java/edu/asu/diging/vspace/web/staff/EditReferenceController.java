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
import edu.asu.diging.vspace.references.ReferenceBlock;
import edu.asu.diging.vspace.references.ReferenceDisplayDefault;
import edu.asu.diging.vspace.references.ReferenceType;

@Controller
public class EditReferenceController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IReferenceManager referenceManager;
    
    @Autowired
    private ReferenceDisplayDefault referenceDisplayProvider;
    
    @RequestMapping(value = "/staff/reference/{referenceId}/edit", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("referenceId") String referenceId, RedirectAttributes attributes) {
        IReference reference = referenceManager.getReferenceById(referenceId);
        model.addAttribute("referenceData", reference);
        model.addAttribute("referenceTypes", ReferenceType.values());
        return "staff/references/edit";
    }
    
    @RequestMapping(value = "/staff/reference/{referenceId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute Reference refData, @PathVariable("referenceId") String referenceId, 
            RedirectAttributes attributes) {
        IReference reference = referenceManager.getReferenceById(referenceId);
        if(reference!=null) {
            reference.setTitle(refData.getTitle());
            reference.setAuthor(refData.getAuthor());
            reference.setYear(refData.getYear());
            reference.setJournal(refData.getJournal());
            reference.setUrl(refData.getUrl());
            reference.setVolume(refData.getVolume());
            reference.setIssue(refData.getIssue());
            reference.setPages(refData.getPages());
            reference.setEditors(refData.getEditors());
            reference.setType(refData.getType());
            reference.setNote(refData.getNote());
            referenceManager.updateReference(reference);
            return "redirect:/staff/display/reference/{referenceId}";
        }
        return "redirect:/404";
    }
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/biblio/{biblioId}/reference/{refId}/edit", method = RequestMethod.POST)
    public ResponseEntity<ReferenceBlock> editReference(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId, @PathVariable("biblioId") String biblioId, 
            @PathVariable("refId") String refId, @RequestBody Reference ref, RedirectAttributes attributes) throws IOException {
        IReference reference = referenceManager.getReference(refId);
        if(reference!=null) {
            reference.setTitle(ref.getTitle());
            reference.setAuthor(ref.getAuthor());
            reference.setYear(ref.getYear());
            reference.setJournal(ref.getJournal());
            reference.setUrl(ref.getUrl());
            reference.setVolume(ref.getVolume());
            reference.setIssue(ref.getIssue());
            reference.setPages(ref.getPages());
            reference.setEditors(ref.getEditors());
            reference.setType(ref.getType());
            reference.setNote(ref.getNote());            
            referenceManager.updateReference(ref);
            String refDisplayText = referenceDisplayProvider.getReferenceDisplayText((Reference)ref);
            ReferenceBlock refBlock = new ReferenceBlock((Reference) ref, refDisplayText);
            attributes.addAttribute("alertType", "success");
            attributes.addAttribute("message", "Reference successfully updated!");
            attributes.addAttribute("showAlert", "true");
            return new ResponseEntity<ReferenceBlock>(refBlock, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
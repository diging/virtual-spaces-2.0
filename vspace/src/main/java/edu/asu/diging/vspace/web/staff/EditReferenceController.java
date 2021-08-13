package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "staff/references/edit";
    }
    
    @RequestMapping(value = "/staff/reference/{referenceId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute Reference refData, @PathVariable("referenceId") String referenceId, 
            RedirectAttributes attributes) {
        IReference reference = referenceManager.getReferenceById(referenceId);
        if(reference!=null) {
            refData.setId(reference.getId());
            referenceManager.updateReference(refData);
            return "redirect:/staff/display/reference/{referenceId}";
        }
        return "redirect:/404";
    }

}
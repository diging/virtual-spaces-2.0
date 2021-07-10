package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.model.IReference;

@Controller
public class ReferenceController {
    
    @Autowired
    private ReferenceRepository referenceRepo;

    @RequestMapping("/staff/display/reference/{id}")
    public String showBiblio(@PathVariable String id, Model model) {
        IReference reference = referenceRepo.findById(id).get();
        model.addAttribute("reference", reference);
        return "staff/references/reference";
    }
}

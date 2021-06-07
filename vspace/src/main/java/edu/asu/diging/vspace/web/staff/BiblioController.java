package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.BiblioRepository;
import edu.asu.diging.vspace.core.model.IBiblioBlock;

@Controller
public class BiblioController {
    
    @Autowired
    private BiblioRepository biblioRepo;

    @RequestMapping("/staff/display/biblio/{id}")
    public String showBiblio(@PathVariable String id, Model model) {
        IBiblioBlock biblio = biblioRepo.findById(id).get();
        model.addAttribute("biblio", biblio);
        return "staff/biblios/biblio";
    }
}

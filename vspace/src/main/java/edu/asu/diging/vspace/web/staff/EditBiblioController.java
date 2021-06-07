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

import edu.asu.diging.vspace.core.exception.BibliographyDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.services.IBiblioService;

@Controller
public class EditBiblioController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IBiblioService biblioService;

    @RequestMapping(value = "/staff/biblio/{biblioId}/edit", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("biblioId") String biblioId, RedirectAttributes attributes) {
        try {
            IBiblioBlock biblio = biblioService.getBiblioById(biblioId);
            model.addAttribute("biblioData", biblio);
            model.addAttribute("biblioId", biblioId);
        } catch(BibliographyDoesNotExistException biblioDoesNotExistException) {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Bibliography doesnt exist with given biblio id.");
            return "redirect:/staff/biblios/list/";  
        }
        return "staff/biblios/edit";
    }

    @RequestMapping(value = "/staff/biblio/{biblioId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute BiblioBlock biblioData, @PathVariable("biblioId") String biblioId, 
            RedirectAttributes attributes) {
        try {
            biblioService.editBibliography(biblioId, biblioData);
        } catch (BibliographyDoesNotExistException biblioDoesNotExistException) {
            logger.error("Edit Bibliography Failed" + biblioDoesNotExistException);
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Edit Bibliography Failed. Please try again");
            return "redirect:/staff/biblios/list/1";
        }
        return "redirect:/staff/display/biblio/{biblioId}";
    }
}

package edu.asu.diging.vspace.web.staff;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ILanguageService;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.web.staff.dto.SlideDTO;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Controller
public class SequenceController {

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ILanguageService languageService;

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{id}/slides", method = RequestMethod.GET)
    public ResponseEntity<List<SlideDTO>> getSequenceSlides(Model model, @PathVariable("moduleId") String moduleId,
            @PathVariable("id") String sequenceId, @ModelAttribute SequenceForm sequenceForm, Principal principal) {

        List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
        String defaultLanguageCode = languageService.getDefaultLanguageCode();
        
        List<SlideDTO> slideDTOs = slides.stream()
            .map(slide -> {
                String localizedName = slide.getLocalizedName(defaultLanguageCode, defaultLanguageCode);
                String localizedDescription = slide.getLocalizedDescription(defaultLanguageCode, defaultLanguageCode);
                return new SlideDTO(
                    slide.getId(),
                    localizedName != null && !localizedName.isEmpty() ? localizedName : slide.getName(),
                    localizedDescription != null && !localizedDescription.isEmpty() ? localizedDescription : slide.getDescription()
                );
            })
            .collect(java.util.stream.Collectors.toList());
            
        return new ResponseEntity<List<SlideDTO>>(slideDTOs, HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{id}", method = RequestMethod.GET)
    public String getSequence(Model model, @PathVariable("moduleId") String moduleId,
            @PathVariable("id") String sequenceId, @ModelAttribute SequenceForm sequenceForm, Principal principal) {

        model.addAttribute("module", moduleManager.getModule(moduleId));
        model.addAttribute("sequence", sequenceManager.getSequence(sequenceId));
        model.addAttribute("selectedSlides", sequenceManager.getSequence(sequenceId).getSlides());
        model.addAttribute("allSlides", moduleManager.getModuleSlides(moduleId));
        model.addAttribute("selectedLanguage", languageService.getDefaultLanguageCode());
        model.addAttribute("defaultLanguageCode", languageService.getDefaultLanguageCode());
        
        return "staff/modules/sequences/sequence";
    }
}
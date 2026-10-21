package edu.asu.diging.vspace.web.staff;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.factory.ISlideFormFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class EditSlideController {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private ISlideFormFactory slideFormFactory;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/edit/description", method = RequestMethod.POST)
    public ResponseEntity<String> saveDescription(@RequestParam("description") String description,
            @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        slide.setDescription(description);
        slideManager.updateSlide((Slide) slide);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/edit/title", method = RequestMethod.POST)
    public ResponseEntity<String> saveTitle(@RequestParam("title") String title,
            @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        slide.setName(title);
        slideManager.updateSlide((Slide) slide);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value="/staff/module/{moduleId}/slide/{slideId}/edit", method=RequestMethod.GET)
    public String show(Model model, @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        SlideForm slideForm = slideFormFactory.createNewSlideForm(slide, exhibitionManager.getStartExhibition());  
        if(slide instanceof BranchingPoint) {
            slideForm.setType(SlideType.BRANCHING_POINT.toString());
            IBranchingPoint branchingPoint = (IBranchingPoint) slide;           
            List<String> choices = branchingPoint.getChoices().stream().map(choice -> choice.getSequence().getId()).collect(Collectors.toList());
            slideForm.setChoices(choices);
            model.addAttribute("choices", choices);
        }
        else {
            slideForm.setType(SlideType.SLIDE.toString());
        }
        model.addAttribute("slideForm", slideForm);
        model.addAttribute("slideId", slideId);
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("sequences", moduleManager.getModuleSequences(moduleId));
        return "staff/modules/slides/edit";
    }

    @RequestMapping(value="/staff/module/{moduleId}/slide/{slideId}/edit", method=RequestMethod.POST)
    public String save(@ModelAttribute SlideForm slideForm, @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        SlideType type = slideForm.getType().isEmpty() ? null : SlideType.valueOf(slideForm.getType());
        
        if(type.equals(SlideType.BRANCHING_POINT)) {
            List<String> editedChoices = slideForm.getChoices();
            slideManager.updateBranchingPoint((IBranchingPoint)slide, editedChoices);
        }
        slideManager.updateNameAndDescription(slide, slideForm);
        slideManager.updateSlide((Slide)slide);
        return "redirect:/staff/module/{moduleId}/slide/{slideId}";
    }
}

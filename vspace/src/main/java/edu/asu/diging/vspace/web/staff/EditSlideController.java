package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.impl.ModuleManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class EditSlideController {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ModuleManager moduleManager;

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
        SlideForm slideForm = new SlideForm();
        slideForm.setName(slide.getName());
        slideForm.setDescription(slide.getDescription());
        if(slide instanceof BranchingPoint) {
            slideForm.setType(SlideType.BRANCHING_POINT.toString());
            IBranchingPoint bp = (IBranchingPoint) slide;
            List<String> choices = new ArrayList<String>();
            for(IChoice choice : bp.getChoices()) {
                choices.add(choice.getSequence().getId());
            }
            slideForm.setChoices(choices);
        }
        else {
            slideForm.setType(SlideType.SLIDE.toString());
        }
        model.addAttribute("slideForm", slideForm);
        model.addAttribute("slideId", slideId);
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("sequences", moduleManager.getModuleSequences(moduleId));
        return "staff/module/slide/edit";
    }

    @RequestMapping(value="/staff/module/{moduleId}/slide/{slideId}/edit", method=RequestMethod.POST)
    public String save(@ModelAttribute SlideForm slideForm, @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        slide.setName(slideForm.getName());
        slide.setDescription(slideForm.getDescription());

        slideManager.updateSlide((Slide)slide);
        return "redirect:/staff/module/{moduleId}/slide/{slideId}";
    }
}

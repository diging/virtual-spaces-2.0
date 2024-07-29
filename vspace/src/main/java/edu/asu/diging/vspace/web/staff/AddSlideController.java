
package edu.asu.diging.vspace.web.staff;

import java.security.Principal;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.factory.ISlideFormFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class AddSlideController {
    
    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISlideFormFactory slideFormFactory;


    @RequestMapping(value = "/staff/module/{id}/slide/add", method = RequestMethod.GET)
    public String showAddSlide(@PathVariable("id") String moduleId, Model model) {
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("slide", slideFormFactory.createNewSlideForm(new Slide(), exhibitionManager.getStartExhibition()));
        model.addAttribute("sequences", moduleManager.getModuleSequences(moduleId));
        
        return "staff/modules/slides/add";
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/add", method = RequestMethod.POST)
    public String addSlide(Model model, @PathVariable("moduleId") String moduleId, @ModelAttribute SlideForm slideForm,
            Principal principal) {

        IModule module = moduleManager.getModule(moduleId);
        SlideType type = slideForm.getType().isEmpty() ? null : SlideType.valueOf(slideForm.getType());
        String slideId;
        if(type.equals(SlideType.BRANCHING_POINT)) {
            IBranchingPoint branchingPoint = slideManager.createBranchingPoint(module, slideForm, type);
            slideId = branchingPoint.getId();
        } 
        else {
            
            ISlide slide = slideManager.createSlide(module, slideForm, type);
            slideId = slide.getId();
            slideManager.updateNameAndDescription(slide,slideForm);
        }
        
        return "redirect:/staff/module/{moduleId}/slide/" + slideId;
    }
}
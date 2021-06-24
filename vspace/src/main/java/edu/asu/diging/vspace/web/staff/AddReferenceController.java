package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Reference;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IReferenceManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class AddReferenceController {

    @Autowired
    private IReferenceManager referenceManager;

    @Autowired
    private IContentBlockManager contentBlockManager;

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/reference/add", method = RequestMethod.GET)
    public String showAddSlide(@PathVariable("id") String moduleId, @PathVariable("slideId") String slideId, 
            @RequestBody Reference reference, Model model) {
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("slide", new SlideForm());

        return "staff/modules/slides/add";
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/add", method = RequestMethod.POST)
    public String addSlide(Model model, @PathVariable("moduleId") String moduleId, @ModelAttribute SlideForm slideForm,
            Principal principal) {

        IModule module = moduleManager.getModule(moduleId);
        SlideType type = slideForm.getType().isEmpty() ? null : SlideType.valueOf(slideForm.getType());
        if(type.equals(SlideType.BRANCHING_POINT)) {
            IBranchingPoint branchingPoint = slideManager.createBranchingPoint(module, slideForm, type);
            IChoiceBlock choiceBlock = contentBlockManager.createChoiceBlock(branchingPoint.getId(), null, 0, true);
        } else {
            slideManager.createSlide(module, slideForm, type);
        }

        return "redirect:/staff/module/{moduleId}";
    }

}
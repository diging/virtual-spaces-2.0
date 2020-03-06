package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;

@Controller
public class ExhibitionSlideController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private SlideManager sildeManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}", method = RequestMethod.GET)
    public String slide(Model model, @PathVariable("slideId") String slideId,@PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
        IModule module = moduleManager.getModule(moduleId);
        model.addAttribute("module", module);
        model.addAttribute("startSequenceId", module.getStartSequence().getId());
        model.addAttribute("firstSlide", module.getStartSequence().getSlides().get(0).getId());
        model.addAttribute("slides", sequenceManager.getSequence(sequenceId).getSlides());
        model.addAttribute("currentSequenceId", sequenceId);
        model.addAttribute("currentSlide", slideId);
        model.addAttribute("currentSlideCon", sildeManager.getSlide(slideId));
        model.addAttribute("startSequenceId", module.getStartSequence().getId());
        return "module";

    }
}

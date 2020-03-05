package edu.asu.diging.vspace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;
import edu.asu.diging.vspace.core.services.impl.SequenceManager;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ContentBlockManager contentBlockManager;

    @Autowired
    private SequenceManager sequenceManager;

    @RequestMapping(value = "/exhibit/module/{id}")
    public String module(@PathVariable("id") String id, Model model) {
        IModule module = moduleManager.getModule(id);
        model.addAttribute("module", module);
        if (module.getStartSequence() == null) {
            return "module";
        } else {
            model.addAttribute("startSequenceId", module.getStartSequence().getId());
            model.addAttribute("slides", module.getStartSequence().getSlides());
            model.addAttribute("slideContents", contentBlockManager.getAllContentBlocks(module.getStartSequence().getSlides().get(0).getId()));
            return "module";
        }
    }
    @RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}/content", method = RequestMethod.GET)
    public String slide(Model model, @PathVariable("slideId") String slideId,@PathVariable("moduleId") String moduleId) {
        IModule module = moduleManager.getModule(moduleId);
        model.addAttribute("module", module);
        model.addAttribute("startSequenceId", module.getStartSequence().getId());
        model.addAttribute("firstSlide", module.getStartSequence().getSlides().get(0).getId());
        model.addAttribute("slides", module.getStartSequence().getSlides());
        model.addAttribute("currentSlide", slideId);
        List<IContentBlock> slideContents = contentBlockManager.getAllContentBlocks(slideId);
        model.addAttribute("slideContents", slideContents);
        return "module";

    }
    @RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{id}/slides", method = RequestMethod.GET)
    public ResponseEntity<List<ISlide>> getSequenceSlides(Model model, @PathVariable("moduleId") String moduleId,
            @PathVariable("id") String sequenceId) {
        List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
        return new ResponseEntity<List<ISlide>>(slides, HttpStatus.OK);
    }
}

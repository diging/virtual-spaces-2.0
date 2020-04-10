package edu.asu.diging.vspace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.web.exception.SlideNotFoundException;
import edu.asu.diging.vspace.web.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

@Controller
public class ExhibitionSlideController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private SlideManager sildeManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/exhibit/{spaceId}/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}", method = RequestMethod.GET)
    public String slide(Model model, @PathVariable("slideId") String slideId, @PathVariable("moduleId") String moduleId,
            @PathVariable("sequenceId") String sequenceId, @PathVariable("spaceId") String spaceId)
            throws ModuleNotFoundException, SequenceNotFoundException,
            SlidesInSequenceNotFoundException, SlideNotFoundException, SpaceDoesNotExistException,
            SpaceNotFoundException {

        ISpace space = spaceManager.getSpace(spaceId);
        if (space == null) {
            throw new SpaceNotFoundException(spaceId);
        }
        IModule module = moduleManager.getModule(moduleId);
        model.addAttribute("module", module);
        if (module == null) {
            throw new ModuleNotFoundException(moduleId);
        }
        if (module.getStartSequence() == null) {
            model.addAttribute("error","Sorry, module has not been configured yet.");
            return "module";
        }
        String startSequenceId = module.getStartSequence().getId();
        model.addAttribute("startSequenceId", startSequenceId);

//        List<ISequence> sequences = moduleManager.getModuleSequences(moduleId);
//        boolean sequenceExist = sequences.stream().anyMatch(sequence -> sequence.getId().equals(sequenceId));
        ISequence sequenceExist=moduleManager.checkIfSequenceExists(moduleId, sequenceId);
        if (sequenceExist==null) {
            throw new SequenceNotFoundException(sequenceId);
        }
        List<ISlide> sequenceSlides = sequenceManager.getSequence(sequenceId).getSlides();

        boolean slideExist = sequenceSlides.stream().anyMatch(slide -> slide.getId().equals(slideId));
        if (!slideExist) {
            throw new SlideNotFoundException(slideId);
        }

        if (sequenceSlides.size() == 0) {
            throw new SlidesInSequenceNotFoundException();
        }
        model.addAttribute("firstSlide", module.getStartSequence().getSlides().get(0).getId());
        String nextSlideId = "";
        String prevSlideId = "";
        int slideIndex = 0;
        slideIndex = sequenceSlides.indexOf(sildeManager.getSlide(slideId));
        int slideSize = sequenceSlides.size();
        if (slideSize > slideIndex + 1) {
            nextSlideId = sequenceSlides.get(slideIndex + 1).getId();
        }
        if (slideIndex > 0) {
            prevSlideId = sequenceSlides.get(slideIndex - 1).getId();
        }
//        if (slideIndex == slideSize - 1) {
//            nextSlideId = sequenceSlides.get(0).getId();
//        }
//        if (slideIndex == 0) {
//            prevSlideId = sequenceSlides.get(sequenceSlides.size() - 1).getId();
//        }
//        if (slideIndex < slideSize - 1) {
//            nextSlideId = sequenceSlides.get(slideIndex + 1).getId();
//        }
//        if (slideIndex > 0) {
//            prevSlideId = sequenceSlides.get(slideIndex - 1).getId();
//        }
        model.addAttribute("slides", sequenceSlides);
        model.addAttribute("currentSequenceId", sequenceId);
        model.addAttribute("nextSlide", nextSlideId);
        model.addAttribute("prevSlide", prevSlideId);
        model.addAttribute("currentSlideCon", sildeManager.getSlide(slideId));
        model.addAttribute("numOfSlides", sequenceSlides.size());
        model.addAttribute("currentNumOfSlide", slideIndex + 1);
        model.addAttribute("spaceId", spaceId);
        model.addAttribute("spaceName", spaceManager.getSpace(spaceId).getName());
        return "module";
    }
}

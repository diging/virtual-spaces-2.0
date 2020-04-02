package edu.asu.diging.vspace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.exception.ModuleNotConfiguredException;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.web.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

@Controller
public class ExhibitionSequencesController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value = "/exhibit/{spaceId}/module/{moduleId}/sequence/{sequenceId}")
    public String sequence(Model model, @PathVariable("sequenceId") String sequenceId,
            @PathVariable("moduleId") String moduleId, @PathVariable("spaceId") String spaceId)
            throws ModuleNotFoundException, SequenceNotFoundException, SlidesInSequenceNotFoundException,
            ModuleNotConfiguredException, SpaceNotFoundException {
        ISpace space = spaceManager.getSpace(spaceId);
        if (space == null) {
            throw new SpaceNotFoundException(spaceId);
        }
        IModule module = moduleManager.getModule(moduleId);
        if (module == null) {
            throw new ModuleNotFoundException(moduleId);
        }
        model.addAttribute("module", module);
        if (module.getStartSequence() == null) {
            throw new ModuleNotConfiguredException(moduleId);
        }
        List<ISequence> sequences = moduleManager.getModuleSequences(moduleId);
        boolean sequenceExist = sequences.stream().anyMatch(sequence -> sequence.getId().equals(sequenceId));
        if (!sequenceExist) {
            throw new SequenceNotFoundException(sequenceId);
        }
        List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
        if (slides.size() == 0) {
            throw new SlidesInSequenceNotFoundException();
        }
        String firstSlideId = slides.get(0).getId();
        return "redirect:/exhibit/{spaceId}/module/" + moduleId + "/sequence/" + sequenceId + "/slide/" + firstSlideId;
    }
}

package edu.asu.diging.vspace.web.publicview;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
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

    @Autowired
    private SequenceHistory sequenceHistory;

    @RequestMapping(value = { 
        "/exhibit/{spaceId}/module/{moduleId}/sequence/{sequenceId}",
        "/preview/{previewId}/{spaceId}/module/{moduleId}/sequence/{sequenceId}" 
    })
    public String sequence(Model model, @PathVariable("sequenceId") String sequenceId,
            @PathVariable("moduleId") String moduleId, @PathVariable("spaceId") String spaceId,
            @PathVariable(name = "previewId", required = false) String previewId,
            @RequestParam(required = false, name = "branchingPoint") String branchingPointId,
            @RequestParam(required = false, name = "previousSequenceId") String previousSequenceId,
            @RequestParam(required = false, name = "clearHistory") Boolean clearHistory) throws ModuleNotFoundException,
            SequenceNotFoundException, SlidesInSequenceNotFoundException, SpaceNotFoundException {

        ISpace space = spaceManager.getSpace(spaceId);
        if (space == null) {
            return "redirect:/exhibit/404";
        }
        IModule module = moduleManager.getModule(moduleId);
        if (module == null) {
            return "redirect:/exhibit/404";
        }
        model.addAttribute("module", module);
        if (module.getStartSequence() == null) {
            model.addAttribute("showAlert", true);
            model.addAttribute("message", "Sorry, module has not been configured yet.");
            if (previewId != null) {
                model.addAttribute("isExhPreview", true);
                model.addAttribute("previewId", previewId);
            }
            return "/exhibition/module";
        }
        ISequence sequenceExist = moduleManager.checkIfSequenceExists(moduleId, sequenceId);
        if (sequenceExist == null) {
            throw new SequenceNotFoundException(sequenceId);
        }

        List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
        if (slides.size() == 0) {
            model.addAttribute("showAlert", true);
            model.addAttribute("message", "Sorry, module has not been configured yet.");
            if (previewId != null) {
                model.addAttribute("isExhPreview", true);
                model.addAttribute("previewId", previewId);
            }
            return "/exhibition/module";
        }
        String firstSlideId = slides.get(0).getId();
        if (sequenceHistory.hasHistory() && (clearHistory != null && clearHistory == true)) {
            sequenceHistory.flushFromHistory();
        }
        if (previewId != null) {
            return String.format(
                    "redirect:/preview/%s/%s/module/%s/sequence/%s/slide/%s?branchingPoint=%s&previousSequenceId=%s",
                    previewId, spaceId, moduleId, sequenceId, firstSlideId,
                    (branchingPointId != null ? branchingPointId : ""),
                    (previousSequenceId != null ? previousSequenceId : ""));
        }
        return String.format(
                "redirect:/exhibit/%s/module/%s/sequence/%s/slide/%s?branchingPoint=%s&previousSequenceId=%s",
                spaceId, moduleId, sequenceId, firstSlideId, (branchingPointId != null ? branchingPointId : ""),
                (previousSequenceId != null ? previousSequenceId : ""));

    }
}

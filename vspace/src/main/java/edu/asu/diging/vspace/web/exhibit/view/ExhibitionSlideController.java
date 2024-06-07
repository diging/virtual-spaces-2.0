package edu.asu.diging.vspace.web.exhibit.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlideNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceNotFoundException;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ExhibitionSlideController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private SequenceHistory sequenceHistory;
    
    @Autowired
    private IExhibitionManager exhibitManager;
    
    @Autowired
    private ISlideExternalLinkManager slideExternalLinkManager;

    @RequestMapping(value = {
        "/exhibit/{spaceId}/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}",
        "/preview/{"+ExhibitionConstants.PREVIEW_ID+"}/{spaceId}/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}"
        }, method = RequestMethod.GET)
    public String slide(Model model, @PathVariable("slideId") String slideId, @PathVariable("moduleId") String moduleId,
            @PathVariable("sequenceId") String sequenceId, @PathVariable("spaceId") String spaceId,
            @PathVariable(name = ExhibitionConstants.PREVIEW_ID, required = false) String previewId,
            @RequestParam(required = false, name = "back") boolean back,
            @RequestParam(required = false, name = "branchingPoint") String branchingPointId,
            @RequestParam(required = false, name = "previousSequenceId") String previousSequenceId)
            throws ModuleNotFoundException, SequenceNotFoundException, SlidesInSequenceNotFoundException,
            SlideNotFoundException, SpaceDoesNotExistException, SpaceNotFoundException {

        ISpace space = spaceManager.getSpace(spaceId);
        if (space == null) {
            return "redirect:/exhibit/404";
        }
        IModule module = moduleManager.getModule(moduleId);
        model.addAttribute("module", module);
        if (module == null) {
            return "redirect:/exhibit/404";
        }
        if (module.getStartSequence() == null) {
            return moduleNotConfigured(model);
        }
        String startSequenceId = module.getStartSequence().getId();
        model.addAttribute("startSequenceId", startSequenceId);
        IExhibition exhibition = exhibitManager.getStartExhibition();
        model.addAttribute("exhibitionConfig", exhibition);
        ISequence sequenceExist = moduleManager.checkIfSequenceExists(moduleId, sequenceId);
        if (sequenceExist == null) {
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

        ISlide currentSlide = slideManager.getSlide(slideId);
        int slideIndex = sequenceSlides.indexOf(currentSlide);

        int slideSize = sequenceSlides.size();
        if (slideSize > slideIndex + 1) {
            nextSlideId = sequenceSlides.get(slideIndex + 1).getId();
        }
        if (slideIndex > 0) {
            prevSlideId = sequenceSlides.get(slideIndex - 1).getId();
        }
        model.addAttribute("sequences", moduleManager.getModuleSequences(moduleId));
        model.addAttribute("sequence", sequenceExist);
        model.addAttribute("slides", sequenceSlides);
        model.addAttribute("currentSequenceId", sequenceId);
        model.addAttribute("nextSlide", nextSlideId);
        model.addAttribute("prevSlide", prevSlideId);

        model.addAttribute("currentSlideCon", currentSlide);
        if (currentSlide instanceof BranchingPoint) {
            handleBranchingPoint(model, slideId, back, currentSlide);
        }
        if (branchingPointId != null && !branchingPointId.isEmpty()) {
            sequenceHistory.addToHistory(previousSequenceId, branchingPointId);
        }
        if (sequenceHistory.hasHistory()) {
            model.addAttribute("showBackToPreviousChoice", true);
            model.addAttribute("previousSequenceId", sequenceHistory.peekSequenceId());
            model.addAttribute("previousBranchingPoint",
                    ((BranchingPoint) slideManager.getSlide(sequenceHistory.peekBranchingPointId())));
        }

        model.addAttribute("numOfSlides", sequenceSlides.size());
        model.addAttribute("currentNumOfSlide", slideIndex + 1);
        model.addAttribute("spaceId", spaceId);
        model.addAttribute("spaceName", spaceManager.getSpace(spaceId).getName());
        model.addAttribute("slideExternalLinkList", slideExternalLinkManager.getLinkDisplays(slideId));
        return "exhibition/module";
    }

    private void handleBranchingPoint(Model model, String slideId, boolean back, ISlide currentSlide) {
        model.addAttribute("choices", ((BranchingPoint) currentSlide).getChoices());
        if (back && sequenceHistory.peekBranchingPointId().equalsIgnoreCase(slideId)) {
            sequenceHistory.popFromHistory();
        }
    }

    private String moduleNotConfigured(Model model) {
        model.addAttribute("showAlert", true);
        model.addAttribute("message", "Sorry, module has not been configured yet.");
        return "/exhibition/module";
    }
}

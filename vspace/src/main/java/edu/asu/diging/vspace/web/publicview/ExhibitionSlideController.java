package edu.asu.diging.vspace.web.publicview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.ChoicesHistory;
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
    
    @Autowired
    private ChoicesHistory choiceHistory;

    @RequestMapping(value = "/exhibit/{spaceId}/module/{moduleId}/sequence/{sequenceId}/slide/{slideId}", method = RequestMethod.GET)
    public String slide(Model model, HttpServletRequest request, SessionStatus sessionStatus,@PathVariable("slideId") String slideId, @PathVariable("moduleId") String moduleId,
            @PathVariable("sequenceId") String sequenceId, @PathVariable("spaceId") String spaceId, @RequestParam("back") boolean back, @RequestParam("choice") boolean choice)
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
            model.addAttribute("showAlert", true);
            model.addAttribute("message", "Sorry, module has not been configured yet.");
            return "module";
        }
        String startSequenceId = module.getStartSequence().getId();
        model.addAttribute("startSequenceId", startSequenceId);
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
        ISlide currentSlide = sildeManager.getSlide(slideId);
        int slideIndex = sequenceSlides.indexOf(currentSlide);
        int slideSize = sequenceSlides.size();
        if (slideSize > slideIndex + 1) {
            nextSlideId = sequenceSlides.get(slideIndex + 1).getId();
        }
        if (slideIndex > 0) {
            prevSlideId = sequenceSlides.get(slideIndex - 1).getId();
        }
        model.addAttribute("sequences",moduleManager.getModuleSequences(moduleId));
        model.addAttribute("sequence",sequenceExist);
        model.addAttribute("slides", sequenceSlides);
        model.addAttribute("currentSequenceId", sequenceId);
        model.addAttribute("nextSlide", nextSlideId);
        model.addAttribute("prevSlide", prevSlideId);
        model.addAttribute("currentSlideCon", currentSlide);
        
        if(currentSlide instanceof BranchingPoint) {
            model.addAttribute("choices", ((BranchingPoint)currentSlide).getChoices());
            if(choiceHistory.getFromSequenceSlideHistory().size()==0) {
                choiceHistory.addToSequenceSlideHistory(sequenceId, slideId);
            }
        }
        if(choice){
            String lastElement = choiceHistory.removeLastElementFromSequenceSlideHistory();
            if(lastElement.equals(sequenceId+","+slideId)){
                choiceHistory.addToSequenceSlideHistory(lastElement);
            }else {
                choiceHistory.addToSequenceSlideHistory(lastElement);
                choiceHistory.addToSequenceSlideHistory(sequenceId+","+slideId);
            }
        }
        if(choiceHistory.getFromSequenceSlideHistory().size()>=2) {
            model.addAttribute("showBackToPreviousChoice", true);
            String lastElement = choiceHistory.removeLastElementFromSequenceSlideHistory();
            String seondLastElement = choiceHistory.removeLastElementFromSequenceSlideHistory();
            String[] elementValues = seondLastElement.split(",");
            model.addAttribute("previousChoiceSequence", elementValues[0]);
            model.addAttribute("previousChoiceSlide", elementValues[1]);
            choiceHistory.addToSequenceSlideHistory(seondLastElement);
            choiceHistory.addToSequenceSlideHistory(lastElement);
        }

        if(back) {
            choiceHistory.removeLastElementFromSequenceSlideHistory();
        }
        model.addAttribute("numOfSlides", sequenceSlides.size());
        model.addAttribute("currentNumOfSlide", slideIndex + 1);
        model.addAttribute("spaceId", spaceId);
        model.addAttribute("spaceName", spaceManager.getSpace(spaceId).getName());
        return "module";
    }
}

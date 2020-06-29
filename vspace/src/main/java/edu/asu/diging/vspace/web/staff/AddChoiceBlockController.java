package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.data.ChoiceRepository;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class AddChoiceBlockController {

    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private ISequenceManager sequenceManager;
    
    @Autowired
    private ChoiceRepository choiceRepo;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/choice/content", method = RequestMethod.POST)
    public ResponseEntity<IChoiceBlock> addChoiceBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId,
            @RequestParam("contentOrder") Integer contentOrder, @RequestParam("selectedChoices") List<String> selectedChoice, @RequestParam("showsAll") boolean showsAll) throws IOException {

        IBranchingPoint branchingPoint = (IBranchingPoint) slideManager.getSlide(slideId);
        List<IChoice> choices = null;
        if(showsAll) {
            choices = branchingPoint.getChoices();
        }else {
            for(String choiceID : selectedChoice) {
                Optional<Choice> choice = choiceRepo.findById(choiceID);
                choices.add(choice.get());
            }
        }
        IChoiceBlock choiceBlock = contentBlockManager.createChoiceBlock(slideId, choices, contentOrder, showsAll);

        return new ResponseEntity<>(choiceBlock, HttpStatus.OK);
    }
}

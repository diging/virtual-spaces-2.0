package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ChoiceContentBlockRepository;
import edu.asu.diging.vspace.core.factory.IChoiceBlockFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;

@Service
public class ChoiceBlockManager extends AbstractContentBlockManager<IChoiceBlock, ChoiceContentBlockRepository> {

    @Autowired
    private IChoiceBlockFactory choiceBlockFactory;
    
    @Autowired
    private ChoiceContentBlockRepository choiceBlockRepo;

    @Override
    protected ChoiceContentBlockRepository getRepository() {
        return choiceBlockRepo;
    }

    /**
     * Creates a new choice block for the specified slide.
     * 
     * @param slideId The ID of the slide
     * @param selectedChoices List of selected choice IDs
     * @param showsAll Whether to show all choices or only selected ones
     * @return The created choice block
     */
    public IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, boolean showsAll) {
        Integer contentOrder = getNextContentOrder(slideId);
        
        List<IChoice> choices = new ArrayList<IChoice>();
        if (!showsAll) {
            choices = selectedChoices.stream()
                    .map(choice -> slideManager.getChoice(choice))
                    .collect(Collectors.toList());
        }
        
        IChoiceBlock choiceBlock = choiceBlockFactory.createChoiceBlock(
            slideManager.getSlide(slideId), 
            contentOrder,
            choices, 
            showsAll
        );
        return choiceBlockRepo.save((ChoiceBlock) choiceBlock);
    }
}

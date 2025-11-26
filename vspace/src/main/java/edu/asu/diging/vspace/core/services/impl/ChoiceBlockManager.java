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
import edu.asu.diging.vspace.core.services.IChoiceBlockManager;

@Service
public class ChoiceBlockManager extends GenericContentBlockManager<IChoiceBlock, ChoiceContentBlockRepository> 
        implements IChoiceBlockManager {

    @Autowired
    private IChoiceBlockFactory choiceBlockFactory;
    
    @Autowired
    private ChoiceContentBlockRepository choiceBlockRepo;

    @Override
    protected ChoiceContentBlockRepository getRepository() {
        return choiceBlockRepo;
    }

    @Override
    public IChoiceBlock createContentBlock(String slideId) {
        return createChoiceBlock(slideId, new ArrayList<>(), true);
    }

    @Override
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
            choices,
            showsAll
        );
        choiceBlock.setContentOrder(contentOrder);
        return choiceBlockRepo.save((ChoiceBlock) choiceBlock);
    }

    @Override
    public void updateContentBlock(IChoiceBlock choiceBlock) {
        choiceBlockRepo.save((ChoiceBlock) choiceBlock);
    }

    @Override
    public void saveContentBlock(IChoiceBlock choiceBlock) {
        choiceBlockRepo.save((ChoiceBlock) choiceBlock);
    }
}

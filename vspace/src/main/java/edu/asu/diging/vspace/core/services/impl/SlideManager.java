package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.BranchingPointRepository;
import edu.asu.diging.vspace.core.data.ChoiceRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.impl.ChoiceFactory;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;


@Service
public class SlideManager implements ISlideManager {

    @Autowired
    private SlideFactory slideFactory;

    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Autowired
    private BranchingPointRepository bpointRepo;

    @Autowired
    private ChoiceRepository choiceRepo;

    @Autowired
    private ChoiceFactory choiceFactory;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ISlide createSlide(IModule module, SlideForm slideForm, SlideType type) {
        ISlide slide = slideFactory.createSlide(module, slideForm, type);     
        return slideRepo.save((Slide) slide);
    }

    @Override
    public IBranchingPoint createBranchingPoint(IModule module, SlideForm slideForm, SlideType type) {
        ISlide branchingPoint = slideFactory.createSlide(module, slideForm, type);                 
        return bpointRepo.save((BranchingPoint) branchingPoint);
    }

    @Override
    public void updateBranchingPoint(IBranchingPoint branchingPoint, List<String> editedChoices) {
        List<IChoice> existingChoices=branchingPoint.getChoices();
        List<String> existingChoiceSequences=existingChoices.stream().map(choiceSequence -> choiceSequence.getSequence().getId()).collect(Collectors.toList());
        List<String> deletedChoices = (List<String>) CollectionUtils.subtract(existingChoiceSequences, editedChoices);
        List<String> addedChoices = (List<String>) CollectionUtils.subtract(editedChoices,existingChoiceSequences);
        List<IChoice> newlyAddedChoices = choiceFactory.createChoices(addedChoices);
        for(IChoice addedChoice : newlyAddedChoices) {
            existingChoices.add(addedChoice);
        }
        List<IChoice> choicesToDelete = new ArrayList<IChoice>();
        choicesToDelete = existingChoices.stream().filter(choice -> deletedChoices.contains(choice.getSequence().getId())).collect(Collectors.toList());
        for(String deletedChoice : deletedChoices) {
            existingChoices.removeIf(choice -> choice.getSequence().getId().equals(deletedChoice));
        }
        branchingPoint.setChoices(existingChoices);
        bpointRepo.save((BranchingPoint) branchingPoint);
        for(IChoice choice:choicesToDelete) {
            choiceRepo.deleteById(choice.getId());
        }
    }

    @Override
    public ISlide getSlide(String slideId) {
        Optional<Slide> slide = slideRepo.findById(slideId);
        if (slide.isPresent()) {
            return slide.get();
        }
        return null;
    }

    @Override
    public IChoice getChoice(String choiceId) {
        Optional<Choice> choice = choiceRepo.findById(choiceId);
        if (choice.isPresent()) {
            return choice.get();
        }
        return null;
    }

    @Override
    public void updateSlide(Slide slide) {
        slideRepo.save((Slide) slide);
    }

    @Override
    public void deleteSlideById(String slideId, String moduleId) {

        if(slideId == null) {
            logger.error("Slide Id cannot be null.");
            return;
        }

        List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
        Slide slideObj = (Slide) getSlide(slideId);
        List<ISlide> slideObjToRemove = new ArrayList<>();
        slideObjToRemove.add(slideObj);
        if(slideObj == null) {
            return;
        }
        for (Sequence sequence : sequences) {
            if(sequence.getSlides().contains(slideObj)) {
                sequence.getSlides().removeAll(slideObjToRemove);
                sequenceRepo.save(sequence);
            }
        }
        try {

            slideRepo.delete((Slide) getSlide(slideId));

        } catch (IllegalArgumentException exception) {
            logger.error("Unable to delete slide.", exception);
        }
    }

    @Override
    public List<Sequence> getSlideSequences(String slideId, String moduleId) {

        List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
        List<Sequence> sequenceSlides = new ArrayList<>();
        for (Sequence sequence : sequences) {
            Iterator<ISlide> slideIterator = sequence.getSlides().iterator();
            while (slideIterator.hasNext()) {
                if (slideIterator.next().getId().equals(slideId)) {
                    sequenceSlides.add(sequence);
                }
            }
        }
        return sequenceSlides;
    }
}

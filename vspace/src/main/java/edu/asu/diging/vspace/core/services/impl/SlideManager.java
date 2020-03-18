package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideManager implements ISlideManager {

    @Autowired  
    private ModuleManager moduleManager;

    @Autowired
    private SlideFactory slideFactory;

    @Autowired
    private SlideRepository slideRepo;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Override
    public ISlide createSlide(String moduleId, SlideForm slideForm) {
        IModule module = moduleManager.getModule(moduleId);
        ISlide slide = slideFactory.createSlide(module, slideForm);
        slideRepo.save((Slide) slide);
        return slide;
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
    public void updateSlide(Slide slide) {
        slideRepo.save((Slide) slide);
    }

    @Override
    public void deleteSlideById(String slideId, String moduleId) throws SlideDoesNotExistException {
    	
        List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
        Slide slideObj = (Slide) getSlide(slideId);
        for(Sequence sequence : sequences) {
			for(int i = 0; i<sequence.getSlides().size(); i++) {
				String slideIdToCompare = sequence.getSlides().get(i).getId();
				if(slideIdToCompare.equals(slideId)) {
					sequence.getSlides().remove(slideObj);
					sequenceRepo.save(sequence);
				}
			}
        }

        try {

            slideRepo.delete((Slide) getSlide(slideId));

        } catch(IllegalArgumentException exception) {
            throw new SlideDoesNotExistException(exception);
        }
    }

    @Override
    public List<Sequence> getSlideSequences(String slideId, String moduleId) {

        List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
        List<Sequence> sequenceSlides = new ArrayList<>();
        for(Sequence sequence : sequences) {
            Iterator<ISlide> slideIterator = sequence.getSlides().iterator();
            while(slideIterator.hasNext()) {
                if(slideIterator.next().getId().equals(slideId)){
                    sequenceSlides.add(sequence);
                }
            } 
        }
        return sequenceSlides;
    }
}

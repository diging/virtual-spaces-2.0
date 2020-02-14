package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public void deleteSlideById(String slideId) throws SlideDoesNotExistException {
        try {
        	System.out.println("Inside deleteSlideById method() ------->");
        	System.out.println("Slide ID to delete: ----> "+slideId);
            slideRepo.delete((Slide) getSlide(slideId));
        } catch (IllegalArgumentException | EmptyResultDataAccessException exception) {
            throw new SlideDoesNotExistException(exception);
        }
    }

	@Override
	public void deleteSlideBySequence(String slideId, String moduleId, int hasSequence) throws SlideDoesNotExistException {
		System.out.println("Inside deleteSlideBySequence() --->"+slideId);
		List<Sequence> sequence = sequenceRepo.findSequencesForModule(moduleId);
		List<String> sequenceIds = new ArrayList<>();
//		for(int i = 0; i<sequence.size(); i++) {
//			sequenceRepo.deleteById(sequence.get(i).getId());
//		}
		for(int i = 0; i<sequence.size(); i++) {
			sequenceIds.add(sequence.get(i).getId());
			
		}
		System.out.println(sequenceIds);
		sequenceRepo.deleteSlideIdFromSequence1(sequenceIds);
		slideRepo.delete((Slide) getSlide(slideId));

	}
}

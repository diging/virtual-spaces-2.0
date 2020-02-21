package edu.asu.diging.vspace.core.services.impl;

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
	public void deleteSlideById(String slideId, String moduleId, String flag) throws SlideDoesNotExistException {
		try {

			if (flag.equals("0")) {
				slideRepo.delete((Slide) getSlide(slideId));
			}
			// Slide part of another sequence
			else {
				List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
				int sizeOfSequences = sequences.size();
				for (int i = 0; i < sizeOfSequences; i++) {
					
					if (sequences.get(i).getSlides().size() > 0) {
						int sizeOfSlides = sequences.get(i).getSlides().size();
						
						for(int j = 0; j < sizeOfSlides; j++) {
							if (sequences.get(i).getSlides().get(j).getId().equals(slideId)) {
								sequences.get(i).getSlides().remove(j);
								sequenceRepo.save(sequences.get(i));
								slideRepo.delete((Slide) getSlide(slideId));
							}
						}
						
					}

				}
			}
		} catch (IllegalArgumentException exception) {
			throw new SlideDoesNotExistException(exception);
		}
	}
}

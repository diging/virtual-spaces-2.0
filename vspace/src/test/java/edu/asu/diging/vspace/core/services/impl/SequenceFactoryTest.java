package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.factory.impl.SequenceFactory;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public class SequenceFactoryTest {
    
    @Mock
    private SlideManager slideManager;

    @InjectMocks
    private SequenceFactory sequenceFactoryToTest = new SequenceFactory();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void createSequence_Success() {
        Module module = new Module();
        module.setId("module1");
        
        SequenceForm sequenceForm = new SequenceForm();
        sequenceForm.setName("SEQ1");
        sequenceForm.setDescription("sample description");
        List<String> orderedSlides = new ArrayList<>();
        orderedSlides.add("SLI1");
        orderedSlides.add("SLI2");
        sequenceForm.setOrderedSlides(orderedSlides);
        
        List<ISlide> slides = new ArrayList<>();
        for(String slideId : sequenceForm.getOrderedSlides()) {
            slides.add(slideManager.getSlide(slideId));
        }
        
        ISequence newSequence = new Sequence();      
        newSequence.setName(sequenceForm.getName());;
        newSequence.setDescription(sequenceForm.getDescription());
        newSequence.setSlides(slides);
        newSequence.setModule(module);       
        
        ISequence actualSequence = sequenceFactoryToTest.createSequence(module, sequenceForm, slides);
        Assert.assertEquals(newSequence.getId(), actualSequence.getId());
    }
}

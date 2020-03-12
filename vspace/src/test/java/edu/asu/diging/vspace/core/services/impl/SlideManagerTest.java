package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class SlideManagerTest {

    @Mock
    private SequenceRepository sequenceRepo;
    
    @Mock
    private SlideRepository slideRepo;
    
    @InjectMocks
    private SlideManager slideManagerToTest = new SlideManager();
    
    @InjectMocks
    private SlideManager slideManagerToTestSpy =  Mockito.spy(slideManagerToTest);
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    // setting common used variables and Objects
    String slideId, moduleId, sequenceId, slideIdNotInSequence;
    List<ISlide> slidesList = Arrays.asList(new Slide());
    Sequence sequenceObj;
    
    
    @Before
    public void setup() throws Exception {
        sequenceObj = new Sequence() ;
        slideId = "SLI000000002";
        slideIdNotInSequence = "SLI000000219";
        moduleId = "MOD000000002";
        sequenceId = "SEQ000000004";
        slidesList.get(0).setId(slideId);
    }
    
    @Test
    public void test_slideSequence() {
        
        // Positive scenario - Slide present in Sequence
        
        sequenceObj.setId(sequenceId);
        sequenceObj.setSlides(slidesList);
        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);
        List<Sequence> slideSequencePresent = slideManagerToTest.getSlideSequences(slideId, moduleId);
        
        if(slideSequencePresent.size() > 0) {
            String actualSlideIdSequence = slideSequencePresent.get(0).getSlides().get(0).getId();
            String expectedSlideIdSequence = sequencesList.get(0).getSlides().get(0).getId();
            
            Assert.assertEquals(actualSlideIdSequence, expectedSlideIdSequence);
            Assert.assertEquals(slideSequencePresent.size(), sequencesList.size()); 
        }
        
        // Negative Scenario - Slide not present in Sequence
        List<Sequence> actualSequenceSlideListNotPresent = slideManagerToTest.getSlideSequences(slideId, moduleId);
        Assert.assertNotSame(actualSequenceSlideListNotPresent, slideSequencePresent);
    }
    
    @Test
    public void test_deleteSlideById() throws SlideDoesNotExistException {
        
        
        sequenceObj.setId(sequenceId);
        sequenceObj.setSlides(slidesList);
        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);
        
        ISlide slide = new Slide();
        slide.setId(slideId);
        Mockito.doReturn(slide).when(slideManagerToTestSpy).getSlide(Mockito.any());
        
        slideManagerToTestSpy.deleteSlideById(slideId, moduleId);  
        Mockito.verify(slideRepo).delete((Slide) slide);
    }
    
  
}

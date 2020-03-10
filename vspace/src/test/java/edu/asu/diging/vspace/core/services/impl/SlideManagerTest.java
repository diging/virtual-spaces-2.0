package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class SlideManagerTest {

    @Mock
    private SequenceRepository sequenceRepo;
    
    @InjectMocks
    private SlideManager slideManagerToTest = new SlideManager();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_slideSequence() {
        String slideId = "SLI000000002";
        String moduleId = "MOD000000002";
        
        
        List<ISlide> slidesList = Arrays.asList(new Slide());
        slidesList.get(0).setId("SLI000000002");
        
        Sequence sequenceObj = new Sequence();
        sequenceObj.setId("SEQ000000004");
        sequenceObj.setSlides(slidesList);
        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        
        
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);
        
        List<Sequence> actualSequenceSlideList = slideManagerToTest.getSlideSequences(slideId, moduleId);
        
        Assert.assertEquals(actualSequenceSlideList.get(0).getSlides().get(0).getId(), sequencesList.get(0).getSlides().get(0).getId());
        Assert.assertEquals(actualSequenceSlideList.get(0).getSlides(), sequencesList.get(0).getSlides());
        Assert.assertEquals(actualSequenceSlideList.size(), sequencesList.size()); 
    }
    
    
    
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

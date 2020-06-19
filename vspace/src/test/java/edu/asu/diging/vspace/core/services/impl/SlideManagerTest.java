package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // setting common used variables and Objects
    private String slideId, slideIdNotPresent, slideIdOther, moduleId, sequenceId, slideIdNotInSequence, sequenceIdOther;
    private List<ISlide> slidesList = new ArrayList<ISlide>();
    private List<ISlide> slidesListOther = new ArrayList<ISlide>();
    private Sequence sequenceObj;
    private Sequence sequenceObjOther;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        sequenceObj = new Sequence();
        sequenceObjOther = new Sequence();

        slideId = "SLI000000002";
        slideIdOther = "SLI000000001";
        slideIdNotPresent = "SLI000000300";
        slideIdNotInSequence = "SLI000000219";
        
        moduleId = "MOD000000002";
        
        sequenceId = "SEQ000000004";
        sequenceIdOther = "SEQ000000005";

        Slide slideObj = new Slide();
        slideObj.setId(slideId);

        Slide slideObjOther = new Slide();
        slideObjOther.setId(slideIdOther);

        slidesList.add(slideObj);
        slidesListOther.add(slideObj);
        slidesListOther.add(slideObjOther);
    }

    @Test
    public void test_getSlideSequence_slideExistsInSequence() {

        // Positive scenario - Slide present in Sequence

        sequenceObj.setId(sequenceId);
        sequenceObj.setSlides(slidesList);
        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);
        List<Sequence> slideSequencePresent = slideManagerToTest.getSlideSequences(slideId, moduleId);

        String actualSlideIdSequence = slideSequencePresent.get(0).getSlides().get(0).getId();

        Assert.assertEquals(slideId, actualSlideIdSequence);
        Assert.assertEquals(slideSequencePresent.size(), sequencesList.size());

    }

    @Test
    public void test_getSlideSequence_slideNotExistsInSequence() {

        sequenceObj.setId(sequenceId);
        sequenceObj.setSlides(slidesList);
        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);

        List<Sequence> actualSequenceSlideListNotPresent = slideManagerToTest.getSlideSequences(slideIdNotInSequence,
                moduleId);
        Assert.assertTrue(actualSequenceSlideListNotPresent.isEmpty());
    }

    @Test
    public void test_deleteSlideById_slideIdPresent() throws SlideDoesNotExistException {

        sequenceObj.setId(sequenceId);
        sequenceObj.setSlides(slidesList);
        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);

        Slide slide = new Slide();
        slide.setId(slideId);
        Optional<Slide> slideObj = Optional.of(slide);
        Mockito.when(slideRepo.findById(slide.getId())).thenReturn(slideObj);
        slideManagerToTest.deleteSlideById(slideId, moduleId);
        Mockito.verify(slideRepo).delete((Slide) slide);
    }

    @Test
    public void test_deleteSlideById_slideIdNotPresent() throws SlideDoesNotExistException {

        Mockito.when(slideRepo.findById(slideIdNotPresent)).thenReturn(Optional.empty());
        slideManagerToTest.deleteSlideById(slideIdNotPresent, moduleId);
        Mockito.verify(slideRepo, Mockito.never()).deleteById(slideIdNotPresent);

    }
    
    @Test
    public void test_deleteSlideById_slideIdIsNull() {

        Mockito.when(slideRepo.findById(null)).thenReturn(Optional.empty());
        slideManagerToTest.deleteSlideById(null, moduleId);
        Mockito.verify(slideRepo, Mockito.never()).deleteById(slideIdNotPresent);
    }

    @Test
    public void test_deleteSlideById_slideIdPresentManySequences() throws SlideDoesNotExistException {

        sequenceObj.setId(sequenceId);
        sequenceObj.setSlides(slidesList);

        sequenceObjOther.setId(sequenceIdOther);
        sequenceObjOther.setSlides(slidesList);
        sequenceObjOther.setSlides(slidesListOther);

        List<Sequence> sequencesList = new ArrayList<>();
        sequencesList.add(sequenceObj);
        sequencesList.add(sequenceObjOther);
        ISlide slideObjBeforeDeletion = sequencesList.get(1).getSlides().get(0);
        Mockito.when(sequenceRepo.findSequencesForModule(moduleId)).thenReturn(sequencesList);

        ISlide slideObj = slidesList.get(0);
        Mockito.when(slideRepo.findById(slideObj.getId())).thenReturn(Optional.of((Slide) slideObj));
        slideManagerToTest.deleteSlideById(slideId, moduleId);
        Mockito.verify(slideRepo).delete((Slide) slideObj);
        
        Assert.assertFalse(sequencesList.get(1).getSlides().contains(slideObjBeforeDeletion));
        
        
    }
}

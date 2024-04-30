package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

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
import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public class SlideManagerTest {

    @Mock
    private SequenceRepository sequenceRepo;

    @Mock
    private SlideRepository slideRepo;
    
    @Mock
    private IModuleManager moduleManager;
    
    @Mock
    private ExhibitionManager exhibitionManager;


    @InjectMocks
    private SlideManager slideManagerToTest = new SlideManager();
    
    @Mock
    private LocalizedTextRepository localizedRextRepo;

    @Mock
    private ExhibitionLanguageRepository exhibitionLanguageRepository;

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
    
    @Test
    public void test_createSlide_success() {
        List<Slide> slidePageList = new ArrayList<Slide>();

        slidePageList.add(new Slide());

        SlideForm slideForm = new SlideForm();
        List<LocalizedTextForm> titleList = new ArrayList<LocalizedTextForm>();
        titleList.add(new LocalizedTextForm("title", "ID1", "langId", "English"));
        List<LocalizedTextForm> slideTextList = new ArrayList<LocalizedTextForm>();

        slideTextList.add(new LocalizedTextForm( "slide text","ID2", "langId", "English"));


        slideForm.setNames(titleList);
        slideForm.setDescriptions(slideTextList);
        slideForm.setType("Slide");
        when(slideRepo.findAll()).thenReturn(slidePageList);

        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");

        LocalizedText locText2 =  new LocalizedText();
        locText1.setId( "ID2");
        when(localizedRextRepo.findById("ID1") ).thenReturn(Optional.of(locText1));
        when(localizedRextRepo.findById("ID2") ).thenReturn(Optional.of(locText2));
        
        Slide slide = new Slide();
        Exhibition exhibition = new Exhibition();
        when(exhibitionManager.getStartExhibition()).thenReturn((IExhibition)exhibition);
        when(slideRepo.save(slide)).thenReturn(slide);
        assertEquals(locText1.getText(), "title");
        assertEquals(locText2.getText(), "slide text");
    }
    
    @Test
    public void test_createSlide_failure() {
        List<Slide> slidePageList = new ArrayList<Slide>();

        slidePageList.add(new Slide());

        SlideForm slideForm = new SlideForm();
        List<LocalizedTextForm> titleList = new ArrayList<LocalizedTextForm>();
        titleList.add(new LocalizedTextForm("title", "ID1", "langId", "English"));
        List<LocalizedTextForm> slideTextList = new ArrayList<LocalizedTextForm>();

        slideTextList.add(new LocalizedTextForm( "slide text","ID2", "langId", "English"));

        slideForm.setNames(titleList);
        slideForm.setDescriptions(slideTextList);
        slideForm.setType("Slide");
        when(slideRepo.findAll()).thenReturn(slidePageList);

        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");
        Exhibition exhibition = new Exhibition();
        when(exhibitionManager.getStartExhibition()).thenReturn((IExhibition)exhibition);


        when(localizedRextRepo.findById("ID1") ).thenReturn(Optional.empty());
        when(localizedRextRepo.findById("ID2") ).thenReturn(Optional.empty());
        IModule module = moduleManager.getModule(moduleId);
        SlideType type = slideForm.getType().isEmpty() ? null : SlideType.valueOf(slideForm.getType());

        when(exhibitionLanguageRepository.findById("langId")).thenReturn(Optional.empty());
        slideManagerToTest.createSlide(module, slideForm, type);
        assertEquals(locText1.getText(), null);
    } 
    
}

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
import edu.asu.diging.vspace.core.factory.impl.SequenceFactory;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

public class SequenceManagerTest {

    @Mock
    private SequenceRepository mockSequenceRepo;

    @Mock
    private ModuleManager moduleManager;
    
    @Mock
    private SlideManager slideManager;
    
    @Mock
    private SlideRepository slideRepo;
    
    @Mock
    private SequenceFactory sequenceFactory;
    
    @InjectMocks
    private SequenceManager sequenceManagerToTest = new SequenceManager();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getSequence_idExists() {
        Sequence newSequence = new Sequence();
        newSequence.setId("sequence1");
        Optional<Sequence> mockSequence = Optional.of(newSequence);
        Mockito.when(mockSequenceRepo.findById(newSequence.getId())).thenReturn(mockSequence);

        ISequence actualSequence = sequenceManagerToTest.getSequence(newSequence.getId());
        Assert.assertEquals(newSequence.getId(), actualSequence.getId());
        Assert.assertEquals(newSequence.getName(), actualSequence.getName());
    }
    
    @Test
    public void test_getSequence_idNotExists() throws Exception {
        Mockito.when(mockSequenceRepo.findById("a non existing id string")).thenReturn(Optional.empty());
        Assert.assertNull(sequenceManagerToTest.getSequence("a non existing id string"));
    }
    
    @Test
    public void test_storeSequence_success() {        
        Module module = new Module();
        module.setId("module1");
             
        SequenceForm sequenceForm = new SequenceForm();
        sequenceForm.setName("SEQ1");
        sequenceForm.setDescription("sample description");
              
        String slideId1 = "slide1";
        String slideId2 = "slide2";
        List<String> slideIds = new ArrayList<>();
        slideIds.add(slideId1);
        slideIds.add(slideId2);
        sequenceForm.setOrderedSlides(slideIds);
        
        ISlide slide1 = new Slide();
        slide1.setId("slide1");       
        ISlide slide2 = new Slide();
        slide2.setId("slide2");
 
        List<ISlide> slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);
            
        Sequence newSequence = new Sequence();
        newSequence.setModule(module);
        newSequence.setName(sequenceForm.getName());
        newSequence.setDescription(sequenceForm.getDescription());
        newSequence.setSlides(slides);
              
        Mockito.when(slideManager.getSlide(slideId1)).thenReturn(slide1);
        Mockito.when(slideManager.getSlide(slideId2)).thenReturn(slide2);
        Mockito.when(moduleManager.getModule("module1")).thenReturn(module);
        Mockito.when(sequenceFactory.createSequence(module, sequenceForm, slides)).thenReturn(newSequence);
        Mockito.when(mockSequenceRepo.save(newSequence)).thenReturn(newSequence);
        
        ISequence actualSequence = sequenceManagerToTest.storeSequence(module.getId(), sequenceForm);

        Assert.assertEquals(actualSequence.getModule().getId(), module.getId());
        Assert.assertEquals(actualSequence.getName(), sequenceForm.getName());
        Assert.assertEquals(actualSequence.getDescription(), sequenceForm.getDescription());
        Assert.assertEquals(actualSequence.getSlides(), slides);
        Mockito.verify(mockSequenceRepo).save(newSequence);           
    }
}

package edu.asu.diging.vspace.core.services.impl;

import java.util.Arrays;
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
        newSequence.setId("seq1");
        Optional<Sequence> sequence = Optional.of(newSequence);
        Mockito.when(mockSequenceRepo.findById(newSequence.getId())).thenReturn(sequence);

        ISequence actualSequence = sequenceManagerToTest.getSequence(newSequence.getId());
        Assert.assertEquals(newSequence.getId(), actualSequence.getId());
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
        sequenceForm.setName("seq1");
        sequenceForm.setDescription("sample description");            
        List<String> slideIds = Arrays.asList("slide1", "slide2");
        sequenceForm.setOrderedSlides(slideIds);
        
        List<ISlide> slidesList = Arrays.asList(new Slide(), new Slide());
        slidesList.get(0).setId("slide1");
        slidesList.get(1).setId("slide2");
            
        Sequence newSequence1 = new Sequence();
        newSequence1.setModule(module);
        newSequence1.setName(sequenceForm.getName());
        newSequence1.setDescription(sequenceForm.getDescription());
        newSequence1.setSlides(slidesList);
        
        Sequence newSequence2 = new Sequence();
        newSequence2.setId("001");
        newSequence2.setModule(module);
        newSequence2.setName(sequenceForm.getName());
        newSequence2.setDescription(sequenceForm.getDescription());
        newSequence2.setSlides(slidesList);
              
        Mockito.when(slideManager.getSlide("slide1")).thenReturn(slidesList.get(0));
        Mockito.when(slideManager.getSlide("slide2")).thenReturn(slidesList.get(1));
        Mockito.when(moduleManager.getModule("module1")).thenReturn(module);
        Mockito.when(sequenceFactory.createSequence(module, sequenceForm, slidesList)).thenReturn(newSequence1);
        Mockito.when(mockSequenceRepo.save(newSequence1)).thenReturn(newSequence2);
        
        ISequence actualSequence = sequenceManagerToTest.storeSequence(module.getId(), sequenceForm);

        Assert.assertEquals(actualSequence.getId(), newSequence2.getId());
        Assert.assertEquals(actualSequence.getModule().getId(), module.getId());
        Assert.assertEquals(actualSequence.getName(), sequenceForm.getName());
        Assert.assertEquals(actualSequence.getDescription(), sequenceForm.getDescription());
        Assert.assertEquals(actualSequence.getSlides(), slidesList);
        Mockito.verify(mockSequenceRepo).save(newSequence1); 
       
    }
}

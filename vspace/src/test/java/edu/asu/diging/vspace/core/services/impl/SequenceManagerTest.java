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
        Sequence newSequence = new Sequence();
        Module module = new Module();
        module.setId("module1");
        newSequence.setModule(module);
        
        SequenceForm sequenceForm = new SequenceForm();
        sequenceForm.setName("SEQ1");
        sequenceForm.setDescription("sample description");
        newSequence.setName(sequenceForm.getName());
        newSequence.setDescription(sequenceForm.getDescription());
        
        String sli1 = new String("SLI000000036");
        String sli2 = new String("SLI000000037");
        List<String> sli = new ArrayList<>();
        sli.add(sli1);
        sli.add(sli2);
        sequenceForm.setOrderedSlides(sli);
        
        Slide slide1 = new Slide();
        slide1.setId("SLI000000036");       
        Slide slide2 = new Slide();
        slide2.setId("SLI000000037");
 
        List<ISlide> slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);
        newSequence.setSlides(slides);
        
        Mockito.when(sequenceFactory.createSequence(moduleManager.getModule(module.getId()), sequenceForm, slides)).thenReturn(newSequence);
        Mockito.when(mockSequenceRepo.save(newSequence)).thenReturn(newSequence);
        
        ISequence actualSequence = sequenceManagerToTest.storeSequence(module.getId(), sequenceForm);
        Assert.assertEquals(newSequence, actualSequence);
        
        Assert.assertEquals(actualSequence.getModule().getId(), module.getId());
        Assert.assertEquals(actualSequence.getName(), sequenceForm.getName());
        Assert.assertEquals(actualSequence.getDescription(), sequenceForm.getDescription());
        Assert.assertEquals(actualSequence.getSlides(), slides);
        //Mockito.verify(mockSequenceRepo).save(newSequence);   
        
    }
}

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
    private SequenceManager sequenceToTest = new SequenceManager();
    
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

        ISequence iSequenceActual = sequenceToTest.getSequence(newSequence.getId());
        Assert.assertEquals(mockSequence.get().getId(), iSequenceActual.getId());
        Assert.assertEquals(mockSequence.get().getName(), iSequenceActual.getName());
    }
    
    @Test
    public void test_getSequence_idNotExists() throws Exception {
        Mockito.when(mockSequenceRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(sequenceToTest.getSequence(Mockito.anyString()));
    }
    
    @Test
    public void test_storeSequence_Success() {
        Module module = new Module();
        module.setId("module1");
        
        SequenceForm seqForm = new SequenceForm();
        seqForm.setName("SEQ1");
        seqForm.setDescription("sample description");
        List<String> orderedSlides = new ArrayList<>();
        orderedSlides.add("SLI1");
        orderedSlides.add("SLI2");
        seqForm.setOrderedSlides(orderedSlides);
        
        List<ISlide> slides = new ArrayList<>();
        for(String slideId : seqForm.getOrderedSlides()) {
            slides.add(slideManager.getSlide(slideId));
        }
        
        Sequence newSequence = new Sequence(); 
        Mockito.when((Sequence) sequenceFactory.createSequence(moduleManager.getModule(module.getId()), seqForm, slides)).thenReturn(newSequence);
        Sequence mockSequence = new Sequence();
        Mockito.when(mockSequenceRepo.save(newSequence)).thenReturn(mockSequence);
        
        ISequence iSequenceActual = sequenceToTest.storeSequence(module.getId(), seqForm);
        Assert.assertEquals(mockSequence.getId(), iSequenceActual.getId());
        Assert.assertEquals(mockSequence.getName(), iSequenceActual.getName());       
    }
}

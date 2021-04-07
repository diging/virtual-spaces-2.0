package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.factory.impl.ChoiceFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Sequence;

public class ChoiceFactoryTest {
    
    @Mock
    private SequenceManager mockSequenceManager;

    @InjectMocks
    private ChoiceFactory choiceFactoryToTest = new ChoiceFactory();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createChoice_success() {
        List<String> sequenceIds = new ArrayList<>();
        sequenceIds.add("sequence1");
        sequenceIds.add("sequence2");
        
        ISequence newSequence1 = new Sequence();
        newSequence1.setId("sequence1");
        ISequence newSequence2 = new Sequence();
        newSequence2.setId("sequence2");
        
        List<ISequence> newSequences = new ArrayList<>();
        newSequences.add(newSequence1);
        newSequences.add(newSequence2);
      
        Mockito.when(mockSequenceManager.getSequence("sequence1")).thenReturn(newSequence1);
        Mockito.when(mockSequenceManager.getSequence("sequence2")).thenReturn(newSequence2);

        List<IChoice> actualChoices = choiceFactoryToTest.createChoices(sequenceIds);
        int i = 0;
        for(IChoice actualChoice : actualChoices) {
            Assert.assertEquals(newSequences.get(i).getId(), actualChoice.getSequence().getId());
            i++;
        }
    }
}

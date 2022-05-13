package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;

public class SequenceOverviewManagerTest {
    
    @Mock
    private ModuleRepository moduleRepository;
    
    @Mock
    private ModuleLinkDisplayRepository moduleDisplayLinkRepository;
    
    @Mock
    private SequenceRepository sequenceRepo;
    
    @Mock
    private ModuleManager moduleManager;
    
    @InjectMocks
    private SequenceOverviewManager serviceToTest;
    
    
    private List<ISlide> slides;
    
    private List<SequenceOverview> sequenceOverviewList;
    
    IModule module = new Module();
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        module = new Module();
    }
    
    @Test
    public void showModuleMap_success() {
        slides = new ArrayList<ISlide>();
        sequenceOverviewList = new ArrayList<SequenceOverview>();
        String moduleId = "moduleId";
        
        List<ISequence> sequences = new ArrayList<ISequence>();
        Sequence sequence1 = new Sequence();
        sequence1.setId("1");
        sequence1.setName("Sequence 1");
        sequence1.setSlides(slides);
        sequences.add(sequence1);
        module.setStartSequence(sequence1);
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence1.getId());
        sequenceOverview.setId(sequence1.getId());
        sequenceOverviewList.add(sequenceOverview);
        
        Mockito.when(moduleManager.getModule(moduleId)).thenReturn(module);
        Mockito.when(moduleManager.getModuleSequences(moduleId)).thenReturn(sequences);
        ModuleOverview moduleOverview = serviceToTest.showModuleMap("moduleId");
        assertEquals(sequences.get(0).getId(), moduleOverview.getStartSequence().getId());
        
    }

}

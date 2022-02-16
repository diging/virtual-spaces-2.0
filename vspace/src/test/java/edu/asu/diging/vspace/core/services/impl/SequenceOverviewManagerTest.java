package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;
import edu.asu.diging.vspace.web.SequenceOverviewManager;

public class SequenceOverviewManagerTest {
    
    @Mock
    ModuleRepository moduleRepository;
    
    @Mock
    ModuleLinkDisplayRepository moduleDisplayLinkRepository;
    
    @Mock
    SequenceRepository sequenceRepo;
    
    @Mock
    SequenceOverviewJsonFormat sequenceOverviewJsonFormat;
    
    @InjectMocks
    SequenceOverviewManager serviceToTest;
    
    private List<ISlide> slides;
    
    private List<SequenceOverview> sequenceOverviewList;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void showModuleMap_success() {
        slides = new ArrayList<ISlide>();
        sequenceOverviewList = new ArrayList<SequenceOverview>();
        
        List<Sequence> sequences = new ArrayList<Sequence>();
        Sequence sequence1 = new Sequence();
        sequence1.setId("1");
        sequence1.setName("Sequence 1");
        sequence1.setSlides(slides);
        sequences.add(sequence1);
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence1.getId());
        sequenceOverview.setId(sequence1.getId());
        sequenceOverview.setSlides(sequence1.getSlides());
        sequenceOverviewList.add(sequenceOverview);
        
        Map<Sequence,List<ISlide>> mapSequenceToSlides = new HashMap<Sequence, List<ISlide>>();
        mapSequenceToSlides.put(sequences.get(0),slides);
        
        Mockito.when(sequenceRepo.findSequencesForModule("1")).thenReturn(sequences);
        Mockito.when(sequenceOverviewJsonFormat.constructNodesForSequences(sequences)).thenReturn(sequenceOverviewList);
       
        ModuleOverview moduleOverview = serviceToTest.showModuleMap("1");
        assertEquals(sequenceOverviewList.get(0).getId(), moduleOverview.getSequenceOverview().get(0).getId());
        Mockito.verify(sequenceOverviewJsonFormat).constructNodesForSequences(sequences);
        
    }

}

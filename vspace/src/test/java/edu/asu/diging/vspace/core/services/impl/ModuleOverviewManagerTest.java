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
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;
import edu.asu.diging.vspace.core.services.impl.model.SlideOverview;

public class ModuleOverviewManagerTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private ModuleLinkDisplayRepository moduleDisplayLinkRepository;

    @Mock
    private SequenceRepository sequenceRepo;

    @Mock
    private ModuleManager moduleManager;

    @InjectMocks
    private ModuleOverviewManager serviceToTest;

    private List<ISlide> slides;
    
    private List<ISequence> sequences;
    private Module module;
    private Sequence sequence;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        Space space = new Space();
        space.setId("SPACE_01");

        module = new Module();
        module.setId("MODULE_01");
        
        Module module2 = new Module();
        module2.setId("MODULE_02");
        
        slides = new ArrayList<ISlide>();
        ISlide slide = new Slide();
        slide.setId("SLIDE_01");
        slide.setModule(module);
        slides.add(slide);

        sequences = new ArrayList<ISequence>();
        sequence = new Sequence();
        sequence.setId("SEQUENCE_01");
        sequence.setName("Sequence 1");
        sequence.setSlides(slides);      
        
        sequences.add(sequence);
        module.setStartSequence(sequence);
    }

    @Test
    public void test_getModuleOverview_success() {
        
        String moduleId = "MODULE_01";
        module.setStartSequence(sequence);

        Mockito.when(moduleManager.getModule(moduleId)).thenReturn(module);
        Mockito.when(moduleManager.getModuleSequences(moduleId)).thenReturn(sequences);
        ModuleOverview moduleOverview = serviceToTest.getModuleOverview(moduleId);
        assertEquals(sequence.getId(), moduleOverview.getStartSequence().getId());
    }

    @Test
    public void test_getModuleOverview_checkBranchingPointSuccess() {

        List<IChoice> choices = new ArrayList<IChoice>();
        IChoice choice = new Choice();
        choice.setId("choiceId");
        choice.setName("choiceName");
        choice.setSequence(sequence);
        choices.add(choice);
        
        BranchingPoint branchingPoint = new BranchingPoint();
        branchingPoint.setChoices(choices);

        ISlide slide = branchingPoint;
        slide.setId("BRANCHING_POINT_01");
        slide.setModule(module);

        slides.add(slide);

        String moduleId = "MODULE_01";

        Mockito.when(moduleManager.getModule(moduleId)).thenReturn(module);
        Mockito.when(moduleManager.getModuleSequences(moduleId)).thenReturn(sequences);
        
        ModuleOverview moduleOverview = serviceToTest.getModuleOverview(moduleId);
        assertEquals(sequence.getId(), moduleOverview.getStartSequence().getId());
        assertEquals(true, moduleOverview.getOtherSequences().isEmpty());
    }
    
    @Test
    public void test_getModuleOverview_otherSequencesSuccess() {
        
        List<IChoice> choices = new ArrayList<IChoice>();
        IChoice choice = new Choice();
        choice.setId("choiceId");
        choice.setName("choiceName");
        choice.setSequence(sequence);
        choices.add(choice);
        
        BranchingPoint branchingPoint = new BranchingPoint();
        branchingPoint.setChoices(choices);
       
        sequence.setSlides(slides);      
        
        ISlide slide2 = branchingPoint;
        slide2.setId("SLIDE_02");
        slide2.setModule(module);
        slides.add(slide2);
        
        String moduleId = "MODULE_01";
        
        Sequence sequence2 = new Sequence();
        sequence2.setId("SEQUENCE_02");
        sequence2.setName("Sequence 2");
        sequence2.setSlides(slides);
        sequences.add(sequence2);
        
        Mockito.when(moduleManager.getModule(moduleId)).thenReturn(module);
        Mockito.when(moduleManager.getModuleSequences(moduleId)).thenReturn(sequences);
        
        ModuleOverview moduleOverview = serviceToTest.getModuleOverview(moduleId);
        
        List<SequenceOverview> sequenceOverviews = moduleOverview.getOtherSequences();
        SequenceOverview sequenceOverview = sequenceOverviews.get(0);
        
        assertEquals(sequence2.getId(), sequenceOverview.getId());
        
        List<SlideOverview> slideOverviews = sequenceOverview.getSlideOverviews();            
        int i = 0;
        for(SlideOverview slideOverview : slideOverviews) {
            assertEquals(slideOverview.getId(), slides.get(i).getId());
            i++;
        }
    }
}

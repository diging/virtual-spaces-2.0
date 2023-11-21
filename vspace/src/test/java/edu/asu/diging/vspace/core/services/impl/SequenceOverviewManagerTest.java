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
    private ModuleOverviewManager serviceToTest;

    private List<ISlide> slides;

    IModule module = new Module();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        module = new Module();
    }

    @Test
    public void showModuleMap_success() {
        slides = new ArrayList<ISlide>();
        ISlide slide = new Slide();
        slide.setId("slide1");
        slide.setModule(module);

        slides.add(slide);

        String moduleId = "moduleId";

        List<ISequence> sequences = new ArrayList<ISequence>();
        Sequence sequence1 = new Sequence();
        sequence1.setId("1");
        sequence1.setName("Sequence 1");
        sequence1.setSlides(slides);
        sequences.add(sequence1);
        module.setStartSequence(sequence1);

        Mockito.when(moduleManager.getModule(moduleId)).thenReturn(module);
        Mockito.when(moduleManager.getModuleSequences(moduleId)).thenReturn(sequences);
        ModuleOverview moduleOverview = serviceToTest.showModuleMap("moduleId");
        assertEquals(sequences.get(0).getId(), moduleOverview.getStartSequence().getId());

    }

    @Test
    public void showModuleMap_success_checkBranchingPoint() {
        slides = new ArrayList<ISlide>();

        List<ISequence> sequences = new ArrayList<ISequence>();
        Sequence sequence1 = new Sequence();
        sequence1.setId("1");
        sequence1.setName("Sequence 1");
        sequence1.setSlides(slides);
        sequences.add(sequence1);

        Sequence sequence2 = new Sequence();
        sequence2.setId("2");
        sequence2.setName("Sequence 2");
        sequence2.setSlides(slides);
        sequences.add(sequence2);

        module.setStartSequence(sequence1);

        List<IChoice> choices = new ArrayList<IChoice>();
        IChoice choice = new Choice();
        choice.setId("choiceId");
        choice.setName("choiceName");
        choice.setSequence(sequence1);

        BranchingPoint branchingPoint = new BranchingPoint();
        branchingPoint.setChoices(choices);

        ISlide slide = branchingPoint;
        slide.setId("slide1");
        slide.setModule(module);

        slides.add(slide);

        String moduleId = "moduleId";
        module.setStartSequence(sequence1);

        Mockito.when(moduleManager.getModule(moduleId)).thenReturn(module);
        Mockito.when(moduleManager.getModuleSequences(moduleId)).thenReturn(sequences);
        ModuleOverview moduleOverview = serviceToTest.showModuleMap("moduleId");
        assertEquals(sequences.get(0).getId(), moduleOverview.getStartSequence().getId());

    }

}

package edu.asu.diging.vspace.web.exhibit.view;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class ExhibitionUnpublishedContentTest {

    private static final String NOT_FOUND = "redirect:/exhibit/404";

    @Mock
    private ISpaceManager spaceManager;

    @Mock
    private IModuleManager moduleManager;

    @Mock
    private ISequenceManager sequenceManager;

    @Mock
    private ExhibitionAccessChecker accessChecker;

    private ExhibitionSequencesController sequencesController;

    private ExhibitionSlideController slideController;

    private final Space space = new Space();

    private final Module module = new Module();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sequencesController = new ExhibitionSequencesController();
        slideController = new ExhibitionSlideController();
        for (Object controller : new Object[] { sequencesController, slideController }) {
            ReflectionTestUtils.setField(controller, "spaceManager", spaceManager);
            ReflectionTestUtils.setField(controller, "moduleManager", moduleManager);
            ReflectionTestUtils.setField(controller, "sequenceManager", sequenceManager);
            ReflectionTestUtils.setField(controller, "accessChecker", accessChecker);
        }
        when(spaceManager.getSpace("SPA1")).thenReturn(space);
        when(moduleManager.getModule("MOD1")).thenReturn(module);
    }

    @Test
    public void test_sequence_hiddenSpace() throws Exception {
        when(accessChecker.canView(space)).thenReturn(false);
        String view = sequencesController.sequence(new ExtendedModelMap(), "SEQ1", "MOD1", "SPA1", null, null, null, null);
        assertEquals(NOT_FOUND, view);
        verify(sequenceManager, never()).getSequence(Mockito.any());
    }

    @Test
    public void test_sequence_hiddenModule() throws Exception {
        when(accessChecker.canView(space)).thenReturn(true);
        when(accessChecker.canView(module)).thenReturn(false);
        String view = sequencesController.sequence(new ExtendedModelMap(), "SEQ1", "MOD1", "SPA1", null, null, null, null);
        assertEquals(NOT_FOUND, view);
        verify(sequenceManager, never()).getSequence(Mockito.any());
    }

    @Test
    public void test_slide_hiddenSpace() throws Exception {
        when(accessChecker.canView(space)).thenReturn(false);
        String view = slideController.slide(new ExtendedModelMap(), "SLI1", "SEQ1", "SPA1", null, false, null, null, "MOD1");
        assertEquals(NOT_FOUND, view);
        verify(sequenceManager, never()).getSequence(Mockito.any());
    }

    @Test
    public void test_slide_hiddenModule() throws Exception {
        when(accessChecker.canView(space)).thenReturn(true);
        when(accessChecker.canView(module)).thenReturn(false);
        ExtendedModelMap model = new ExtendedModelMap();
        String view = slideController.slide(model, "SLI1", "SEQ1", "SPA1", null, false, null, null, "MOD1");
        assertEquals(NOT_FOUND, view);
        assertEquals(null, model.get("module"));
        verify(sequenceManager, never()).getSequence(Mockito.any());
    }
}

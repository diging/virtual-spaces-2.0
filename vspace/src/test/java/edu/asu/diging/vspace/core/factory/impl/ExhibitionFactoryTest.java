package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitionFactoryTest {

    @Mock
    private IExhibition exhibition;

    @InjectMocks
    private ExhibitionFactory factory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_CreateExhibition_success() {
        IExhibition exhibition = factory.createExhibition();
        assertNotNull(exhibition);
        assertNotNull(exhibition.getPreviewId());
        assertEquals(ExhibitionModes.ACTIVE, exhibition.getMode());
    }

    @Test
    public void test_UpdatePreviewId_success() {
        ExhibitionFactory factory = new ExhibitionFactory();
        Exhibition exhibition = new Exhibition();
        factory.updatePreviewId(exhibition);
        assertNotNull(exhibition.getPreviewId());
        assertEquals(ExhibitionModes.ACTIVE, exhibition.getMode());
    }
}

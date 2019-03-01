package edu.asu.diging.vspace.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class ExhibitionManagerTest {

    @Mock
    private ExhibitionRepository exhibitRepo;

    @InjectMocks
    private ExhibitionManager serviceToTest;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_storeExhibition_success() {
        Exhibition exhibition = new Exhibition();
        when(exhibitRepo.save(exhibition)).thenReturn(exhibition);
        IExhibition exhibitionTest = serviceToTest.storeExhibition(exhibition);
        assertNotNull(exhibitionTest);
        verify(exhibitRepo).save(exhibition);
    }

    @Test
    public void test_getExhibitionById_success() {
        String id = "ID";
        Exhibition exhibition = new Exhibition();
        exhibition.setId(id);
        Optional<Exhibition> exhibitionOptional = Optional.of(exhibition);;
        when(exhibitRepo.findById(id)).thenReturn(exhibitionOptional);
        
        IExhibition exhibitionTest = serviceToTest.getExhibitionById(id);
        assertEquals(exhibitionTest, exhibition);
        verify(exhibitRepo).findById(id);
    }

}

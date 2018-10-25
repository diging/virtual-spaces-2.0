package edu.asu.diging.vspace.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class ExhibitionManagerTest {

    @Mock
    private ExhibitionRepository exhibitRepo;

    @InjectMocks
    private ExhibitionManager exhibitManager = new ExhibitionManager();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_storeExhibition_success() {
        Exhibition exhibition = new Exhibition();
        when(exhibitRepo.save(exhibition)).thenReturn(exhibition);
        IExhibition exhibitionTest = exhibitManager.storeExhibition(exhibition);
        assertNotNull(exhibitionTest);
        verify(exhibitRepo).save(exhibition);
    }

    @Test
    public void test_getExhibitionById_success() {
        Exhibition exhibition = new Exhibition();
        Optional<Exhibition> findExhibition = Optional.of(exhibition);;
        exhibitManager.storeExhibition(exhibition);
        when(exhibitRepo.findById(exhibition.getId())).thenReturn(findExhibition);
        IExhibition exhibitionTest = exhibitManager.getExhibitionById(findExhibition.get().getId());
        assertEquals(exhibitionTest, exhibition);
        verify(exhibitRepo).findById(exhibition.getId());
    }

}

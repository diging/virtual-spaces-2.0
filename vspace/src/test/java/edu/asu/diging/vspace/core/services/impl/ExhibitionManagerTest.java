package edu.asu.diging.vspace.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
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
  public void test_getExhibitionById_successForPresent() {
    Exhibition exhibition = new Exhibition(); 
    Optional<Exhibition> findExhibition;
    exhibitManager.storeExhibition(exhibition);
    findExhibition = Optional.of(exhibition);
    when(exhibitRepo.findById(exhibition.getId())).thenReturn(findExhibition);
    when(findExhibition.isPresent()).thenReturn(true);
    IExhibition exhibitionTest = exhibitManager.getExhibitionById(findExhibition.get().getId()); 
    assertEquals(exhibitionTest, exhibition);
    verify(findExhibition).isPresent();
    verify(exhibitRepo).findById(exhibition.getId());
  }
  
  @Test
  public void test_getExhibitionById_successForAbsent() {
    Exhibition exhibition = new Exhibition(); 
    Optional<Exhibition> findExhibition;
    findExhibition = Optional.of(exhibition);
    when(exhibitRepo.findById(exhibition.getId())).thenReturn(findExhibition);
    when(findExhibition.isPresent()).thenReturn(false);
    IExhibition exhibitionTest = exhibitManager.getExhibitionById(findExhibition.get().getId()); 
    assertEquals(exhibitionTest, null);
    verify(findExhibition).isPresent();
    verify(exhibitRepo).findById(exhibition.getId());
  }
}

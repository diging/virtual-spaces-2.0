package edu.asu.diging.vspace.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import java.util.Optional;

public class ExhibitionManagerTest {

  @Mock
  private ExhibitionRepository exhibitRepo;
  
  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testStoreExhibitionForSuccess() {
    Exhibition exhibition = new Exhibition();
    Space space = mock(Space.class);
    exhibition.setSpace(space);
    assertNotNull(exhibition);
    when(exhibitRepo.save(exhibition)).thenReturn(exhibition);   
  }
  
  @Test
  public void testStoreExhibitionForNullSpace() {
    Exhibition exhibition = new Exhibition();
    Space space = mock(Space.class);
    exhibition.setSpace(space);
    when(exhibitRepo.save(exhibition)).thenThrow(new NullPointerException());  
  }
  
  @Test
  public void testStoreExhibitionForNull() {
    Exhibition exhibition = new Exhibition();
    when(exhibitRepo.save(exhibition)).thenThrow(new NullPointerException());  
  }
  
  @Test
  public void testGetExhibitionByIdPresent() {
    Exhibition exhibition = new Exhibition();
    Space space = mock(Space.class);
    exhibition.setSpace(space);
    assertNotNull(exhibition);
    exhibition = exhibitRepo.save(exhibition);
    Optional<Exhibition> foundExhibition = exhibitRepo.findById(exhibition.getId());
    when(exhibitRepo.findById(foundExhibition.get().getId())).thenReturn(foundExhibition);
  }
  
  @Test
  public void testGetExhibitionByIdAbsent() {
    when(exhibitRepo.findById(null)).thenReturn(null);
  }
}

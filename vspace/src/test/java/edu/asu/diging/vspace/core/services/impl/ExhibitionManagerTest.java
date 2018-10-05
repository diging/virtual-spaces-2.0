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

public class ExhibitionManagerTest {

  @Mock
  private ExhibitionRepository exhibitRepo;
  
  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }
  @Test
  public void storeExhibition() {
    Exhibition exhibit = new Exhibition();
    assertNotNull(exhibit);
    Space space = mock(Space.class);
    exhibit.setSpace(space);
    when(exhibitRepo.save(exhibit)).thenReturn(exhibit);
        
  }
  public void getExhibitionById() {
    
  }
}

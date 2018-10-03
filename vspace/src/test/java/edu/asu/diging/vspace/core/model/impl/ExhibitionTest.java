package edu.asu.diging.vspace.core.model.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Namratha
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExhibitionTest {
  
  @Mock
  private String id;
  
  @Mock
  private Space space;
  
  @Test
  public void testSetter_setId() {
    final Exhibition exhibition = new Exhibition();
    exhibition.setId("test");
    
    //TODO assert
  }
}

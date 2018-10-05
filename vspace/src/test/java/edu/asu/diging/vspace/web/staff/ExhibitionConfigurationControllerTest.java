package edu.asu.diging.vspace.web.staff;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.ui.Model;
import static org.mockito.Mockito.when;

public class ExhibitionConfigurationControllerTest {
  @Mock
  private SpaceRepository spaceRepo;

  @Mock
  private SpaceManager spaceManager;

  @Mock
  private ExhibitionManager exhibitManager;

  @Mock
  private ExhibitionRepository exhibitRepo;

  @Mock
  private ExhibitionFactory exhibitFactory;

  @Mock 
  private Model model;
 
  @Test
  public void showExhibitionsTest() {
   
    when(model.addAttribute("exhibitionsList")).thenReturn((Model) exhibitRepo.findAll());
    when(model.addAttribute("spacesList")).thenReturn((Model) spaceRepo.findAll());
  }
}

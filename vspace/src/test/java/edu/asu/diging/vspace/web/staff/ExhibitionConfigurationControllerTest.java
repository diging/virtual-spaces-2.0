package edu.asu.diging.vspace.web.staff;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;
import org.mockito.Mock;

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

  //TODO Tests
}

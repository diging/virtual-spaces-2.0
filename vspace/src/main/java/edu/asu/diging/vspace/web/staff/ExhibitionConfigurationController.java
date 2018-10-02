package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

@Controller
public class ExhibitionConfigurationController {

  @Autowired
  private SpaceRepository spaceRepo;

  @Autowired
  private SpaceManager spaceManager;

  @Autowired
  private ExhibitionManager exhibitManager;

  @Autowired
  private ExhibitionRepository exhibitRepo;

  @Autowired
  private ExhibitionFactory exhibitFactory;

  @RequestMapping("/staff/exhibit/config")
  public String showExhibitions(Model model) {
    model.addAttribute("exhibitionsList", exhibitRepo.findAll());
    model.addAttribute("spacesList", spaceRepo.findAll());
    return "staff/exhibit/config";
  }

  /**
   * exhibitID is used when default space of existing exhibition is updated.
   * 
   * @param exhibitID
   * @param spaceID
   * @return
   */
  @RequestMapping(value = "/staff/exhibit/config", method = RequestMethod.POST)
  public String createOrUpdateExhibition(
      @RequestParam(required = false, name = "exhibitionParam") String exhibitID,
      @RequestParam("spaceParam") String spaceID) throws IOException {

    Exhibition exhibit;
    String success = "?success=";
    ISpace space = spaceManager.getSpace(spaceID);
    if (exhibitID.isEmpty()) {
      exhibit = (Exhibition) exhibitFactory.createExhibition();
    } else {
      exhibit = (Exhibition) exhibitManager.getExhibitionById(exhibitID);
    }
    exhibit.setSpace((Space) space);
    exhibit = (Exhibition) exhibitManager.storeExhibition(exhibit);
    if(exhibit.getSpace()!=null) {
      success+=1;
    }
    else {
      success+=0;
    }
      
    return "redirect:/staff/exhibit/config"+success;
  }
}

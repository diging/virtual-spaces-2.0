package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
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
   * exhibitionParam is used when default space of existing exhibition is updated.
   * @param exhibitionParam
   * @param spaceParam
   * @param attributes
   * @return
   */
  @RequestMapping(value = "/staff/exhibit/config", method = RequestMethod.POST)
  public RedirectView createOrUpdateExhibition(
      @RequestParam(required = false, name = "exhibitionParam") String exhibitID,
      @RequestParam("spaceParam") String spaceID, RedirectAttributes attributes) throws IOException {

    Exhibition exhibition;
    ISpace startSpace = spaceManager.getSpace(spaceID);
    
    if (exhibitID==null || exhibitID.isEmpty()) {
      exhibition = (Exhibition) exhibitFactory.createExhibition();
    } else {
      exhibition = (Exhibition) exhibitManager.getExhibitionById(exhibitID);
    } 
    
    exhibition.setSpace(startSpace);
    exhibition = (Exhibition) exhibitManager.storeExhibition(exhibition);
    attributes.addAttribute("alertType", "success");
    attributes.addAttribute("message", "Successfully Saved!");
    attributes.addAttribute("showAlert", "true");
    return new RedirectView("/vspace/staff/exhibit/config");
  }
  
}

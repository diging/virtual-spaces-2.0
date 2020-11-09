package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ListSpacesController {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/space/list")
    public String listSpaces(Model model) {

        model.addAttribute("spaces", spaceManager.addIncomingLinkInfoToSpaces(spaceRepo.findAll()));
        IExhibition startExhibition = exhibitionManager.getStartExhibition();
        if(startExhibition!=null) {
            model.addAttribute("startSpace", startExhibition.getStartSpace());
        }

        return "staff/spaces/spacelist";
    }
}
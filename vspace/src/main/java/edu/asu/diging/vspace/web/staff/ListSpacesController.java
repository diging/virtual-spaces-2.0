package edu.asu.diging.vspace.web.staff;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

@Controller
public class ListSpacesController {

    @Autowired
    private SpaceRepository spaceRepo;
    
    @Autowired
    private SpaceLinkRepository spaceLinkRepo;

    @Autowired
    private ExhibitionManager exhibitionManager;
    
    @Autowired
    private SpaceManager spaceManager;

    @RequestMapping("/staff/space/list")
    public String listSpaces(Model model) {

        model.addAttribute("spaces", spaceManager.addIncomingLinkInfoToSpaces(spaceRepo.findAll()));
        IExhibition startExhibition = exhibitionManager.getStartExhibition();
        if(startExhibition!=null) {
            model.addAttribute("startSpace", startExhibition.getStartSpace());
        }

        return "staff/space/list";
    }
}
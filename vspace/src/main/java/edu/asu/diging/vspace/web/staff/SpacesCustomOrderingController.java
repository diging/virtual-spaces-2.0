package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class SpacesCustomOrderingController {
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @RequestMapping("/staff/spaceordering")
    public String displayCurrentOrder(Model model) {
        List<ISpace> spaces = (List<ISpace>) spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        spaces.addAll(spaceManager.getSpacesWithStatus(null));
        model.addAttribute("spaces", spaces);
        
        return "/staff/spaces/customordering";
    }
    @RequestMapping("/staff/spaceordering/changeorder")
    public String changeDisplayOrder(Model model) {
        List<ISpace> spaces = (List<ISpace>) spaceManager.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        spaces.addAll(spaceManager.getSpacesWithStatus(null));
        model.addAttribute("spaces", spaces);
        
        return "/staff/spaces/customordering";
    }

}

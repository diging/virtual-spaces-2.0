package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ListSpacesController {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/space/list")
    public String listSpaces(Model model) {
        List<String> targetSpaceIds = new ArrayList<>();
        spaceManager.getAllTargetSpaces().forEach(s -> targetSpaceIds.add(s.getId()));
        model.addAttribute("spaces", spaceRepo.findAll());
        model.addAttribute("targetSpacesList", targetSpaceIds);
        return "staff/space/list";
    }
}

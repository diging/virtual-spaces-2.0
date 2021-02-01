package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @RequestMapping(value="/staff/search")
    @GetMapping
    public String getAllSearchedElements(Model model, @RequestParam(name = "searchText") String searchString) {
        HashSet<IVSpaceElement> elementList = staffSearchManager.getAllSearchedElements(searchString);
        for(IVSpaceElement ele : elementList)
        model.addAttribute("searchWord", searchString);
        model.addAttribute("conIndex", new ExternalLinkValue("0"));
        model.addAttribute("searchResult", elementList);
        return "/staff/search/staffSearch";
    }
    
}

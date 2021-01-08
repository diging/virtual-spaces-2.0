package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @RequestMapping(value="/staff/search/{searchString}")
    public String getAllSearchedElements(Model model, @PathVariable("searchString") String searchString) {
        HashSet<IVSpaceElement> elementList = staffSearchManager.getAllContainingElements(searchString);
        for(IVSpaceElement ele : elementList)
            System.out.println(ele.getName());
        model.addAttribute("searchWord", searchString);
        model.addAttribute("searchResult", elementList);
        return "/staff/search/staffSearch";
    }
    
}

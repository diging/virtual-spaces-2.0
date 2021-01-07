package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @RequestMapping(value="/staff/search", method=RequestMethod.POST)
    public String getAllSearchedElements(Model model, HttpServletRequest request, RedirectAttributes attributes, @RequestParam("searchString") String searchString) {
        HashSet<IVSpaceElement> elementList = staffSearchManager.getAllContainingElements(searchString);
        for(IVSpaceElement ele : elementList)
            System.out.println(ele.getName());
        model.addAttribute("searchResult", elementList);
        return "redirect:/staff/search/staffSearch";
    }
    
    @RequestMapping(value="/staff/searchPage")
    public String updateToSearchPage(Model model, RedirectAttributes attributes) {
        System.out.println("Inside redirect");
        System.out.println(model.containsAttribute("searchResult"));
        System.out.println(attributes.containsAttribute("searchResult"));
        model.addAttribute("searchResult", staffSearchManager.getAllContainingElements("sample"));
      //  HashSet<IVSpaceElement> elementList = staffSearchManager.getAllContainingElements(searchString);
      //  for(IVSpaceElement ele : elementList)
      //      System.out.println(ele.getName());
      //  attributes.addAttribute("searchResult", elementList);
        return "/staff/search/staffSearch";
    }
    
}

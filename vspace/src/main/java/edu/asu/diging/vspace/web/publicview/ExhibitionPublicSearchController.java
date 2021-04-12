package edu.asu.diging.vspace.web.publicview;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;

@Controller
public class ExhibitionPublicSearchController {

    @Autowired
    private IPublicSearchManager publicSearchManager;
    
    @RequestMapping(value = "/exhibit/search")
    @GetMapping
    public String getAllSearchedElements(Model model, @RequestParam(name = "searchText") String searchText) {
        System.out.println("Prashant :- "+searchText);
        HashSet<IVSpaceElement> elementList = publicSearchManager.getAllSearchedElements(searchText);
        System.out.println(elementList);
        model.addAttribute("searchWord", searchText);
        model.addAttribute("conIndex", new ExternalLinkValue("0"));
        model.addAttribute("searchResult", elementList);
        model.addAttribute("resultCount", elementList.size());
        return "/exhibit/search";
    }
    
}
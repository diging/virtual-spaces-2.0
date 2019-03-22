package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.impl.SlideManager;

@Controller
public class ContentBlockController {

    @Autowired
    private SlideManager slideManager;
    
    @RequestMapping("/staff/module/slide/{id}/content")
    public String listTextBlocks(@PathVariable String id, Model model) {
        
        model.addAttribute("contents", slideManager.getSlide(id).getContents());
        System.out.println("inside textblock controller");
        System.out.println(slideManager.getSlide(id).getContents());
             
        return "staff/module/slide/contentBlocks";
    }
}

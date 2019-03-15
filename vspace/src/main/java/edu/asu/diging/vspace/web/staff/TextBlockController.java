package edu.asu.diging.vspace.web.staff;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;

@Transactional
@Controller
public class TextBlockController {

    @Autowired
    private ContentBlockManager contentBlockManager;
    
    @RequestMapping("/staff/module/slide/{id}/content")
    public String listTextBlocks(@PathVariable String id, Model model) {
        
        //model.addAttribute("textblocks", contentBlockManager.getSlide(id).getContents());
        System.out.println("inside textblock controller");
        //System.out.println(slideManager.getSlide(id).getContents());
             
        return "staff/module/slide/contentBlocks";
    }
}

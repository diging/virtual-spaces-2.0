package edu.asu.diging.vspace.web.staff;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;
import edu.asu.diging.vspace.core.services.impl.ModuleManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;

@Transactional
@Controller
public class SlideController {

    @Autowired
    private SlideManager slideManager;

    @Autowired
    private ModuleManager moduleManager;

    @Autowired
    private ContentBlockManager contentBlockManager;

    @RequestMapping("/staff/module/{moduleId}/slide/{id}")
    public String listSlides(@PathVariable("id") String id, @PathVariable("moduleId") String moduleId, Model model) {
        
        model.addAttribute("module", moduleManager.getModule(moduleId));
        model.addAttribute("slide", slideManager.getSlide(id));
        model.addAttribute("slideContents", contentBlockManager.getAllContentBlocks(id));
        if(slideManager.getSlide(id) instanceof BranchingPoint) {
            model.addAttribute("choices", ((IBranchingPoint)slideManager.getSlide(id)).getChoices());
        }
                     
        return "staff/module/slide/contents";
    }
}

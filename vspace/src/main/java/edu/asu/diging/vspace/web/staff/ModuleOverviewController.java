package edu.asu.diging.vspace.web.staff;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.impl.ModuleOverviewManager;

@Controller
public class ModuleOverviewController {
    
    public static final String STAFF_MODULE_PATH = "/staff/module/showmodulemap/";
    
    @Autowired
    ModuleOverviewManager moduleOverviewManager;
    
    @RequestMapping(STAFF_MODULE_PATH+"{id}")
    public String showModuleMap(@PathVariable String id, Model model) {
        Map<String,List<ISlide>> mapSequenceToSlides = moduleOverviewManager.getSequencesFromModules(id);
        model.addAttribute("mapSequencesToSlides", mapSequenceToSlides);
        return "";
    }
    
}

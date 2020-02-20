package edu.asu.diging.vspace.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;
import javassist.bytecode.Descriptor.Iterator;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private ContentBlockManager contentBlockManager;

    @RequestMapping(value = "/exhibit/module/{id}")
    public String module(@PathVariable("id") String id, Model model, RedirectAttributes attributes) {
    	IModule module=moduleManager.getModule(id);
    	ISequence startSequence=module.getStartSequence();
    	if(startSequence==null) {
            return "moduleerror";
    	}
    	List<ISequence> sequences = moduleManager.getModuleSequences(id);
    	List<ISlide> slides = sequenceManager.getSequence(startSequence.getId()).getSlides();
    	List<IContentBlock> contents= contentBlockManager.getAllContentBlocks(slides.get(0).getId());
    	
    	Map<ISequence, List<ISlide>> map=new HashMap();
    	for(int i=0;i<sequences.size();i++) {
        	List<ISlide> slide = sequenceManager.getSequence(sequences.get(i).getId()).getSlides();
        	map.put(sequences.get(i),slide);
    	}
    	
    	model.addAttribute("map", map);
        model.addAttribute("module", module);
        model.addAttribute("sequences", moduleManager.getModuleSequences(id));
        model.addAttribute("moduleSlides", moduleManager.getModuleSlides(id));
        model.addAttribute("startSequence", startSequence);
        model.addAttribute("startSlideContents", contents);
        return "modulesuccess";
    }
}

package edu.asu.diging.vspace.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;
import edu.asu.diging.vspace.core.services.impl.SequenceManager;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private SequenceManager sequenceManager;
    
    @Autowired
    private ContentBlockManager contentBlockManager;

    @RequestMapping(value = "/exhibit/module/{id}")
    public String module(@PathVariable("id") String id, Model model) {
        IModule module=moduleManager.getModule(id);
        model.addAttribute("module", module);
        if(module.getStartSequence()==null) {
            return "module";
        }
        else {
            model.addAttribute("startSequenceId", module.getStartSequence().getId());
            model.addAttribute("startSlideId", module.getStartSequence().getSlides().get(0).getId());
            //return "module";
        List<List<IContentBlock>> slideContents=new ArrayList<List<IContentBlock>>();
        List<ISlide> slides = module.getStartSequence().getSlides();
        Iterator<ISlide> iter=slides.iterator();
        while(iter.hasNext()) {
            ISlide slide=iter.next();
            slideContents.add(contentBlockManager.getAllContentBlocks(slide.getId()));
        }
        model.addAttribute("slideContents", slideContents);

        return "module";
        }
    }
    
    @RequestMapping(value="/exhibit/module/{moduleId}/sequence/{startSequenceId}/slide")
    public String slides(@PathVariable("moduleId") String moduleId, @PathVariable("startSequenceId") String startSequenceId, Model model) {
        ISequence sequence=sequenceManager.getSequence(startSequenceId);
        ISlide slide=sequence.getSlides().get(0);
        model.addAttribute("slide",slide);
        return "module";
        
    }
    
    @RequestMapping(value="/exhibit/module/{moduleId}/sequence/{startSequenceId}/slide/{slideId}")
    public ResponseEntity<List<IContentBlock>> slide(@PathVariable("moduleId") String moduleId, @PathVariable("startSequenceId") String startSequenceId, @PathVariable("slideId") String slideId,Model model) {
 
        return new ResponseEntity<List<IContentBlock>>(contentBlockManager.getAllContentBlocks(slideId), HttpStatus.OK);
        
    }
}

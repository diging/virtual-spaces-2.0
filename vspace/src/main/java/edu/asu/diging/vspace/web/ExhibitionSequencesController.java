package edu.asu.diging.vspace.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Controller
public class ExhibitionSequencesController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @RequestMapping(value = "/exhibit/module/{moduleId}/sequence/{sequenceId}")
    public String sequence(Model model, @PathVariable("sequenceId") String sequenceId, @PathVariable("moduleId") String moduleId) {
        IModule module = moduleManager.getModule(moduleId);
        model.addAttribute("module", module);
        List<ISequence> list=moduleManager.getModuleSequences(moduleId);
        boolean flag=true;
        for(ISequence sequence:list) {
            if(sequence.getId().equals(sequenceId)) {
                flag=false;
                break;
            }
        }
        if(flag) {
            model.addAttribute("error","Sequence does not belong to selected module.");
            return "module";
        }
        else if(sequenceManager.getSequence(sequenceId).getSlides().size()==0) {
            model.addAttribute("error","No slides to display in selected sequence for module.");
            return "module";
        }
        String firstSlideId=sequenceManager.getSequence(sequenceId).getSlides().get(0).getId();
        return "redirect:/exhibit/module/"+moduleId+"/sequence/"+sequenceId+"/slide/"+firstSlideId;
    }
}

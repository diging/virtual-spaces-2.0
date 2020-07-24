package edu.asu.diging.vspace.web.staff;

import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.services.impl.ContentBlockManager;
import edu.asu.diging.vspace.core.services.impl.ModuleManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

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

        ISlide slide = slideManager.getSlide(id);
        model.addAttribute("module", moduleManager.getModule(moduleId));
        model.addAttribute("slide", slide);
        List<IContentBlock> slideContents = contentBlockManager.getAllContentBlocks(id);
        model.addAttribute("slideContents", slideContents);
        model.addAttribute("contentCount",slideContents.size()>0 ? slideContents.get(slideContents.size()-1).getContentOrder() : 0);
        if(slideManager.getSlide(id) instanceof BranchingPoint) {
            model.addAttribute("choices", ((IBranchingPoint)slide).getChoices());
        }           
        return "staff/module/slide/contents";
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/contents", method = RequestMethod.GET)
    public ResponseEntity<List<IContentBlock>> getAllContentBlocks(Model model, @PathVariable("moduleId") String moduleId, @PathVariable("id") String slideId, @ModelAttribute SequenceForm sequenceForm,
            Principal principal) {

        List<IContentBlock> slideContents = contentBlockManager.getAllContentBlocks(slideId);
        return new ResponseEntity<List<IContentBlock>>(slideContents, HttpStatus.OK);
    }
}

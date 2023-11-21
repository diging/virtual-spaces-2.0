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
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideDisplayManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Transactional
@Controller
public class SlideController {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private IContentBlockManager contentBlockManager;
    
    @Autowired
    private IExternalLinkManager externalLinkManager;
    
    @Autowired
    private ISlideDisplayManager slideDisplayManager;
   

    @RequestMapping("/staff/module/{moduleId}/slide/{id}")
    public String listSlides(@PathVariable("id") String id, @PathVariable("moduleId") String moduleId, Model model) {

        ISlide slide = slideManager.getSlide(id);
        model.addAttribute("module", moduleManager.getModule(moduleId));
        model.addAttribute("slide", slide);
        model.addAttribute("externalLinks", externalLinkManager.getLinkDisplays(id));
        model.addAttribute("slideSequences", slideManager.getSlideSequences(id, moduleId));
        List<IContentBlock> slideContents = contentBlockManager.getAllContentBlocks(id);
        IImageBlock imageblock = contentBlockManager.getImageBlock(slideContents.get(0).getId());
        slideManager.storeSlideDisplay(slide, imageblock.getImage());
        model.addAttribute("slideContents", slideContents);
        model.addAttribute("display", slideDisplayManager.getBySlide(slide,imageblock.getImage()));
        model.addAttribute("contentCount",slideContents.size()>0 ? slideContents.get(slideContents.size()-1).getContentOrder() : 0);
        if(slideManager.getSlide(id) instanceof BranchingPoint) {
            model.addAttribute("choices", ((IBranchingPoint)slide).getChoices());
        }           
        return "staff/modules/slides/slide";
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/contents", method = RequestMethod.GET)
    public ResponseEntity<List<IContentBlock>> getAllContentBlocks(Model model, @PathVariable("moduleId") String moduleId, @PathVariable("id") String slideId, @ModelAttribute SequenceForm sequenceForm,
            Principal principal) {

        List<IContentBlock> slideContents = contentBlockManager.getAllContentBlocks(slideId);
        return new ResponseEntity<List<IContentBlock>>(slideContents, HttpStatus.OK);
    }
}

package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.factory.ISpaceFormFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class AddSpaceController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceFactory spaceFactory;

    @Autowired
    private IImageService imageService;
    
    @Autowired
    private ISpaceFormFactory spaceFormFactory;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @RequestMapping(value = "/staff/space/add", method = RequestMethod.GET)
    public String showAddSpace(Model model) {
        ISpace space = new Space();
        model.addAttribute("space", spaceFormFactory.createNewSpaceForm(space, exhibitionManager.getStartExhibition()));
        model.addAttribute("images", imageService.getImages(1));     
        
        return "staff/spaces/add";
    }

    @RequestMapping(value = "/staff/space/add", method = RequestMethod.POST)
    public String addSpace(Model model, @ModelAttribute SpaceForm spaceForm, @RequestParam("file") MultipartFile file,
            Principal principal, @RequestParam(value = "imageId", required=false) String imageId, RedirectAttributes redirectAttrs) throws IOException {
        ISpace space;
        /*
         * Check if the default name is provided. If a non-default name is provided, create a new form without default values
         */
        if (!spaceForm.getDefaultName().getText().isEmpty()) {
            SpaceForm nonDefaultSpaceForm = new SpaceForm(); 
            LocalizedTextForm nameSpace = spaceForm.getNames().stream().filter(name -> name != null).findFirst().orElse(null);
            LocalizedTextForm descriptionSpace = spaceForm.getDescriptions().stream().filter(description -> description != null).findFirst().orElse(null);
            nonDefaultSpaceForm.setDefaultDescription(descriptionSpace);
            nonDefaultSpaceForm.setDefaultName(nameSpace);
            space = spaceFactory.createSpace(nonDefaultSpaceForm);
            spaceManager.updateNameAndDescription(space, nonDefaultSpaceForm);
        } else {
            // If only the default name is provided, create the space with the default form
            space = spaceFactory.createSpace(spaceForm);
            spaceManager.updateNameAndDescription(space, spaceForm);
        } 
        
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);      
        exhibitionManager.getStartExhibition();

        byte[] bgImage = null;
        String filename = null;
        if (file != null) {
            bgImage = file.getBytes();
            filename = file.getOriginalFilename();
        } 
        CreationReturnValue creationValue = null;
        if(imageId != null && !imageId.isEmpty()) {
            IVSImage image;
            try {
                image = imageService.getImageById(imageId);
            } catch (ImageDoesNotExistException e) {
                logger.error("Image does not exist.", e);
                redirectAttrs.addAttribute("showAlert", true);
                redirectAttrs.addAttribute("alertType", "danger");
                redirectAttrs.addAttribute("message", "Selected image does not exist.");
                return "redirect:/staff/spaces/spacelist";
            }
            creationValue = spaceManager.storeSpace(space, image);
        }else {
            creationValue = spaceManager.storeSpace(space, bgImage, filename);
        }

        if (creationValue != null) {
            return "redirect:/staff/space/" + creationValue.getElement().getId();
        }

        redirectAttrs.addAttribute("showAlert", true);
        redirectAttrs.addAttribute("alertType", "danger");
        redirectAttrs.addAttribute("message", "Unkown error. Space could not be created.");
        return "redirect:/staff/spaces/spacelist";
    }

}
